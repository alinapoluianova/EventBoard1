package com.eventboard.repository;

import com.eventboard.model.Event;
import com.eventboard.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventRepository {
    public List<Event> findFutureEvents() {
        String sql = "SELECT id, title, event_date, max_seats FROM events WHERE event_date >= CURRENT_DATE ORDER BY event_date";
        List<Event> events = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                events.add(mapEvent(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot load events", e);
        }

        return events;
    }

    public Event findById(int id) {
        String sql = "SELECT id, title, event_date, max_seats FROM events WHERE id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapEvent(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot find event", e);
        }

        return null;
    }

    public void save(Event event) {
        String sql = "INSERT INTO events (title, event_date, max_seats) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, event.getTitle());
            statement.setDate(2, Date.valueOf(event.getEventDate()));
            statement.setInt(3, event.getMaxSeats());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot save event", e);
        }
    }

    public int countParticipantsByEventId(int eventId) {
        String sql = "SELECT COUNT(*) FROM participants WHERE event_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot count participants", e);
        }

        return 0;
    }

    private Event mapEvent(ResultSet resultSet) throws SQLException {
        return new Event(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getDate("event_date").toLocalDate(),
                resultSet.getInt("max_seats")
        );
    }
}

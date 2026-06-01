package com.eventboard.repository;

import com.eventboard.model.Participant;
import com.eventboard.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParticipantRepository {
    public List<Participant> findByEventId(int eventId) {
        String sql = "SELECT id, event_id, student_name, student_email FROM participants WHERE event_id = ? ORDER BY id";
        List<Participant> participants = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    participants.add(new Participant(
                            resultSet.getInt("id"),
                            resultSet.getInt("event_id"),
                            resultSet.getString("student_name"),
                            resultSet.getString("student_email")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot load participants", e);
        }

        return participants;
    }

    public void save(Participant participant) {
        String sql = "INSERT INTO participants (event_id, student_name, student_email) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, participant.getEventId());
            statement.setString(2, participant.getStudentName());
            statement.setString(3, participant.getStudentEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot save participant", e);
        }
    }
}

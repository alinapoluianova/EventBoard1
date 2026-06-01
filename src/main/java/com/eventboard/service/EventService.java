package com.eventboard.service;

import com.eventboard.model.Event;
import com.eventboard.model.Participant;
import com.eventboard.repository.EventRepository;
import com.eventboard.repository.ParticipantRepository;

import java.util.List;

public class EventService {
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;

    public EventService(EventRepository eventRepository, ParticipantRepository participantRepository) {
        this.eventRepository = eventRepository;
        this.participantRepository = participantRepository;
    }

    public List<Event> getFutureEvents() {
        List<Event> events = eventRepository.findFutureEvents();

        for (Event event : events) {
            setFreeSeats(event);
        }

        return events;
    }

    public Event getEventById(int id) {
        Event event = eventRepository.findById(id);

        if (event != null) {
            setFreeSeats(event);
        }

        return event;
    }

    public List<Participant> getParticipantsByEventId(int eventId) {
        return participantRepository.findByEventId(eventId);
    }

    public void createEvent(Event event) {
        if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (event.getEventDate() == null) {
            throw new IllegalArgumentException("Event date is required");
        }

        if (event.getMaxSeats() <= 0) {
            throw new IllegalArgumentException("Max seats must be greater than zero");
        }

        eventRepository.save(event);
    }

    public void registerParticipant(int eventId, String studentName, String studentEmail) {
        Event event = eventRepository.findById(eventId);

        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }

        if (studentName == null || studentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name is required");
        }

        if (studentEmail == null || studentEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Student email is required");
        }

        int freeSeats = countFreeSeats(event);

        if (freeSeats <= 0) {
            throw new IllegalStateException("No free seats available");
        }

        Participant participant = new Participant(eventId, studentName.trim(), studentEmail.trim());
        participantRepository.save(participant);
    }

    public int getFreeSeats(int eventId) {
        Event event = eventRepository.findById(eventId);

        if (event == null) {
            return 0;
        }

        int registeredCount = eventRepository.countParticipantsByEventId(eventId);
        return event.getMaxSeats() - registeredCount;
    }

    private void setFreeSeats(Event event) {
        event.setFreeSeats(countFreeSeats(event));
    }

    private int countFreeSeats(Event event) {
        int registeredCount = eventRepository.countParticipantsByEventId(event.getId());
        return event.getMaxSeats() - registeredCount;
    }
}

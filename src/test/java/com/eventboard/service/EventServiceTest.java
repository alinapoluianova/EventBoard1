package com.eventboard.service;

import com.eventboard.model.Event;
import com.eventboard.model.Participant;
import com.eventboard.repository.EventRepository;
import com.eventboard.repository.ParticipantRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EventServiceTest {
    @Test
    void registerParticipantShouldThrowExceptionWhenNoFreeSeats() {
        FakeEventRepository eventRepository = new FakeEventRepository();
        FakeParticipantRepository participantRepository = new FakeParticipantRepository();
        EventService eventService = new EventService(eventRepository, participantRepository);

        eventRepository.event = new Event(1, "Java Meetup", LocalDate.now().plusDays(1), 2);
        eventRepository.participantsCount = 2;

        assertThrows(IllegalStateException.class, () ->
                eventService.registerParticipant(1, "Ivan Petrenko", "ivan@gmail.com"));

        assertFalse(participantRepository.participantSaved);
    }

    private static class FakeEventRepository extends EventRepository {
        private Event event;
        private int participantsCount;

        @Override
        public Event findById(int id) {
            return event;
        }

        @Override
        public int countParticipantsByEventId(int eventId) {
            return participantsCount;
        }
    }

    private static class FakeParticipantRepository extends ParticipantRepository {
        private boolean participantSaved;

        @Override
        public void save(Participant participant) {
            participantSaved = true;
        }
    }
}

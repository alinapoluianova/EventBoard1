package com.eventboard.servlet;

import com.eventboard.model.Event;
import com.eventboard.repository.EventRepository;
import com.eventboard.repository.ParticipantRepository;
import com.eventboard.service.EventService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/events")
public class EventsServlet extends HttpServlet {
    private EventService eventService;

    @Override
    public void init() {
        EventRepository eventRepository = new EventRepository();
        ParticipantRepository participantRepository = new ParticipantRepository();
        eventService = new EventService(eventRepository, participantRepository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("events", eventService.getFutureEvents());
        request.getRequestDispatcher("/views/events.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        try {
            String title = request.getParameter("title");
            LocalDate eventDate = LocalDate.parse(request.getParameter("event_date"));
            int maxSeats = Integer.parseInt(request.getParameter("max_seats"));

            Event event = new Event(title, eventDate, maxSeats);
            eventService.createEvent(event);

            response.sendRedirect(request.getContextPath() + "/events");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("events", eventService.getFutureEvents());
            request.getRequestDispatcher("/views/events.jsp").forward(request, response);
        }
    }
}

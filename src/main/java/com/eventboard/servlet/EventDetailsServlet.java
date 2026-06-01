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

@WebServlet("/event")
public class EventDetailsServlet extends HttpServlet {
    private EventService eventService;

    @Override
    public void init() {
        EventRepository eventRepository = new EventRepository();
        ParticipantRepository participantRepository = new ParticipantRepository();
        eventService = new EventService(eventRepository, participantRepository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int eventId = Integer.parseInt(request.getParameter("id"));
        showEventPage(request, response, eventId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        int eventId = Integer.parseInt(request.getParameter("event_id"));
        String studentName = request.getParameter("student_name");
        String studentEmail = request.getParameter("student_email");

        try {
            eventService.registerParticipant(eventId, studentName, studentEmail);
            response.sendRedirect(request.getContextPath() + "/event?id=" + eventId);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showEventPage(request, response, eventId);
        }
    }

    private void showEventPage(HttpServletRequest request, HttpServletResponse response, int eventId)
            throws ServletException, IOException {
        Event event = eventService.getEventById(eventId);

        if (event == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Event not found");
            return;
        }

        request.setAttribute("event", event);
        request.setAttribute("participants", eventService.getParticipantsByEventId(eventId));
        request.getRequestDispatcher("/views/event-details.jsp").forward(request, response);
    }
}

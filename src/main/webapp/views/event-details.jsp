<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventboard.model.Event" %>
<%@ page import="com.eventboard.model.Participant" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Event details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<main class="container">
    <a href="${pageContext.request.contextPath}/events">Назад до заходів</a>

    <%
        Event event = (Event) request.getAttribute("event");
        List<Participant> participants = (List<Participant>) request.getAttribute("participants");
    %>

    <h1><%= event.getTitle() %></h1>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <section class="block">
        <h2>Інформація</h2>
        <p>Дата: <strong><%= event.getEventDate() %></strong></p>
        <p>Максимальна кількість місць: <strong><%= event.getMaxSeats() %></strong></p>
        <p>Вільно місць: <strong><%= event.getFreeSeats() %></strong></p>
    </section>

    <section class="block">
        <h2>Учасники</h2>

        <table>
            <thead>
            <tr>
                <th>Ім'я</th>
                <th>Email</th>
            </tr>
            </thead>
            <tbody>
            <%
                if (participants == null || participants.isEmpty()) {
            %>
            <tr>
                <td colspan="2">Ще ніхто не зареєструвався.</td>
            </tr>
            <%
                } else {
                    for (Participant participant : participants) {
            %>
            <tr>
                <td><%= participant.getStudentName() %></td>
                <td><%= participant.getStudentEmail() %></td>
            </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </section>

    <section class="block">
        <h2>Реєстрація</h2>

        <form method="post" action="${pageContext.request.contextPath}/event">
            <input type="hidden" name="event_id" value="<%= event.getId() %>">

            <label>
                Ім'я студента
                <input type="text" name="student_name" required>
            </label>

            <label>
                Email
                <input type="email" name="student_email" required>
            </label>

            <button type="submit">Зареєструватися</button>
        </form>
    </section>
</main>
</body>
</html>

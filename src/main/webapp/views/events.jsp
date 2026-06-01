<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventboard.model.Event" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>EventBoard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<main class="container">
    <h1>EventBoard</h1>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <section class="block">
        <h2>Майбутні заходи</h2>

        <table>
            <thead>
            <tr>
                <th>Назва</th>
                <th>Дата</th>
                <th>Усього місць</th>
                <th>Вільно</th>
                <th>Дія</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Event> events = (List<Event>) request.getAttribute("events");
                if (events == null || events.isEmpty()) {
            %>
            <tr>
                <td colspan="5">Поки немає майбутніх заходів.</td>
            </tr>
            <%
                } else {
                    for (Event event : events) {
            %>
            <tr>
                <td><%= event.getTitle() %></td>
                <td><%= event.getEventDate() %></td>
                <td><%= event.getMaxSeats() %></td>
                <td><%= event.getFreeSeats() %></td>
                <td>
                    <a href="${pageContext.request.contextPath}/event?id=<%= event.getId() %>">Відкрити</a>
                </td>
            </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </section>

    <section class="block">
        <h2>Створити захід</h2>

        <form method="post" action="${pageContext.request.contextPath}/events">
            <label>
                Назва
                <input type="text" name="title" required>
            </label>

            <label>
                Дата
                <input type="date" name="event_date" required>
            </label>

            <label>
                Максимальна кількість місць
                <input type="number" name="max_seats" min="1" required>
            </label>

            <button type="submit">Додати</button>
        </form>
    </section>
</main>
</body>
</html>

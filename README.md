ПОЯСНЕННЯ ПРОЄКТУ EVENTBOARD

Це простий Java Web проєкт для реєстрації студентів на заходи.

У проєкті використовується:
- Java
- Maven
- Tomcat 10
- PostgreSQL
- Servlet
- JSP
- JDBC
- JUnit 5

У проєкті НЕ використовується:
- Spring
- Hibernate
- Mockito

Структура папок:

src/main/java/com/eventboard/model
Тут знаходяться прості класи-об'єкти:
- Event
- Participant

src/main/java/com/eventboard/repository
Тут знаходяться класи, які працюють з базою даних через SQL:
- EventRepository
- ParticipantRepository

src/main/java/com/eventboard/service
Тут знаходиться бізнес-логіка:
- EventService

src/main/java/com/eventboard/servlet
Тут знаходяться сервлети, тобто контролери:
- EventsServlet
- EventDetailsServlet

src/main/java/com/eventboard/util
Тут знаходиться клас для підключення до PostgreSQL:
- DatabaseUtil

src/main/webapp/views
Тут знаходяться JSP сторінки:
- events.jsp
- event-details.jsp

src/main/resources/schema.sql
Тут знаходиться SQL для створення таблиць.

src/test/java
Тут знаходиться unit-тест для EventService.

БАЗА ДАНИХ

У проєкті використовується PostgreSQL.

Назва бази даних:

eventboard

Щоб створити базу, у pgAdmin або PostgreSQL Query Tool треба виконати:

CREATE DATABASE eventboard;

Після цього треба підключитися саме до бази eventboard і створити таблиці.

У проєкті є 2 таблиці.

1. events

Ця таблиця зберігає заходи.

Поля:
- id - унікальний номер заходу
- title - назва заходу
- event_date - дата заходу
- max_seats - максимальна кількість місць

2. participants

Ця таблиця зберігає студентів, які зареєструвалися на заходи.

Поля:
- id - унікальний номер учасника
- event_id - id заходу
- student_name - ім'я студента
- student_email - пошта студента

event_id є зовнішнім ключем.
Він зв'язує учасника з конкретним заходом.

SQL знаходиться у файлі:

src/main/resources/schema.sql

Підключення до бази знаходиться у файлі:

src/main/java/com/eventboard/util/DatabaseUtil.java

Там треба змінити пароль:

private static final String PASSWORD = "your_password";

ЯК ПРАЦЮЮТЬ СТОРІНКИ

У проєкті є 2 головні адреси:

1. /events
2. /event?id=...

СТОРІНКА /events

GET /events

Коли користувач відкриває /events:

1. Запускається EventsServlet.
2. Сервлет викликає EventService.
3. EventService бере список майбутніх заходів.
4. Для кожного заходу рахується кількість вільних місць.
5. Дані передаються у events.jsp.
6. JSP показує таблицю заходів.

POST /events

Коли користувач додає новий захід:

1. Форма відправляє title, event_date, max_seats.
2. EventsServlet читає ці дані.
3. Створюється об'єкт Event.
4. EventService перевіряє дані.
5. EventRepository додає захід у базу.
6. Сервер робить redirect назад на /events.

Це називається PRG Pattern:

Post -> Redirect -> Get

Це потрібно, щоб при оновленні сторінки форма не відправлялася ще раз.

СТОРІНКА /event?id=1

GET /event?id=1

Коли користувач відкриває конкретний захід:

1. Запускається EventDetailsServlet.
2. Сервлет читає id заходу.
3. EventService знаходить захід.
4. EventService бере список учасників.
5. Дані передаються у event-details.jsp.
6. JSP показує деталі заходу і форму реєстрації.

POST /event

Коли студент реєструється:

1. Форма відправляє event_id, student_name, student_email.
2. EventDetailsServlet читає ці дані.
3. EventService перевіряє, чи є вільні місця.
4. Якщо місця є, ParticipantRepository додає студента у базу.
5. Сервер робить redirect назад на /event?id=...
6. Якщо місць немає, сторінка показує помилку.


ПОЯСНЕННЯ КЛАСІВ

Event.java

Це модель заходу.
Вона має поля:
- id
- title
- eventDate
- maxSeats
- freeSeats

freeSeats не зберігається в базі.
Воно рахується в Java:

freeSeats = maxSeats - кількість зареєстрованих учасників

Participant.java

Це модель учасника.
Вона має поля:
- id
- eventId
- studentName
- studentEmail

DatabaseUtil.java

Цей клас створює підключення до PostgreSQL.
У ньому є метод:

getConnection()

Його використовують repository-класи.

EventRepository.java

Цей клас працює з таблицею events.

Основні методи:
- findFutureEvents()
- findById(int id)
- save(Event event)
- countParticipantsByEventId(int eventId)

Усі SQL-запити зроблені через PreparedStatement.
Це захищає від SQL Injection.

ParticipantRepository.java

Цей клас працює з таблицею participants.

Основні методи:
- findByEventId(int eventId)
- save(Participant participant)

EventService.java

Це головний клас з бізнес-логікою.

Він:
- створює захід
- отримує список заходів
- отримує учасників
- рахує вільні місця
- не дозволяє реєстрацію, якщо місць немає

EventsServlet.java

Це контролер для сторінки /events.

doGet() показує список заходів.
doPost() додає новий захід.

EventDetailsServlet.java

Це контролер для сторінки /event.

doGet() показує один захід.
doPost() реєструє студента на захід.

events.jsp

Це сторінка зі списком заходів і формою створення заходу.

event-details.jsp

Це сторінка конкретного заходу.
Тут видно учасників і форму реєстрації.


ЯК ЗАПУСТИТИ ПРОЄКТ З НУЛЯ

1. Встановити PostgreSQL.

2. Створити базу даних:

CREATE DATABASE eventboard;

3. Відкрити базу eventboard і виконати SQL з файлу:

src/main/resources/schema.sql

4. Відкрити проєкт в IntelliJ IDEA.

5. Відкрити файл:

src/main/java/com/eventboard/util/DatabaseUtil.java

6. Поставити свій пароль PostgreSQL:

private static final String PASSWORD = "твій пароль";

7. Натиснути Maven Reload.

В IntelliJ справа є вкладка Maven.
Там треба натиснути Reload All Maven Projects.

8. Налаштувати Tomcat 10.

Run -> Edit Configurations -> + -> Tomcat Server -> Local

У вкладці Deployment треба додати:

EventBoard:war exploded

Application context:

/EventBoard

9. Запустити Tomcat.

10. Відкрити у браузері:

http://localhost:8080/EventBoard/events

Якщо відкрилася сторінка EventBoard, значить проєкт працює.

Якщо 404:
- перевірити, чи доданий EventBoard:war exploded
- перевірити Application context
- перевірити, що використовується Tomcat 10

Якщо помилка підключення до бази:
- перевірити, чи PostgreSQL запущений
- перевірити назву бази eventboard
- перевірити логін postgres
- перевірити пароль у DatabaseUtil.java

КОРОТКИЙ ТЕКСТ ДЛЯ ПОЯСНЕННЯ ПРОЄКТУ

Цей проєкт називається EventBoard.
Він зроблений для реєстрації студентів на заходи.

У проєкті є дві таблиці:
events і participants.

Таблиця events зберігає заходи.
Таблиця participants зберігає студентів, які зареєструвалися.

Проєкт зроблений за MVC.

Model - це класи Event і Participant.
View - це JSP сторінки events.jsp і event-details.jsp.
Controller - це сервлети EventsServlet і EventDetailsServlet.

Також є шар Repository.
Він відповідає за SQL-запити до бази даних.

Є шар Service.
Він відповідає за бізнес-логіку.
Наприклад, EventService перевіряє, чи є вільні місця перед реєстрацією студента.

Для захисту від SQL Injection використовуються PreparedStatement.

Після POST-запитів використовується redirect.
Це зроблено для PRG Pattern, щоб форма не відправлялася повторно після оновлення сторінки.

Для тестування є unit-тест EventServiceTest.
Він перевіряє, що студент не може зареєструватися, якщо місць уже немає.

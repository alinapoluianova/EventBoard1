ПОЯСНЕННЯ ПРОЄКТУ EVENTBOARD

!Проєкт робила не зовсім самостійно, а з викорситання ШІ!
!Я розумію, що це ставить мене в інші умови порівняно з учасниками, 
які писали все власноруч, проте я детально розібралася в частково генерованому коді 
та готова відповісти на будь-які питання щодо його роботи!

1. Структура: 

У проєкті використовується:
- Java
- Maven
- Tomcat 10
- PostgreSQL
- Servlet
- JSP
- JDBC
- JUnit 5

Структура папок:

src/main/java/com/eventboard/model - прості класи-об'єкти:
- Event
- Participant

src/main/java/com/eventboard/repository - класи, які працюють з базою даних через SQL:
- EventRepository
- ParticipantRepository

src/main/java/com/eventboard/service - бізнес-логіка:
- EventService

src/main/java/com/eventboard/servlet - контролери:
- EventsServlet
- EventDetailsServlet

src/main/java/com/eventboard/util - клас для підключення до PostgreSQL:
- DatabaseUtil

src/main/webapp/views - JSP сторінки:
- events.jsp
- event-details.jsp

src/test/java - unit-тест для EventService.

2. DataBase:

У проєкті використовується PostgreSQL.

Назва бази даних: eventboard

Щоб створити базу, у pgAdmin (PostgreSQL Query Tool) треба виконати:
CREATE DATABASE eventboard;

Після цього підключаємося до бази eventboard і створити 2 таблиці:

1) events (заходи)
Поля:
- id - унікальний номер заходу
- title - назва заходу
- event_date - дата заходу
- max_seats - максимальна кількість місць

2. participants (учасники)
Поля:
- id - унікальний номер учасника
- event_id - id заходу
- student_name - ім'я студента
- student_email - пошта студента

event_id є зовнішнім ключем.
Він зв'язує учасника з конкретним заходом.

Підключення до бази знаходиться у файлі:
src/main/java/com/eventboard/util/DatabaseUtil.java

3. Як працюють сторінки

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

4. Пояснення класів

Event.java - модель заходу
Поля:
- id
- title
- eventDate
- maxSeats
- freeSeats

freeSeats не зберігається в базі.
Воно рахується в Java:
freeSeats = maxSeats - кількість зареєстрованих учасників

Participant.java - модель учасника
Поля:
- id
- eventId
- studentName
- studentEmail

DatabaseUtil.java - клас підключається до PostgreSQL
У ньому є метод: getConnection(). Його використовують repository-класи.

EventRepository.java - клас працює з таблицею events

Основні методи:
- findFutureEvents()
- findById(int id)
- save(Event event)
- countParticipantsByEventId(int eventId)

Усі SQL-запити зроблені через PreparedStatement.
Це захищає від SQL Injection.

ParticipantRepository.java - клас працює з таблицею participants

Основні методи:
- findByEventId(int eventId)
- save(Participant participant)

EventService.java - головний клас з бізнес-логікою

Він:
- створює захід
- отримує список заходів
- отримує учасників
- рахує вільні місця
- не дозволяє реєстрацію, якщо місць немає

EventsServlet.java - контролер для сторінки /events

doGet() показує список заходів
doPost() додає новий захід

EventDetailsServlet.java - контролер для сторінки /event

doGet() показує один захід
doPost() реєструє студента на захід

events.jsp - сторінка зі списком заходів і формою створення заходу

event-details.jsp - сторінка конкретного заходу (тут учасники і форма реєстрації)

5. unit-тест EventServiceTest

Замість реальної бази даних створюються два "муляжі" 
(FakeEventRepository та FakeParticipantRepository). Вони потрібні, щоб тест 
працював швидко і не чіпав справжню базу.

Створюється подія "Java Meetup", де максимум може бути 2 місця.
Програмі кажуть, що на неї вже записано 2 учасники. Тобто вільних місць — нуль.

Тест викликає метод registerParticipant і намагається записати туди ще одного студента (Івана).

Перевірка результату (assertThrows):
Тест очікує, що програма видасть помилку (IllegalStateException).
Якщо помилка вилетіла — сервіс спрацював правильно і заблокував зайвого студента.

Фінальна перевірка (assertFalse):
Тест перевіряє фейкову базу для учасників. Прапорець participantSaved має бути 
false (це означає, що Івана в базу дійсно НЕ записало).
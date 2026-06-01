package com.eventboard.model;

public class Participant {
    private int id;
    private int eventId;
    private String studentName;
    private String studentEmail;

    public Participant() {
    }

    public Participant(int id, int eventId, String studentName, String studentEmail) {
        this.id = id;
        this.eventId = eventId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
    }

    public Participant(int eventId, String studentName, String studentEmail) {
        this.eventId = eventId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }
}

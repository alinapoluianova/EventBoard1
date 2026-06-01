package com.eventboard.model;

import java.time.LocalDate;

public class Event {
    private int id;
    private String title;
    private LocalDate eventDate;
    private int maxSeats;
    private int freeSeats;

    public Event() {
    }

    public Event(int id, String title, LocalDate eventDate, int maxSeats) {
        this.id = id;
        this.title = title;
        this.eventDate = eventDate;
        this.maxSeats = maxSeats;
    }

    public Event(String title, LocalDate eventDate, int maxSeats) {
        this.title = title;
        this.eventDate = eventDate;
        this.maxSeats = maxSeats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }

    public int getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(int freeSeats) {
        this.freeSeats = freeSeats;
    }
}

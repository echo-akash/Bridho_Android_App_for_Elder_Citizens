package com.example.thinkpad.bridho;

public class Appointment {
    private String name;
    private String address;
    private String date;
    private String time;

    public Appointment(String name, String address, String date, String time) {
        //this.id = id;
        this.name = name;
        this.address = address;
        this.date = date;
        this.time = time;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return name + " - " + address + " - " + date + "-" + time;
    }
}

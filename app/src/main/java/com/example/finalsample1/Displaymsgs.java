package com.example.finalsample1;

public class Displaymsgs {

    private String message;
    private boolean seen;
    private long time;
    private String type;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    private String from;

    public Displaymsgs(String message, boolean seen, long time, String type, String from) {
        this.message = message;
        this.seen = seen;
        this.time = time;
        this.type = type;
        this.from = from;

    }

    public Displaymsgs() {

    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSeen() {
        return seen;
    }
}

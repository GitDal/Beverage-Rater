package dk.au.mad21spring.appproject.group6.models;

public enum Status {
    DRAFT(0),
    PENDING(1),
    DECLINED(2),
    APPROVED(3);

    private int id;
    private Status(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Status fromId(int id) {
        for(Status type : values()) {
            if(type.id == id) {
                return type;
            }
        }
        return null;
    }
}



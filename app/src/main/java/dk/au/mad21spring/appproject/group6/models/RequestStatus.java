package dk.au.mad21spring.appproject.group6.models;

public enum RequestStatus {
    DRAFT(1),
    PENDING(2),
    DECLINED(3),
    APPROVED(4);

    private int id;
    private RequestStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static RequestStatus fromId(int id) {
        for(RequestStatus type : values()) {
            if(type.id == id) {
                return type;
            }
        }
        return null;
    }
}



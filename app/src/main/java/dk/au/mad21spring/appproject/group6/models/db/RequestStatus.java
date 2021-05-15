package dk.au.mad21spring.appproject.group6.models.db;

import dk.au.mad21spring.appproject.group6.R;

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

    public int resolveIconResId () {
        switch (this){
            case DRAFT:
                return R.drawable.ic_draft_letter;
            case PENDING:
                return R.drawable.ic_mail;
            case DECLINED:
                return R.drawable.ic_cross_circle_filled;
            case APPROVED:
                return R.drawable.ic_check_circle;
            default:
                return -1;
        }
    }
}



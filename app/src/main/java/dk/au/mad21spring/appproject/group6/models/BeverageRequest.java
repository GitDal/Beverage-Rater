package dk.au.mad21spring.appproject.group6.models;

public class BeverageRequest {
    public String Id;
    public String UserId;
    public String BeverageId;
    public Status Status;
    public String EanNumber;

    public BeverageRequest(String id, String userId, String beverageId, int status, String eanNumber){
        Id = id;
        UserId = userId;
        BeverageId = beverageId;
        Status = Status.fromId(status);
        EanNumber = eanNumber;
    }

}

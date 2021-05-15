package dk.au.mad21spring.appproject.group6.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Beverage {

    public String Id;
    public String Name;
    public String CompanyName;
    public String BeverageInfo;
    public Double GlobalRating;
    public Map<String, UserRating> UserRatings;
    public String ImageUrl;
    public List<String> EanNumbers;
    public RequestStatus Status;
    public String RequestedByUserId;

    public Beverage(){}

    public Beverage(String name, String companyName, String beverageInfo, String imageUrl, int statusCode, String requestedByUserId) {
        Id = UUID.randomUUID().toString();
        Name = name;
        CompanyName = companyName;
        BeverageInfo = beverageInfo;
        ImageUrl = imageUrl;

        GlobalRating = 0.0;
        UserRatings = new HashMap<>();
        EanNumbers = new ArrayList();

        Status = RequestStatus.fromId(statusCode);
        RequestedByUserId = requestedByUserId;
    }

    @Override
    public String toString() {
        return "Beverage{" + "Id='" + Id + '\'' + ", Name='" + Name + '\'' + '}';
    }
}

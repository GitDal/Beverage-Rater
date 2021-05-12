package dk.au.mad21spring.appproject.group6.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Beverage {

    public String Name;
    public String CompanyName;
    public String BeverageInfo;
    public Double GlobalRating;
    public List<UserRating> UserRatings;
    public String ImageUrl;
    public List<String> EanNumbers;
    public RequestStatus Status;
    public String RequestedByUserId;

    public Beverage(){}

    public Beverage(String name, String companyName, String beverageInfo, String eanNumber, String imageUrl, int statusCode, String requestedByUserId) {
        Name = name;
        CompanyName = companyName;
        BeverageInfo = beverageInfo;
        ImageUrl = imageUrl;

        GlobalRating = 0.0;
        UserRatings = new ArrayList();
        EanNumbers = new ArrayList();
        EanNumbers.add(eanNumber);

        Status = RequestStatus.fromId(statusCode);
        RequestedByUserId = requestedByUserId;
    }
}

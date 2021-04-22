package dk.au.mad21spring.appproject.group6.models;

import java.util.ArrayList;
import java.util.List;

public class Beverage {

    public String Id;
    public String Name;
    public String CompanyName;
    public String BeverageInfo;
    public Number GlobalRating;
    public List<UserRating> UserRatings;
    public String ImageUrl;
    public List<String> EanNumbers;

    public Beverage(String id, String name, String companyName, String beverageInfo, String imageUrl) {
        Id = id;
        Name = name;
        CompanyName = companyName;
        BeverageInfo = beverageInfo;
        ImageUrl = imageUrl;

        GlobalRating = 0.0;
        UserRatings = new ArrayList<>();
        EanNumbers = new ArrayList<>();
    }
}

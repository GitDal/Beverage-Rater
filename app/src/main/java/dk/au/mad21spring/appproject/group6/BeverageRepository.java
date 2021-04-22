package dk.au.mad21spring.appproject.group6;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.BeverageRequest;

public class BeverageRepository {

    private static BeverageRepository instance;

    public static BeverageRepository getBeverageRepository(final Context context) {
        if(instance == null) {
            instance = new BeverageRepository(context);
        }
        return instance;
    }

    private BeverageRepository(Context context) {
        //TODO: link med firebase
    }

    public List<BeverageRequest> getRequestsForUser(String userId){
        ArrayList<BeverageRequest> requests = new ArrayList<>();

        List<Beverage> beverages = getDummyBeverages();
        requests.add(new BeverageRequest("1", userId, beverages.get(0).Id, 0, "12345"));

        return requests;
    }

    public List<Beverage> getAllBeverages(){
        return getDummyBeverages();
    }

    private List<Beverage> getDummyBeverages() {
        ArrayList<Beverage> beverages = new ArrayList<>();

        String dummyImgUrl = "https://s3-eu-west-2.amazonaws.com/newzimlive/wp-content/uploads/2019/01/09152727/Fizzy-Drinks.jpg";

        Beverage beverage1 = new Beverage("1", "Pepsi Max", "Pepsi Company", "This beverage was first produced in 1946", dummyImgUrl);
        Beverage beverage2 = new Beverage("2", "Coca Cola", "Cola Company", "This beverage is tha bomb", dummyImgUrl);
        Beverage beverage3 = new Beverage("3", "Fanta", "Fanta Company", "Orange", dummyImgUrl);
        Beverage beverage4 = new Beverage("4", "Sprite", "Sprite Company", "The sugar content in this drink doesn't cleanse thirst", dummyImgUrl);
        Beverage beverage5 = new Beverage("5", "Mountain Dew", "Mountain Dew Broo", "Gamur Juice", dummyImgUrl);
        Beverage beverage6 = new Beverage("6", "Dansk Vand", "SodaStream", "Det sunde valg", dummyImgUrl);

        // Too lazy to change constructor
        beverage1.GlobalRating = 10.0;
        beverage2.GlobalRating = 9.0;
        beverage3.GlobalRating = 8.7;
        beverage4.GlobalRating = 9.2;
        beverage5.GlobalRating = 9.0;
        beverage6.GlobalRating = 2.0;

        beverages.add(beverage1);
        beverages.add(beverage2);
        beverages.add(beverage3);
        beverages.add(beverage4);
        beverages.add(beverage5);
        beverages.add(beverage6);

        return beverages;
    }


}

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

    private List<BeverageRequest> getRequestsForUser(String userId){
        ArrayList<BeverageRequest> requests = new ArrayList<>();

        List<Beverage> beverages = getDummyBeverages();
        requests.add(new BeverageRequest("1", userId, beverages.get(0).Id, 0, "12345"));

        return requests;
    }

    private List<Beverage> getDummyBeverages() {
        ArrayList<Beverage> beverages = new ArrayList<>();

        Beverage beverage1 = new Beverage("1", "Pepsi Max", "Pepsi Company", "This beverage was first produced in 1946", "");
        beverages.add(beverage1);

        return beverages;
    }


}

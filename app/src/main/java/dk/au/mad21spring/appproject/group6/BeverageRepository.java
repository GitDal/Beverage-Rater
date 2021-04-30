package dk.au.mad21spring.appproject.group6;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.RequestStatus;

public class BeverageRepository {

    private static BeverageRepository instance;
    private List<Beverage> dummyBeverages;

    public static BeverageRepository getBeverageRepository(final Context context) {
        if(instance == null) {
            instance = new BeverageRepository(context);
        }
        return instance;
    }

    private BeverageRepository(Context context) {
        //TODO: link med firebase

        //Load dummy data (Så det kun skal genereres en gang)
        dummyBeverages = getDummyBeverages();
    }

    public Beverage getBeverageRequest(String beverageId) {
        for(Beverage beverage : dummyBeverages) {
            if(beverage.Id == beverageId) {
                return beverage;
            }
        }

        return null;
    }

    public List<Beverage> getBeverageRequestsForUser(String userId){
        List<Beverage> userBeverageRequests = new ArrayList<>();

        for(Beverage beverage : dummyBeverages) {
            if(beverage.RequestedByUserId == userId) {
                userBeverageRequests.add(beverage);
            }
        }

        return userBeverageRequests;
    }

    public List<Beverage> getAllBeverages(){
        return dummyBeverages;
    }

    private List<Beverage> getDummyBeverages() {
        ArrayList<Beverage> beverages = new ArrayList<>();

        // Request-statuses (integer values)
        int draftStatusCode = RequestStatus.DRAFT.getId();
        int pendingStatusCode = RequestStatus.PENDING.getId();
        int declinedStatusCode = RequestStatus.DECLINED.getId();
        int approvedStatusCode = RequestStatus.APPROVED.getId();

        String dummyImgUrl = "https://s3-eu-west-2.amazonaws.com/newzimlive/wp-content/uploads/2019/01/09152727/Fizzy-Drinks.jpg";

        // Requested by user with id = "1"
        Beverage beverage1 = new Beverage("1", "Pepsi Max", "Pepsi Company", "This beverage was first produced in 1946", dummyImgUrl, approvedStatusCode, "1");
        Beverage beverage2 = new Beverage("2", "Coca Cola", "Cola Company", "This beverage is tha bomb", dummyImgUrl, approvedStatusCode, "1");
        Beverage beverage3 = new Beverage("3", "Fanta", "Fanta Company", "Orange", dummyImgUrl, approvedStatusCode, "1");

        // Requested by user with id = "2"
        Beverage beverage4 = new Beverage("4", "Sprite", "Sprite Company", "The sugar content in this drink doesn't cleanse thirst", dummyImgUrl, approvedStatusCode, "2");
        Beverage beverage5 = new Beverage("5", "Mountain Dew", "Mountain Dew Broo", "Gamur Juice", dummyImgUrl, approvedStatusCode, "2");
        Beverage beverage6 = new Beverage("6", "Dansk Vand", "SodaStream", "Det sunde valg", dummyImgUrl, approvedStatusCode, "2");

        // Requested by user with id = "3" (til test af requests)
        Beverage beverage7 = new Beverage("7", "Blå sodavand", "Mikes basement TM", "Its the best there is!", dummyImgUrl, draftStatusCode, "3");
        Beverage beverage8 = new Beverage("8", "Monster", "Monster Company", "Meget hyper energidrik", dummyImgUrl, pendingStatusCode, "3");
        Beverage beverage9 = new Beverage("9", "HahaLol", "Lolern Company", "Dette er et troll request", dummyImgUrl, declinedStatusCode, "3");
        Beverage beverage10 = new Beverage("10", "Coca Cola Zero", "Cola Company", "Very nice ;)", dummyImgUrl, approvedStatusCode, "3");

        // Too lazy to change constructor (Only set globalRating for beverages that are approved)
        beverage1.GlobalRating = 10.0;
        beverage2.GlobalRating = 9.0;
        beverage3.GlobalRating = 8.7;
        beverage4.GlobalRating = 9.2;
        beverage5.GlobalRating = 9.0;
        beverage6.GlobalRating = 2.0;
        beverage10.GlobalRating = 7.2;

        beverages.add(beverage1);
        beverages.add(beverage2);
        beverages.add(beverage3);
        beverages.add(beverage4);
        beverages.add(beverage5);
        beverages.add(beverage6);

        beverages.add(beverage7);
        beverages.add(beverage8);
        beverages.add(beverage9);
        beverages.add(beverage10);

        return beverages;
    }


}

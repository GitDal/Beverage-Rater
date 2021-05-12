package dk.au.mad21spring.appproject.group6;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.CurrentUser;
import dk.au.mad21spring.appproject.group6.models.RequestStatus;

public class BeverageRepository {

    private static final String TAG = "BeverageRepository";
    private static BeverageRepository instance;
    DatabaseReference beverageDb;
    private static List<Beverage> dummyBeverages;

    public static BeverageRepository getBeverageRepository(final Context context) {
        if(instance == null) {
            Log.d(TAG, "getBeverageRepository: Initializing new instance");
            instance = new BeverageRepository(context);
        }
        return instance;
    }

    public FirebaseAuth auth;
    public CurrentUser currentUser;

    public boolean CurrentUserIsAdmin = false;

    private BeverageRepository(Context context) {
        //TODO: link med firebase
        beverageDb = FirebaseDatabase.getInstance().getReference("beverages");

        //Load dummy data (Så det kun skal genereres en gang)
        dummyBeverages = getDummyBeverages();
    }

    public void addBeverage(Beverage beverage){
        beverageDb.child(beverage.Name).setValue(beverage);
    }

    public void getAllBeverages(MutableLiveData<List<Beverage>> beverages){
        ValueEventListener beveragesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Beverage> beverageList = new ArrayList<Beverage>();
                for(DataSnapshot beverageSnapshot : snapshot.getChildren()){
                    beverageList.add(beverageSnapshot.getValue(Beverage.class));
                }
                beverages.setValue(beverageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        beverageDb.addValueEventListener(beveragesListener);
    }

    public void getAllApprovedBeverages(MutableLiveData<List<Beverage>> beverages){
        ValueEventListener beveragesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Beverage> beverageList = new ArrayList<Beverage>();
                for(DataSnapshot beverageSnapshot : snapshot.getChildren()){
                    Beverage beverage = beverageSnapshot.getValue(Beverage.class);
                    if(beverage.Status == RequestStatus.APPROVED){
                        beverageList.add(beverage);
                    }
                }
                beverages.setValue(beverageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        beverageDb.addValueEventListener(beveragesListener);
    }

    // Dummy

    public Beverage getBeverageRequest(String beverageName) {
        for(Beverage beverage : dummyBeverages) {
            if(beverage.Id == beverageId) {
                return beverage;
            }
        }

        return null;
    }

    public List<Beverage> getBeverageRequestsForUser(String userEmail){
        List<Beverage> userBeverageRequests = new ArrayList<>();

        for(Beverage beverage : dummyBeverages) {
            if(beverage.RequestedByUserId.equals(userEmail)) {
                userBeverageRequests.add(beverage);
            }
        }

        return userBeverageRequests;
    }

    public List<Beverage> getBeverageRequestsForModerator() {
        List<Beverage> pendingBeverageRequests = new ArrayList<>();

        for(Beverage beverage : dummyBeverages) {
            if(beverage.Status == RequestStatus.PENDING) {
                pendingBeverageRequests.add(beverage);
            }
        }

        return pendingBeverageRequests;
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

        // Requested by moderator
        String adminId = "admin@gmail.com";
        Beverage beverage1 = new Beverage("1", "Pepsi Max", "Pepsi Company", "This beverage was first produced in 1946", dummyImgUrl, approvedStatusCode, adminId);
        Beverage beverage2 = new Beverage("2", "Coca Cola", "Cola Company", "This beverage is tha bomb", dummyImgUrl, approvedStatusCode, adminId);
        Beverage beverage3 = new Beverage("3", "Fanta", "Fanta Company", "Orange", dummyImgUrl, approvedStatusCode, adminId);
        Beverage beverage4 = new Beverage("4", "Sprite", "Sprite Company", "The sugar content in this drink doesn't cleanse thirst", dummyImgUrl, approvedStatusCode, adminId);
        Beverage beverage5 = new Beverage("5", "Mountain Dew", "Mountain Dew Broo", "Gamur Juice", dummyImgUrl, approvedStatusCode, adminId);
        Beverage beverage6 = new Beverage("6", "Dansk Vand", "SodaStream", "Det sunde valg", dummyImgUrl, approvedStatusCode, adminId);

        // Requested by user (til test af requests)
        String userId = "user@gmail.com";
        Beverage beverage7 = new Beverage("7", "Blå sodavand", "Mikes basement TM", "Its the best there is!", dummyImgUrl, draftStatusCode, userId);
        Beverage beverage8 = new Beverage("8", "Monster", "Monster Company", "Meget hyper energidrik", dummyImgUrl, pendingStatusCode, userId);
        Beverage beverage9 = new Beverage("9", "Vand drink", "Havet", "Det er lækkert...", dummyImgUrl, pendingStatusCode, userId);
        Beverage beverage10 = new Beverage("10", "HahaLol", "Lolern Company", "Dette er et troll request", dummyImgUrl, declinedStatusCode, userId);
        Beverage beverage11 = new Beverage("11", "Coca Cola Zero", "Cola Company", "Very nice ;)", dummyImgUrl, approvedStatusCode, userId);

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
        beverages.add(beverage11);

        return beverages;
    }


    public void save(Beverage beverageToSave) {
        int index = 0;

        for(Beverage beverage : dummyBeverages) {
            if(beverage.Name == beverageToSave.Name) {
                dummyBeverages.set(index, beverageToSave);
            }
            index++;
        }
    }
}

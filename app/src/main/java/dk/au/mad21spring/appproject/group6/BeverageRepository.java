package dk.au.mad21spring.appproject.group6;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.CurrentUser;
import dk.au.mad21spring.appproject.group6.models.RequestStatus;

public class BeverageRepository {

    private static final String TAG = "BeverageRepository";
    private static BeverageRepository instance;
    DatabaseReference beverageDb;
    private static String dummyImgUrl = "https://s3-eu-west-2.amazonaws.com/newzimlive/wp-content/uploads/2019/01/09152727/Fizzy-Drinks.jpg";

    public static BeverageRepository getBeverageRepository(final Context context) {
        if(instance == null) {
            Log.d(TAG, "getBeverageRepository: Initializing new instance");
            instance = new BeverageRepository(context);
        }
        return instance;
    }

    public FirebaseAuth auth;
    public CurrentUser currentUser;

    private BeverageRepository(Context context) {
        beverageDb = FirebaseDatabase.getInstance().getReference("beverages");
    }

    public void ResolveUser() {
        Log.d(TAG, "ResolveUser: resolving user ...");

        if(auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        if(currentUser == null) {
            currentUser = new CurrentUser();
        }

        currentUser.Email = auth.getCurrentUser().getEmail();

        Log.d(TAG, "ResolveUser: Fetching admin-claim ...");

        auth.getCurrentUser().getIdToken(false).addOnSuccessListener(result -> {
            currentUser.IsAdmin = result.getClaims().containsKey("admin");
            Log.d(TAG, "ResolveUser: user resolved: " + currentUser.toString());
        });
    }

    /* Queries */

    public void getBeverage(String beverageId, MutableLiveData<Beverage> beverage) {
        ValueEventListener beveragesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                beverage.setValue(snapshot.getValue(Beverage.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        beverageDb.child(beverageId).addValueEventListener(beveragesListener);
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

    public void getAllPendingBeverages(MutableLiveData<List<Beverage>> beverages) {
        ValueEventListener beveragesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Beverage> beverageList = new ArrayList<Beverage>();
                for(DataSnapshot beverageSnapshot : snapshot.getChildren()){
                    Beverage beverage = beverageSnapshot.getValue(Beverage.class);
                    if(beverage.Status == RequestStatus.PENDING){
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

    public void getAllBeveragesRequestedByUser(MutableLiveData<List<Beverage>> beverages) {
        ValueEventListener beveragesListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Beverage> beverageList = new ArrayList<Beverage>();
                for(DataSnapshot beverageSnapshot : snapshot.getChildren()){
                    Beverage beverage = beverageSnapshot.getValue(Beverage.class);
                    if(beverage.RequestedByUserId.equals(currentUser.Email)){
                        beverageList.add(beverage);
                    }
                }
                beverageList.sort((b1, b2) -> b1.Status.getId() - b2.Status.getId());
                beverages.setValue(beverageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        beverageDb.addValueEventListener(beveragesListener);
    }

    /* Mutations */

    public void addBeverage(Beverage beverage){
        beverageDb.child(beverage.Id).setValue(beverage);
    }

    public void updateBeverage(Beverage beverage) { beverageDb.child(beverage.Id).setValue(beverage); }

    public void deleteBeverage(String beverageId) {
        beverageDb.child(beverageId).removeValue();
    }
}

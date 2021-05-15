package dk.au.mad21spring.appproject.group6;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad21spring.appproject.group6.constants.BeverageDbFieldNames;
import dk.au.mad21spring.appproject.group6.constants.GoogleSearchApi;
import dk.au.mad21spring.appproject.group6.models.db.Beverage;
import dk.au.mad21spring.appproject.group6.models.CurrentUser;
import dk.au.mad21spring.appproject.group6.models.db.RequestStatus;
import dk.au.mad21spring.appproject.group6.models.db.UserRating;
import dk.au.mad21spring.appproject.group6.models.api.GoogleSearchResponse;
import dk.au.mad21spring.appproject.group6.models.api.Image;
import dk.au.mad21spring.appproject.group6.models.api.Item;

public class BeverageRepository {

    private static final String TAG = "BeverageRepository";
    private static BeverageRepository instance;
    private DatabaseReference beverageDb;
    private RequestQueue queue;
    private ExecutorService execService;

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
        queue = Volley.newRequestQueue(context.getApplicationContext());
        beverageDb = FirebaseDatabase.getInstance().getReference("beverages");
        execService = Executors.newSingleThreadExecutor();
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Beverage> beverageList = getBeveragesFromSnapshot(snapshot);
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
                List<Beverage> beverageList = getBeveragesFromSnapshot(snapshot);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    beverageList.sort((o1, o2) -> o2.GlobalRating.compareTo(o1.GlobalRating));
                }
                beverages.setValue(beverageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        beverageDb.orderByChild(BeverageDbFieldNames.Status).equalTo(RequestStatus.APPROVED.toString()).addValueEventListener(beveragesListener);
    }

    public void getAllPendingBeverages(MutableLiveData<List<Beverage>> beverages) {
        ValueEventListener beveragesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Beverage> beverageList = getBeveragesFromSnapshot(snapshot);
                beverages.setValue(beverageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        beverageDb.orderByChild(BeverageDbFieldNames.Status).equalTo(RequestStatus.PENDING.toString()).addValueEventListener(beveragesListener);
    }

    public void getAllPendingBeveragesRequestedByUser(MutableLiveData<List<Beverage>> beverages) {
        ValueEventListener beveragesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Beverage> beverageList = getBeveragesFromSnapshotRequestedByCurrentUser(snapshot);
                beverages.setValue(beverageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        beverageDb.orderByChild(BeverageDbFieldNames.Status).equalTo(RequestStatus.PENDING.toString()).addValueEventListener(beveragesListener);
    }

    public void getAllBeveragesRequestedByUser(MutableLiveData<List<Beverage>> beverages) {
        ValueEventListener beveragesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Beverage> beverageList = getBeveragesFromSnapshotRequestedByCurrentUser(snapshot);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    beverageList.sort((b1, b2) -> b1.Status.getId() - b2.Status.getId());
                }
                beverages.setValue(beverageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        beverageDb.addValueEventListener(beveragesListener);
    }

    private List<Beverage> getBeveragesFromSnapshot(@NonNull DataSnapshot snapshot) {
        List<Beverage> beverageList = new ArrayList<Beverage>();

        for(DataSnapshot beverageSnapshot : snapshot.getChildren()){
            Beverage beverage = beverageSnapshot.getValue(Beverage.class);
            beverageList.add(beverage);
        }

        return beverageList;
    }

    private List<Beverage> getBeveragesFromSnapshotRequestedByCurrentUser(@NonNull DataSnapshot snapshot) {
        List<Beverage> beverageList = new ArrayList<Beverage>();

        for(DataSnapshot beverageSnapshot : snapshot.getChildren()){
            Beverage beverage = beverageSnapshot.getValue(Beverage.class);
            if(beverage.RequestedByUserId.equals(currentUser.Email)) {
                beverageList.add(beverage);
            }
        }

        return beverageList;
    }

    /* Mutations */

    public void addBeverage(Beverage beverage){
        beverageDb.child(beverage.Id).setValue(beverage);
    }

    public void updateBeverage(Beverage beverage) { beverageDb.child(beverage.Id).setValue(beverage); }

    public void updateBeverageRating(Beverage beverage, String ratingId, double globalRating, int userRating){
        UserRating ur = new UserRating();
        ur.userId = currentUser.Email;
        ur.rating = userRating;

        beverageDb.child(beverage.Id).child("GlobalRating").setValue(globalRating);
        if(!ratingId.isEmpty()){
            beverageDb.child(beverage.Id).child("UserRatings").child(ratingId).setValue(ur);
        } else{
            String key = beverageDb.child(beverage.Id).child("UserRatings").push().getKey();
            Map map = new HashMap<>();
            map.put(key, ur);
            beverageDb.child(beverage.Id).child("UserRatings").updateChildren(map);
        }
    }

    public void deleteBeverage(String beverageId) {
        beverageDb.child(beverageId).removeValue();
    }

    /* API */

    public void updateImageUrlForProductAsync(Beverage beverage, String productName) {
        execService.submit(() -> {
            String requestUrl = GoogleSearchApi.BaseUrl + "key=" + GoogleSearchApi.ApiKey + "&cx=" + GoogleSearchApi.ImagesGoogleEngineId + "&searchType=image" + "&q=" + productName;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                    response -> {
                        if(!response.isEmpty()) {

                            Log.d(TAG, "getImageUrlForProduct: Got Response:");
                            Gson gson = new GsonBuilder().create();
                            GoogleSearchResponse imageResponse = gson.fromJson(response, GoogleSearchResponse.class);

                            List<Item> results = imageResponse.getItems();
                            if(results == null) {
                                Log.d(TAG, "updateImageUrlForProduct: No image-results found for beverage: " + (beverage != null ? beverage.toString() : productName));
                                return;
                            }

                            ListIterator<Item> li = results.listIterator();

                            while(li.hasNext()) {
                                Item imageItem = li.next();
                                Image image =  imageItem.getImage();

                                if(image != null) {
                                    String thumbnailUrl = image.getThumbnailLink();

                                    if(!thumbnailUrl.isEmpty()) {
                                        Log.d(TAG, "getImageUrlForProduct: New image-url found for beverage: " + beverage.toString());
                                        beverage.ImageUrl = thumbnailUrl;
                                        updateBeverage(beverage);
                                        return;
                                    }
                                }
                            }

                        } else {
                            Log.d(TAG, "getImageUrlForProduct: Response was empty");
                        }
                    }, error -> {
                Log.d(TAG, "getImageUrlForProduct: An error occurred while fetching data: " +  error.toString());
            });

            //Add request to Volley-queue
            queue.add(stringRequest);
        });
    }
}

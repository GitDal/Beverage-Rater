package dk.au.mad21spring.appproject.group6.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.models.Beverage;

public class RequestDetailsViewModel extends AndroidViewModel {

    private BeverageRepository beverageRepository;
    private Beverage beverageRequest;
    private String requestId;

    public RequestDetailsViewModel(@NonNull Application application) {
        super(application);
        beverageRepository = BeverageRepository.getBeverageRepository(application);
    }

    public void SetRequestId(String id) { requestId = id; }

    public Beverage GetRequest() {

        List<Beverage> beverages = beverageRepository.getAllBeverages();

        for(Beverage b : beverages) {
            if(b.Id == requestId) {
                return b;
            }
        }
        return null;
    }
}

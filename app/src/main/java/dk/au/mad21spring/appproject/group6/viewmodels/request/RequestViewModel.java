package dk.au.mad21spring.appproject.group6.viewmodels.request;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.models.Beverage;

public class RequestViewModel extends AndroidViewModel {

    private BeverageRepository beverageRepository;
    private List<Beverage> beverageRequests;

    public RequestViewModel(@NonNull Application application) {
        super(application);
        beverageRepository = BeverageRepository.getBeverageRepository(application);
        beverageRequests = beverageRepository.getBeverageRequestsForUser("3");
    }

    public List<Beverage> GetRequests() {
        return beverageRequests;
    }

}

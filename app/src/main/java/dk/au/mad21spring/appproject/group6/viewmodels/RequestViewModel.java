package dk.au.mad21spring.appproject.group6.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.models.BeverageRequest;

public class RequestViewModel extends AndroidViewModel {

    private BeverageRepository beverageRepository;
    private List<BeverageRequest> beverageRequests;

    public RequestViewModel(@NonNull Application application) {
        super(application);
        beverageRepository = BeverageRepository.getBeverageRepository(application);
        beverageRequests = beverageRepository.getRequestsForUser("1");
    }

    public List<BeverageRequest> GetRequests() {
        return beverageRequests;
    }

}

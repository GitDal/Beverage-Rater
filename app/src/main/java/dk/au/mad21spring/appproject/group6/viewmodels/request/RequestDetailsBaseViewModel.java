package dk.au.mad21spring.appproject.group6.viewmodels.request;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.models.Beverage;

public class RequestDetailsBaseViewModel extends AndroidViewModel {

    protected BeverageRepository beverageRepository;
    protected Beverage beverageRequest;

    public RequestDetailsBaseViewModel(@NonNull Application application) {
        super(application);
        beverageRepository = BeverageRepository.getBeverageRepository(application);
    }

    public void SetRequestWithId(String id) {
        beverageRequest = beverageRepository.getBeverageRequest(id);
    }

    public Beverage GetRequest() {
        return beverageRequest;
    }
}

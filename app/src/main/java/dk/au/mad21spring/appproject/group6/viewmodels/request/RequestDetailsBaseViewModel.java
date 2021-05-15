package dk.au.mad21spring.appproject.group6.viewmodels.request;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.models.db.Beverage;

public class RequestDetailsBaseViewModel extends AndroidViewModel {

    protected BeverageRepository beverageRepository;
    protected MutableLiveData<Beverage> beverageRequest;
    protected String beverageRequestId;

    public RequestDetailsBaseViewModel(@NonNull Application application) {
        super(application);
        beverageRepository = BeverageRepository.getBeverageRepository(application);
        beverageRequest = new MutableLiveData<>();
    }

    public void setRequestWithId(String id) {
        beverageRepository.getBeverage(id, beverageRequest);
        beverageRequestId = id;
    }

    public LiveData<Beverage> getRequest() {
        return beverageRequest;
    }

    public String getRequestId() {
        return beverageRequestId;
    }
}

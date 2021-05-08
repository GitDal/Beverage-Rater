package dk.au.mad21spring.appproject.group6.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.RequestStatus;

public class RequestDetailsViewModel extends AndroidViewModel {

    private BeverageRepository beverageRepository;
    private Beverage beverageRequest;

    public RequestDetailsViewModel(@NonNull Application application) {
        super(application);
        beverageRepository = BeverageRepository.getBeverageRepository(application);
    }

    public void SetRequestWithId(String id) {
        beverageRequest = beverageRepository.getBeverageRequest(id);
    }

    public Beverage GetRequest() {
        return beverageRequest;
    }

    // TODO: Remember to update beverageRequest in vm (or make logic so that its always the newest we retrieve)
    public void SaveRequest(Beverage beverageRequest) {
        if(beverageRequest.Status != RequestStatus.DRAFT) {
            return;
        }
        beverageRepository.save(beverageRequest);
    }

    public void SendRequest(Beverage beverageRequest) {
        if(beverageRequest.Status != RequestStatus.DRAFT) {
            return;
        }
        beverageRequest.Status = RequestStatus.PENDING;
        beverageRepository.save(beverageRequest);
    }
}

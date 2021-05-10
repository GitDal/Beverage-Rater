package dk.au.mad21spring.appproject.group6.viewmodels.request;

import android.app.Application;

import androidx.annotation.NonNull;

import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.RequestStatus;

public class RequestDetailsActionsUserViewModel extends RequestDetailsBaseViewModel {

    public RequestDetailsActionsUserViewModel(@NonNull Application application) {
        super(application);
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

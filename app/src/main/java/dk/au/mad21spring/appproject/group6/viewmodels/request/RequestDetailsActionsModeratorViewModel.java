package dk.au.mad21spring.appproject.group6.viewmodels.request;

import android.app.Application;

import androidx.annotation.NonNull;

import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.RequestStatus;

public class RequestDetailsActionsModeratorViewModel extends RequestDetailsBaseViewModel {

    public RequestDetailsActionsModeratorViewModel(@NonNull Application application) {
        super(application);
    }

    public void approveRequest(Beverage beverageRequest) {
        beverageRequest.Status = RequestStatus.APPROVED;
        beverageRepository.save(beverageRequest);
    }

    public void declineRequest(Beverage beverageRequest) {
        beverageRequest.Status = RequestStatus.DECLINED;
        beverageRepository.save(beverageRequest);
    }
}

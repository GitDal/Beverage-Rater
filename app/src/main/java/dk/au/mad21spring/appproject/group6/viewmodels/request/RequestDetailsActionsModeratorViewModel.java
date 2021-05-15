package dk.au.mad21spring.appproject.group6.viewmodels.request;

import android.app.Application;

import androidx.annotation.NonNull;

import dk.au.mad21spring.appproject.group6.models.db.Beverage;
import dk.au.mad21spring.appproject.group6.models.db.RequestStatus;

public class RequestDetailsActionsModeratorViewModel extends RequestDetailsBaseViewModel {

    public RequestDetailsActionsModeratorViewModel(@NonNull Application application) {
        super(application);
    }

    public void approveRequest(Beverage beverageRequest) {
        beverageRequest.Status = RequestStatus.APPROVED;
        beverageRepository.updateBeverage(beverageRequest);
    }

    public void declineRequest(Beverage beverageRequest) {
        beverageRequest.Status = RequestStatus.DECLINED;
        beverageRepository.updateBeverage(beverageRequest);
    }
}

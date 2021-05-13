package dk.au.mad21spring.appproject.group6.viewmodels.request;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.RequestStatus;

public class RequestDetailsActionsUserViewModel extends RequestDetailsBaseViewModel {

    private static final String TAG = "ReqDetailsActionsUserVM";

    public RequestDetailsActionsUserViewModel(@NonNull Application application) {
        super(application);
    }

    // TODO: Remember to update beverageRequest in vm (or make logic so that its always the newest we retrieve)
    public void SaveRequest(Beverage beverageRequest) {
        if(beverageRequest.Status != RequestStatus.DRAFT) {
            return;
        }
        beverageRepository.updateBeverage(beverageRequest);
    }

    public void SendRequest(Beverage beverageRequest) {
        if(beverageRequest.Status != RequestStatus.DRAFT) {
            return;
        }
        beverageRequest.Status = RequestStatus.PENDING;
        beverageRepository.updateBeverage(beverageRequest);
        beverageRepository.updateImageUrlForProduct(beverageRequest, beverageRequest.Name);
    }

    public void DeleteRequest() {
        String beverageId = beverageRequest.getValue().Id;

        Log.d(TAG, "DeleteRequest: deleting request with id = " + beverageId);
        beverageRepository.deleteBeverage(beverageId);
    }
}

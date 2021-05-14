package dk.au.mad21spring.appproject.group6.viewmodels.request;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import dk.au.mad21spring.appproject.group6.models.ActionResult;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.RequestStatus;

public class RequestDetailsActionsUserViewModel extends RequestDetailsBaseViewModel {

    private static final String TAG = "ReqDetailsActionsUserVM";

    public RequestDetailsActionsUserViewModel(@NonNull Application application) {
        super(application);
    }

    public ActionResult saveRequest(Beverage beverageRequest) {
        if(beverageRequest.Status != RequestStatus.DRAFT) {
            return ActionResult.Error("Request couldn't be sent", "Request has to be draft");
        }
        if(!validateBeverageRequest(beverageRequest)) {
            return ActionResult.Error("Request couldn't be sent", "Required fields missing");
        }
        beverageRepository.updateBeverage(beverageRequest);
        return ActionResult.Success("Request was saved!");
    }

    public ActionResult sendRequest(Beverage beverageRequest) {
        if(beverageRequest.Status != RequestStatus.DRAFT) {
            return ActionResult.Error("Request couldn't be sent", "Request has to be draft");
        }
        if(!validateBeverageRequest(beverageRequest)) {
            return ActionResult.Error("Request couldn't be sent", "Required fields missing");
        }

        beverageRequest.Status = RequestStatus.PENDING;
        beverageRepository.updateBeverage(beverageRequest);
        beverageRepository.updateImageUrlForProduct(beverageRequest, beverageRequest.Name);
        return ActionResult.Success("Request was sent!");
    }

    public ActionResult deleteRequest() {
        Beverage currentBeverageRequest = beverageRequest.getValue();

        if(currentBeverageRequest.Status != RequestStatus.DRAFT && currentBeverageRequest.Status != RequestStatus.DECLINED) {
            return ActionResult.Error("Request couldn't be deleted", "Request has to be draft or declined");
        }

        String beverageId = currentBeverageRequest.Id;
        Log.d(TAG, "DeleteRequest: deleting request with id = " + beverageId);
        beverageRepository.deleteBeverage(beverageId);
        return ActionResult.Success("Request was deleted!");
    }

    private boolean validateBeverageRequest(Beverage beverage) {
        return !beverage.Name.isEmpty() && !beverage.CompanyName.isEmpty();
    }
}

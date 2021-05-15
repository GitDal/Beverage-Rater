package dk.au.mad21spring.appproject.group6.viewmodels.request;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.models.ActionResult;
import dk.au.mad21spring.appproject.group6.models.db.Beverage;
import dk.au.mad21spring.appproject.group6.models.db.RequestStatus;

public class RequestDetailsActionsUserViewModel extends RequestDetailsBaseViewModel {

    private static final String TAG = "ReqDetailsActionsUserVM";
    @SuppressLint("StaticFieldLeak")
    private final Context appContext;

    public RequestDetailsActionsUserViewModel(@NonNull Application application) {
        super(application);
        appContext = application.getApplicationContext();
    }

    public ActionResult saveRequest(Beverage beverageRequest) {
        if(beverageRequest.Status != RequestStatus.DRAFT) {
            return ActionResult.Error(
                    appContext.getResources().getString(R.string.request_couldnt_be_saved),
                    appContext.getResources().getString(R.string.request_error_has_to_be_draft));
        }
        if(!validateBeverageRequest(beverageRequest)) {
            return ActionResult.Error(
                    appContext.getResources().getString(R.string.request_couldnt_be_saved),
                    appContext.getResources().getString(R.string.request_error_fields_missing));
        }
        beverageRepository.updateBeverage(beverageRequest);
        return ActionResult.Success(appContext.getResources().getString(R.string.request_was_saved));
    }

    public ActionResult sendRequest(Beverage beverageRequest) {
        if(beverageRequest.Status != RequestStatus.DRAFT) {
            return ActionResult.Error(
                    appContext.getResources().getString(R.string.request_couldnt_be_sent),
                    appContext.getResources().getString(R.string.request_error_has_to_be_draft));
        }
        if(!validateBeverageRequest(beverageRequest)) {
            return ActionResult.Error(
                    appContext.getResources().getString(R.string.request_couldnt_be_sent),
                    appContext.getResources().getString(R.string.request_error_fields_missing));
        }

        beverageRequest.Status = RequestStatus.PENDING;
        beverageRepository.updateBeverage(beverageRequest);
        beverageRepository.updateImageUrlForProductAsync(beverageRequest, beverageRequest.Name);
        return ActionResult.Success(appContext.getResources().getString(R.string.request_was_sent));
    }

    public ActionResult deleteRequest() {
        Beverage currentBeverageRequest = beverageRequest.getValue();

        if(currentBeverageRequest.Status != RequestStatus.DRAFT && currentBeverageRequest.Status != RequestStatus.DECLINED) {
            return ActionResult.Error(
                    appContext.getResources().getString(R.string.request_couldnt_be_deleted),
                    appContext.getResources().getString(R.string.request_error_has_to_be_draft_or_declined));
        }

        String beverageId = currentBeverageRequest.Id;
        Log.d(TAG, "DeleteRequest: deleting request with id = " + beverageId);
        beverageRepository.deleteBeverage(beverageId);
        return ActionResult.Success(appContext.getResources().getString(R.string.request_was_deleted));
    }

    private boolean validateBeverageRequest(Beverage beverage) {
        return !beverage.Name.isEmpty() && !beverage.CompanyName.isEmpty();
    }
}

package dk.au.mad21spring.appproject.group6.viewmodels.request;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.constants.GoogleSearchApi;
import dk.au.mad21spring.appproject.group6.models.db.Beverage;
import dk.au.mad21spring.appproject.group6.models.db.RequestStatus;

public class RequestViewModel extends AndroidViewModel {

    private BeverageRepository beverageRepository;
    private MutableLiveData<List<Beverage>> beverageRequests;

    public RequestViewModel(@NonNull Application application) {
        super(application);
        beverageRepository = BeverageRepository.getBeverageRepository(application);
        beverageRequests = new MutableLiveData<>();
    }

    public boolean currentUserIsAdmin() {
        return beverageRepository.currentUser != null && beverageRepository.currentUser.IsAdmin;
    }

    public LiveData<List<Beverage>> GetRequests() {
        if(beverageRepository.currentUser.IsAdmin) {
            beverageRepository.getAllPendingBeverages(beverageRequests);
        } else {
            beverageRepository.getAllBeveragesRequestedByUser(beverageRequests);
        }

        return beverageRequests;
    }

    public LiveData<List<Beverage>> GetNewRequests() {


        return beverageRequests;
    }

    public String CreateNewBeverageRequest(String eanNumber) {
        String defaultImageUrl = GoogleSearchApi.dummyImgUrl;
        Beverage beverageRequest =
                new Beverage("",
                        "",
                        "",
                        defaultImageUrl,
                        RequestStatus.DRAFT.getId(),
                        beverageRepository.currentUser.Email);

        if(!eanNumber.isEmpty()) {
            beverageRequest.EanNumbers.add(eanNumber);
        }

        beverageRepository.addBeverage(beverageRequest);
        return beverageRequest.Id;
    }

}

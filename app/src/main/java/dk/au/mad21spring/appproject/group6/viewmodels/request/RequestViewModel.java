package dk.au.mad21spring.appproject.group6.viewmodels.request;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.RequestStatus;

public class RequestViewModel extends AndroidViewModel {

    private BeverageRepository beverageRepository;
    private MutableLiveData<List<Beverage>> beverageRequests;

    public RequestViewModel(@NonNull Application application) {
        super(application);
        beverageRepository = BeverageRepository.getBeverageRepository(application);
        beverageRequests = new MutableLiveData<>();
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

    public String CreateNewBeverageRequest() {
        String defaultImageUrl = "https://s3-eu-west-2.amazonaws.com/newzimlive/wp-content/uploads/2019/01/09152727/Fizzy-Drinks.jpg";
        Beverage beverageRequest = new Beverage("untitled product", "", "", defaultImageUrl, RequestStatus.DRAFT.getId(), beverageRepository.currentUser.Email);

        beverageRepository.addBeverage(beverageRequest);
        return beverageRequest.Id;
    }

}

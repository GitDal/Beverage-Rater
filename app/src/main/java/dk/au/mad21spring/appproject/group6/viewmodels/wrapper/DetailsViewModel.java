package dk.au.mad21spring.appproject.group6.viewmodels.wrapper;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.models.db.Beverage;
import dk.au.mad21spring.appproject.group6.models.CurrentUser;

public class DetailsViewModel extends AndroidViewModel {

    BeverageRepository _beverageRepository;
    MutableLiveData<Beverage> _beverage;
    MutableLiveData<String> _beverageId;
    MutableLiveData<Boolean> _updateUserRatingBar;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        _beverageRepository = BeverageRepository.getBeverageRepository(application);
        _beverage = new MutableLiveData<Beverage>();
        _beverageId = new MutableLiveData<String>();
        _updateUserRatingBar = new MutableLiveData<Boolean>();
    }

    public LiveData<Beverage> getBeverage() {
        return _beverage;
    }

    public void setBeverage(Beverage beverage) {
        _beverage.setValue(beverage);
    }

    public String getBeverageId() {
        return _beverageId.getValue();
    }

    public void setBeverageId(String newId) {
        _beverageId.setValue(newId);
    }

    public Boolean getUpdateUserRatingBar() {
        return _updateUserRatingBar.getValue();
    }

    public void setUpdateUserRatingBar(Boolean update) {
        _updateUserRatingBar.setValue(update);
    }

    public CurrentUser getCurrentUser(){
        return _beverageRepository.currentUser;
    }

    public void updateBeverageScore(Beverage beverage, int userRating, Handler handler) {
        _beverageRepository.updateBeverageRating(beverage, userRating, handler);
    }

}
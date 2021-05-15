package dk.au.mad21spring.appproject.group6.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.models.db.Beverage;
import dk.au.mad21spring.appproject.group6.models.CurrentUser;

public class ProfileViewModel extends AndroidViewModel {

    private BeverageRepository _beverageRepository;
    MutableLiveData<List<Beverage>> _beverages;


    public ProfileViewModel(@NonNull Application application) {
        super(application);
        _beverageRepository = BeverageRepository.getBeverageRepository(application);
        _beverages = new MutableLiveData<List<Beverage>>();
        _beverageRepository.getAllApprovedBeverages(_beverages);
    }

    public CurrentUser getCurrentUser(){
        return _beverageRepository.currentUser;
    }

    public LiveData<List<Beverage>> getBeverages() {
        return _beverages;
    }
}
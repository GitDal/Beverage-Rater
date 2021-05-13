package dk.au.mad21spring.appproject.group6.viewmodels.wrapper;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import dk.au.mad21spring.appproject.group6.models.Beverage;

public class DetailsViewModel extends AndroidViewModel {

    MutableLiveData<Beverage> _beverage;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        _beverage = new MutableLiveData<Beverage>();
    }

    public LiveData<Beverage> getBeverage() {
        return _beverage;
    }

    public void setBeverage(Beverage beverage) {
        _beverage.setValue(beverage);
    }
}
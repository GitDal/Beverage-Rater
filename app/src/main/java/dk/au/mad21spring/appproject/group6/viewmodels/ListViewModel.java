package dk.au.mad21spring.appproject.group6.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.models.Beverage;

public class ListViewModel extends AndroidViewModel {

    private BeverageRepository _beverageRepository;
    MutableLiveData<List<Beverage>> _beverages;

    public ListViewModel(@NonNull Application application) {
        super(application);
        _beverageRepository = BeverageRepository.getBeverageRepository(application);
    }

    public LiveData<List<Beverage>> getBeverages() {
        if(_beverages == null){
            _beverages = new MutableLiveData<List<Beverage>>();
            _beverageRepository.getAllApprovedBeverages(_beverages);
        }
        return _beverages;
    }

    // Change to livedata
//    public List<Beverage> getAllBeverages(){
//        return _beverageRepository.getAllBeverages();
//    }

    public void addBeverage(Beverage beverage) {
        _beverageRepository.addBeverage(beverage);
    }
}

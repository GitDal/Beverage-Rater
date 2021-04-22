package dk.au.mad21spring.appproject.group6.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.models.Beverage;

public class ListViewModel extends AndroidViewModel {

    private BeverageRepository beverageRepository;

    public ListViewModel(@NonNull Application application) {
        super(application);
        beverageRepository = BeverageRepository.getBeverageRepository(application);
    }

    // Change to livedata
    public List<Beverage> getAllBeverages(){
        return beverageRepository.getAllBeverages();
    }
}

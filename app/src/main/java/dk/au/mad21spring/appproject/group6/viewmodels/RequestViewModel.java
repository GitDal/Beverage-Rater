package dk.au.mad21spring.appproject.group6.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import dk.au.mad21spring.appproject.group6.BeverageRepository;

public class RequestViewModel extends AndroidViewModel {

    private BeverageRepository beverageRepository;

    public RequestViewModel(@NonNull Application application) {
        super(application);
        beverageRepository = BeverageRepository.getBeverageRepository(application);
    }


}

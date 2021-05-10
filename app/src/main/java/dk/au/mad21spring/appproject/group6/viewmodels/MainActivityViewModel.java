package dk.au.mad21spring.appproject.group6.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import dk.au.mad21spring.appproject.group6.BeverageRepository;

public class MainActivityViewModel extends AndroidViewModel {

    private BeverageRepository beverageRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        beverageRepository = BeverageRepository.getBeverageRepository(application);
    }

    public void ResolveUserIsAdmin() {
        beverageRepository.ResolveUserIsAdmin();
    }

    public void SetUserIsAdminToDefault() {
        beverageRepository.CurrentUserIsAdmin = false;
    }
}

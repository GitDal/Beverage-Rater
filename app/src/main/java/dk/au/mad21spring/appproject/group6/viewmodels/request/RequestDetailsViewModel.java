package dk.au.mad21spring.appproject.group6.viewmodels.request;

import android.app.Application;

import androidx.annotation.NonNull;

public class RequestDetailsViewModel extends RequestDetailsBaseViewModel {

    public RequestDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean IsCurrentUserAdmin() {
        return beverageRepository.CurrentUserIsAdmin;
    }
}

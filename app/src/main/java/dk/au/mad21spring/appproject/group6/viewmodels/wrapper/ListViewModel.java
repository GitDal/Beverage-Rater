package dk.au.mad21spring.appproject.group6.viewmodels.wrapper;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import dk.au.mad21spring.appproject.group6.models.db.Beverage;

public class ListViewModel extends AndroidViewModel {

    MutableLiveData<List<Beverage>> _beverages;

    public ListViewModel(@NonNull Application application) {
        super(application);
        _beverages = new MutableLiveData<List<Beverage>>();
    }

    public LiveData<List<Beverage>> getBeverages() {
        return _beverages;
    }

    public void setBeverages(List<Beverage> beverages) {
        _beverages.setValue(beverages);
    }
}

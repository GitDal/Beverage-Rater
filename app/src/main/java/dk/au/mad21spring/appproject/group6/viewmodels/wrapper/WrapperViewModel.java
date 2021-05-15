package dk.au.mad21spring.appproject.group6.viewmodels.wrapper;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.models.db.Beverage;

public class WrapperViewModel extends AndroidViewModel {
    private BeverageRepository _beverageRepository;

    public enum SelectedFragment {LIST, DETAILS;}
    MutableLiveData<SelectedFragment> _selectedFragment;
    MutableLiveData<List<Beverage>> _beverages;
    MutableLiveData<Integer> _selectedBeverageIndex;
    MutableLiveData<String> _query;

    public WrapperViewModel(@NonNull Application application) {
        super(application);
        _beverageRepository = BeverageRepository.getBeverageRepository(application);
        _selectedFragment = new MutableLiveData<SelectedFragment>(SelectedFragment.LIST);
        _selectedBeverageIndex = new MutableLiveData<Integer>(0);
        _query = new MutableLiveData<String>("");
        _beverages = new MutableLiveData<List<Beverage>>();
        _beverageRepository.getAllApprovedBeverages(_beverages);
    }

    public LiveData<SelectedFragment> getSelectedFragment() {
        return _selectedFragment;
    }

    public void setSelectedFragment(SelectedFragment selection) {
        _selectedFragment.setValue(selection);
    }

    public LiveData<String> getQuery() {
        return _query;
    }

    public void setQuery(String query) {
        _query.setValue(query);
    }

    public LiveData<Integer> getSelectedBeverageIndex() {
        return _selectedBeverageIndex;
    }

    public void setSelectedBeverageIndex(Integer index) {
        _selectedBeverageIndex.setValue(index);
    }

    public LiveData<List<Beverage>> getBeverages() {
        return _beverages;
    }
}

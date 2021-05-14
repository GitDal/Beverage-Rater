package dk.au.mad21spring.appproject.group6.fragments.wrapper;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.viewmodels.wrapper.WrapperViewModel;

public class WrapperFragment extends Fragment implements WrapperInterface {

    private static final String TAG = "WrapperFragment";

    public enum Orientation {PORTRAIT, LANDSCAPE}

    Orientation orientation;

    //Fragment Tags
    private static final String BEVERAGE_LIST_FRAG = "beverage_list_fragment";
    private static final String BEVERAGE_DETAILS_FRAG = "beverage_details_fragment";

    WrapperViewModel vm;
    ListFragment listFragment;
    DetailsFragment detailsFragment;

    FragmentContainerView listFragmentContainer;
    FragmentContainerView detailsFragmentContainer;

    public WrapperFragment() {
    }

    public static WrapperFragment newInstance() {
        WrapperFragment fragment = new WrapperFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            orientation = Orientation.PORTRAIT;
        } else {
            orientation = Orientation.LANDSCAPE;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wrapper, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listFragmentContainer = view.findViewById(R.id.wrapperListFragment);
        detailsFragmentContainer = view.findViewById(R.id.wrapperDetailsFragment);

        initializeFragments();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.wrapperListFragment, listFragment, BEVERAGE_LIST_FRAG)
                .replace(R.id.wrapperDetailsFragment, detailsFragment, BEVERAGE_DETAILS_FRAG)
                .commit();

        vm = new ViewModelProvider(this).get(WrapperViewModel.class);
        vm.getSelectedFragment().observe(getViewLifecycleOwner(), new Observer<WrapperViewModel.SelectedFragment>() {
            @Override
            public void onChanged(WrapperViewModel.SelectedFragment selectedFragment) {
                switchFragment(selectedFragment);
            }
        });
        vm.getSelectedBeverageIndex().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer index) {
                List<Beverage> beverages = listFragment.getAdapterList();
                if (beverages != null) {
                    detailsFragment.setBeverage(beverages.get(index));
                }
            }
        });
        vm.getBeverages().observe(getViewLifecycleOwner(), new Observer<List<Beverage>>() {
            @Override
            public void onChanged(List<Beverage> beverages) {
                updateList(beverages, vm.getQuery().getValue());
            }
        });
        vm.getQuery().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String query) {
                updateList(vm.getBeverages().getValue(), query);
            }
        });
    }

    private void updateList(List<Beverage> beverages, String query) {
        if(beverages == null){
            return;
        }

        List<Beverage> queriedBeverages = new ArrayList<Beverage>();
        Beverage detailsBeverage = detailsFragment.getBeverage();

        for(Beverage beverage : beverages){
            if(detailsBeverage != null && detailsBeverage.Id == beverage.Id){
                detailsFragment.setBeverage(beverage);
            }

            if(beverage.Name.toLowerCase().contains(query.toLowerCase()) || beverage.CompanyName.toLowerCase().contains(query.toLowerCase())){
                queriedBeverages.add(beverage);
                continue;
            }

            if(beverage.EanNumbers == null){
                continue;
            }

            for(String ean : beverage.EanNumbers){
                if(ean.toLowerCase().contains(query.toLowerCase())){
                    queriedBeverages.add(beverage);
                    break;
                }
            }
        }

        listFragment.setBeverages(queriedBeverages);
    }

    private void initializeFragments() {
        listFragment = (ListFragment) getChildFragmentManager().findFragmentByTag(BEVERAGE_LIST_FRAG);
        if (listFragment == null) {
            listFragment = ListFragment.newInstance();
        }
        listFragment.setHandler(this);

        detailsFragment = (DetailsFragment) getChildFragmentManager().findFragmentByTag(BEVERAGE_DETAILS_FRAG);
        if (detailsFragment == null) {
            detailsFragment = DetailsFragment.newInstance();
        }
    }

    public void onBeverageSelected(int position) {
        vm.setSelectedBeverageIndex(position);
        vm.setSelectedFragment(WrapperViewModel.SelectedFragment.DETAILS);
    }

    public void queryList(String query){
        vm.setQuery(query);
    }

    private void switchFragment(WrapperViewModel.SelectedFragment fragmentSelection) {
        if (orientation == Orientation.LANDSCAPE) {
            // To make sure both containers are visible
            listFragmentContainer.setVisibility(View.VISIBLE);
            detailsFragmentContainer.setVisibility(View.VISIBLE);
            return;
        }

        switch (fragmentSelection) {
            case LIST: {
                listFragmentContainer.setVisibility(View.VISIBLE);
                detailsFragmentContainer.setVisibility(View.GONE);
                break;
            }
            case DETAILS: {
                listFragmentContainer.setVisibility(View.GONE);
                detailsFragmentContainer.setVisibility(View.VISIBLE);
                break;
            }
            default: {
                Log.d(TAG, "Unhandled switch case");
                break;
            }
        }
    }

    public boolean handlesOnBackPressed() {
        if (orientation == Orientation.LANDSCAPE || vm.getSelectedFragment().getValue() == WrapperViewModel.SelectedFragment.LIST) {
            return false;
        }

        vm.setSelectedFragment(WrapperViewModel.SelectedFragment.LIST);
        return true;
    }
}
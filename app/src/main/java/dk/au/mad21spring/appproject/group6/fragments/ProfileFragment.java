package dk.au.mad21spring.appproject.group6.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.CurrentUser;
import dk.au.mad21spring.appproject.group6.models.UserRating;
import dk.au.mad21spring.appproject.group6.viewmodels.ProfileViewModel;
import dk.au.mad21spring.appproject.group6.viewmodels.wrapper.ListViewModel;

public class ProfileFragment extends Fragment {

    TextView email, ratingsGiven, beveragesApproved, beveragesApprovedStatic;
    ProfileViewModel vm;

    public ProfileFragment() {}
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = view.findViewById(R.id.profileEmail);
        ratingsGiven = view.findViewById(R.id.profileRatingsGiven);
        beveragesApproved = view.findViewById(R.id.profileBeveragesApproved);
        beveragesApprovedStatic = view.findViewById(R.id.profileBeveragesApprovedStatic);

        vm = new ViewModelProvider(this).get(ProfileViewModel.class);
        vm.getBeverages().observe(getViewLifecycleOwner(), new Observer<List<Beverage>>() {
            @Override
            public void onChanged(List<Beverage> beverages) {
                updateFields(beverages);
            }
        });

        email.setText(vm.getCurrentUser().Email);
        beveragesApproved.setVisibility(vm.getCurrentUser().IsAdmin ? View.GONE : View.VISIBLE);
        beveragesApprovedStatic.setVisibility(vm.getCurrentUser().IsAdmin ? View.GONE : View.VISIBLE);
    }

    private void updateFields(List<Beverage> beverages) {
        if(beverages == null){
            return;
        }
        CurrentUser currentUser = vm.getCurrentUser();
        Integer numberOfBeveragesApproved = 0;
        Integer numberOfRatingsGiven = 0;

        for(Beverage beverage : beverages){
            if(beverage.RequestedByUserId.equals(currentUser.Email)){
                numberOfBeveragesApproved++;
            }

            if(beverage.UserRatings == null){
                continue;
            }

            for(UserRating userRating : beverage.UserRatings){
                if(userRating.userId.equals(currentUser.Email)){
                    numberOfRatingsGiven++;
                    break;
                }
            }
        }

        beveragesApproved.setText(numberOfBeveragesApproved.toString());
        ratingsGiven.setText(numberOfRatingsGiven.toString());
    }
}
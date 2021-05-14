package dk.au.mad21spring.appproject.group6.fragments.wrapper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.viewmodels.wrapper.DetailsViewModel;

public class DetailsFragment extends Fragment {

    TextView name, company, description, rating;
    ImageView img;
    DetailsViewModel vm;

    public DetailsFragment() {
    }

    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.detailsName);
        company = view.findViewById(R.id.detailsCompany);
        description = view.findViewById(R.id.detailsDescription);
        rating = view.findViewById(R.id.detailsRating);
        img = view.findViewById(R.id.detailsImage);

        vm = new ViewModelProvider(this).get(DetailsViewModel.class);
        vm.getBeverage().observe(getViewLifecycleOwner(), new Observer<Beverage>() {
            @Override
            public void onChanged(Beverage beverage) {
                updateUi(beverage);
            }
        });
    }

    public void setBeverage(Beverage beverage) {
        vm.setBeverage(beverage);
    }

    public Beverage getBeverage() {
        return vm.getBeverage().getValue();
    }

    private void updateUi(Beverage beverage) {
        name.setText(beverage.Name);
        company.setText(beverage.CompanyName);
        description.setText(beverage.BeverageInfo);
        rating.setText(beverage.GlobalRating.toString());
        Glide.with(img.getContext()).load(beverage.ImageUrl).into(img);
    }
}
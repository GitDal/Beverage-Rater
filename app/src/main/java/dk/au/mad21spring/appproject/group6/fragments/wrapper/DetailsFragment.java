package dk.au.mad21spring.appproject.group6.fragments.wrapper;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.UserRating;
import dk.au.mad21spring.appproject.group6.viewmodels.wrapper.DetailsViewModel;
import dk.au.mad21spring.appproject.group6.viewmodels.wrapper.WrapperViewModel;

public class DetailsFragment extends Fragment {

    TextView name, company, description, globalRating, userRating;
    SeekBar userRatingBar;
    Button backBtn, UpdateBtn;
    ImageView img;
    DetailsViewModel vm;

    private WrapperInterface _handler;
    WrapperFragment.Orientation orientation;

    public DetailsFragment() {
    }

    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            orientation = WrapperFragment.Orientation.PORTRAIT;
        } else {
            orientation = WrapperFragment.Orientation.LANDSCAPE;
        }
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
        globalRating = view.findViewById(R.id.detailsGlobalRating);
        userRating = view.findViewById(R.id.detailsUserRating);
        img = view.findViewById(R.id.detailsImage);
        userRatingBar = view.findViewById(R.id.detailsRatingbar);
        backBtn = view.findViewById(R.id.detailsBackBtn);

        userRatingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                userRating.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(orientation == WrapperFragment.Orientation.LANDSCAPE){
            backBtn.setVisibility(View.GONE);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _handler.switchFragment(WrapperViewModel.SelectedFragment.LIST);
            }
        });

        vm = new ViewModelProvider(this).get(DetailsViewModel.class);
        vm.getBeverage().observe(getViewLifecycleOwner(), new Observer<Beverage>() {
            @Override
            public void onChanged(Beverage beverage) {
                if(vm.getBeverageId() != null && vm.getBeverageId().equals(beverage.Id)){
                    vm.setUpdateUserRatingBar(false);
                }else {
                    vm.setUpdateUserRatingBar(true);
                    vm.setBeverageId(beverage.Id);
                }
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
        globalRating.setText(beverage.GlobalRating.toString());
        Glide.with(img.getContext()).load(beverage.ImageUrl).into(img);
        if(vm.getUpdateUserRatingBar().equals(true)){
            updateUserRatingElements(beverage);
        }
    }

    private void updateUserRatingElements(Beverage beverage) {
        if(beverage.UserRatings == null){
            userRatingBar.setProgress(0);
            userRating.setText("" + 0);
            return;
        }

        Integer userRatingValue = 0;
        for(UserRating ur : beverage.UserRatings){
            if(vm.getCurrentUser().Email.equals(ur.userId)){
                userRatingValue = (Integer) ur.rating;
                break;
            }
        }

        userRatingBar.setProgress(userRatingValue);
        userRating.setText("" + userRatingValue);
    }

    public void setHandler(WrapperInterface handler) {
        _handler = handler;
    }
}
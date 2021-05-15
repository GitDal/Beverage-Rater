package dk.au.mad21spring.appproject.group6.fragments.wrapper;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.constants.ResultExtras;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.UserRating;
import dk.au.mad21spring.appproject.group6.viewmodels.wrapper.DetailsViewModel;
import dk.au.mad21spring.appproject.group6.viewmodels.wrapper.WrapperViewModel;

public class DetailsFragment extends Fragment {

    TextView name, company, description, globalRating, userRating;
    SeekBar userRatingBar;
    Button backBtn, updateBtn;
    ImageView img;
    DetailsViewModel vm;
    Handler updateHandler;
    ExecutorService executor;
    private WrapperInterface _handler;

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
        globalRating = view.findViewById(R.id.detailsGlobalRating);
        userRating = view.findViewById(R.id.detailsUserRating);
        img = view.findViewById(R.id.detailsImage);
        userRatingBar = view.findViewById(R.id.detailsRatingbar);
        backBtn = view.findViewById(R.id.detailsBackBtn);
        updateBtn = view.findViewById(R.id.detailsUpdateRatingBtn);

        updateHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String name = msg.getData().getString(ResultExtras.UPDATE_RATING_NAME);
                int rating = msg.getData().getInt(ResultExtras.UPDATE_RATING_RATING);
                indicateUpdate(name, rating);
            }
        };

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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _handler.switchFragment(WrapperViewModel.SelectedFragment.LIST);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(executor == null){
                    executor = Executors.newSingleThreadExecutor();
                }

                executor.submit(() -> {
                    boolean alreadyRatedByUser = false;
                    int rating = userRatingBar.getProgress();
                    double score = 0;
                    int size = 0;
                    String ratingId = "";

                    Beverage beverage = vm.getBeverage().getValue();
                    if(beverage.UserRatings != null){
                        size += beverage.UserRatings.size();
                        for(Map.Entry<String, UserRating> userRating : beverage.UserRatings.entrySet()){
                            if(userRating.getValue().userId.equals(vm.getCurrentUser().Email)){
                                alreadyRatedByUser = true;
                                ratingId = userRating.getKey();
                                score += rating;
                                continue;
                            }
                            score += (double) userRating.getValue().rating;
                        }
                    }

                    if(!alreadyRatedByUser){
                        size += 1;
                        score += rating;
                    }

                    score = score / size;
                    vm.updateBeverageScore(beverage, ratingId, score, rating);

                    Message msg = new Message();
                    Bundle msgData = new Bundle();
                    msgData.putInt(ResultExtras.UPDATE_RATING_RATING, rating);
                    msgData.putString(ResultExtras.UPDATE_RATING_NAME, beverage.Name);
                    msg.setData(msgData);
                    updateHandler.sendMessage(msg);
                });
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

    private void indicateUpdate(String name, int rating) {
        Toast.makeText(getContext(), name + " " + getString(R.string.updateRatingText) + " " + rating, Toast.LENGTH_SHORT).show();
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
        globalRating.setText(String.format("%.1f", beverage.GlobalRating));
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
        for(Map.Entry<String, UserRating> ur : beverage.UserRatings.entrySet()){
            if(vm.getCurrentUser().Email.equals(ur.getValue().userId)){
                userRatingValue = (Integer) ur.getValue().rating;
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
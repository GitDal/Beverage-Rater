package dk.au.mad21spring.appproject.group6.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import dk.au.mad21spring.appproject.group6.EditTextExtensions;
import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.RequestStatus;
import dk.au.mad21spring.appproject.group6.viewmodels.RequestDetailsViewModel;

public class RequestDetailsUserFragment extends Fragment {

    private static final String TAG = "RequestDetailsUserFrag";
    private static final String REQUEST_ID = "request_id";

    private RequestDetailsViewModel vm;
    private ImageView imgBeverage;
    private EditText txtBeverageName, txtBeverageCompanyName, txtBeverageInfo;
    private Button sendRequestBtn, saveBtn;

    // TODO: Rename and change types of parameters
    private String requestId;

    public RequestDetailsUserFragment() {
        // Required empty public constructor
    }

    public static RequestDetailsUserFragment newInstance(String requestId) {
        RequestDetailsUserFragment fragment = new RequestDetailsUserFragment();
        Bundle args = new Bundle();
        args.putString(REQUEST_ID, requestId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: retrieving viewmodel");
        vm = new ViewModelProvider(this).get(RequestDetailsViewModel.class);

        if (getArguments() != null) {
            String beverageRequestId = getArguments().getString(REQUEST_ID);
            vm.SetRequestId(beverageRequestId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_details_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUI(view);

        Beverage beverageRequest = vm.GetRequest();

        txtBeverageName.setText(beverageRequest.Name);
        txtBeverageCompanyName.setText(beverageRequest.CompanyName);
        Glide.with(imgBeverage.getContext()).load(beverageRequest.ImageUrl).into(imgBeverage);
        txtBeverageInfo.setText(beverageRequest.BeverageInfo);

        if(beverageRequest.Status != RequestStatus.DRAFT) {
            EditTextExtensions.Disable(txtBeverageName, view.getContext());
            EditTextExtensions.Disable(txtBeverageCompanyName, view.getContext());
            EditTextExtensions.Disable(txtBeverageInfo, view.getContext());

            sendRequestBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.GONE);
        }
    }

    private void setupUI(View view) {
        imgBeverage = view.findViewById(R.id.requestDetailsUserBeverageImg);
        txtBeverageName = view.findViewById(R.id.requestDetailsUserBeverageName);
        txtBeverageCompanyName = view.findViewById(R.id.requestDetailsUserCompanyName);
        txtBeverageInfo = view.findViewById(R.id.requestDetailsUserInfoTxt);

        sendRequestBtn = view.findViewById(R.id.requestDetailsUserSaveRequestBtn);
        saveBtn = view.findViewById(R.id.requestDetailsUserSaveBtn);
        sendRequestBtn.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Sending request!", Toast.LENGTH_SHORT).show();
        });
        saveBtn.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Saving request!", Toast.LENGTH_SHORT).show();
        });


    }
}
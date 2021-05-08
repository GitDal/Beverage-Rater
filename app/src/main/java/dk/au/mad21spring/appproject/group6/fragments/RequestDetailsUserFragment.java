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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
            vm.SetRequestWithId(beverageRequestId);
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
        updateContent(view);
    }

    public void updateShownRequest(String requestId) {
        vm.SetRequestWithId(requestId);
        updateContent(getView());
    }

    private void setupUI(View view) {
        imgBeverage = view.findViewById(R.id.requestDetailsUserBeverageImg);
        txtBeverageName = view.findViewById(R.id.requestDetailsUserBeverageName);
        txtBeverageCompanyName = view.findViewById(R.id.requestDetailsUserCompanyName);
        txtBeverageInfo = view.findViewById(R.id.requestDetailsUserInfoTxt);

        sendRequestBtn = view.findViewById(R.id.requestDetailsUserSaveRequestBtn);
        saveBtn = view.findViewById(R.id.requestDetailsUserSaveBtn);
        sendRequestBtn.setOnClickListener(v -> send() );
        saveBtn.setOnClickListener(v -> save() );
    }

    private void updateContent(View view) {
        Beverage beverageRequest = vm.GetRequest();
        txtBeverageName.setText(beverageRequest.Name);
        txtBeverageCompanyName.setText(beverageRequest.CompanyName);
        Glide.with(imgBeverage.getContext()).load(beverageRequest.ImageUrl).into(imgBeverage);
        txtBeverageInfo.setText(beverageRequest.BeverageInfo);

        changeUIAccordingToStatus(view, beverageRequest.Status);
    }

    private void changeUIAccordingToStatus(View view, RequestStatus status) {
        if(status == RequestStatus.DRAFT) {
            //DRAFT --> user can edit and save/send
            EditTextExtensions.Enable(txtBeverageName, view.getContext());
            EditTextExtensions.Enable(txtBeverageCompanyName, view.getContext());
            EditTextExtensions.Enable(txtBeverageInfo, view.getContext());

            sendRequestBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.VISIBLE);
        } else {
            //NOT DRAFT --> readonly view
            EditTextExtensions.Disable(txtBeverageName, view.getContext());
            EditTextExtensions.Disable(txtBeverageCompanyName, view.getContext());
            EditTextExtensions.Disable(txtBeverageInfo, view.getContext());

            sendRequestBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.GONE);
        }
    }

    private void save() {
        Toast.makeText(getContext(), "Saving request!", Toast.LENGTH_SHORT).show();

        Beverage beverageRequest = vm.GetRequest();

        beverageRequest.Name = txtBeverageName.getText().toString();
        beverageRequest.CompanyName = txtBeverageCompanyName.getText().toString();
        beverageRequest.BeverageInfo = txtBeverageInfo.getText().toString();

        vm.SaveRequest(beverageRequest);
    }

    private void send() {
        Toast.makeText(getContext(), "Sending request!", Toast.LENGTH_SHORT).show();

        save();

        Beverage beverageRequest = vm.GetRequest();
        vm.SendRequest(beverageRequest);
    }
}
package dk.au.mad21spring.appproject.group6.fragments.request;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.models.ActionResult;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.BeverageRequestDetailsDTO;
import dk.au.mad21spring.appproject.group6.viewmodels.request.RequestDetailsActionsUserViewModel;

public class RequestDetailsActionsUserFragment extends Fragment {

    private static final String REQUEST_ID = "request_id";

    RequestDetailsActionsUserViewModel vm;
    Button saveBtn, sendRequestBtn, deleteBtn;

    public RequestDetailsActionsUserFragment() {
        // Required empty public constructor
    }

    public static RequestDetailsActionsUserFragment newInstance(String requestId) {
        RequestDetailsActionsUserFragment fragment = new RequestDetailsActionsUserFragment();
        Bundle args = new Bundle();
        args.putString(REQUEST_ID, requestId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = new ViewModelProvider(this).get(RequestDetailsActionsUserViewModel.class);

        if (getArguments() != null) {
            String beverageRequestId = getArguments().getString(REQUEST_ID);
            vm.setRequestWithId(beverageRequestId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_details_actions_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUI(getView());
    }

    private void setupUI(View view) {
        sendRequestBtn = view.findViewById(R.id.requestDetailsActionsUserSendRequestBtn);
        saveBtn = view.findViewById(R.id.requestDetailsActionsUserSaveBtn);
        deleteBtn = view.findViewById(R.id.requestDetailsActionsUserDeleteRequestBtn);
        sendRequestBtn.setOnClickListener(v -> send() );
        saveBtn.setOnClickListener(v -> save(true) );
        deleteBtn.setOnClickListener(v -> delete() );
    }

    private void save(boolean showMessage) {

        BeverageRequestDetailsDTO formData = ((RequestDetailsFragment) getParentFragment()).getBeverageFormData();
        Beverage beverageRequest = vm.getRequest().getValue();

        beverageRequest.Name = formData.Name;
        beverageRequest.CompanyName = formData.CompanyName;
        beverageRequest.BeverageInfo = formData.BeverageInfo;

        ActionResult result = vm.saveRequest(beverageRequest);
        if(showMessage) {
            result.ShowToastMessage(getContext());
        }
    }

    private void send() {

        save(false);
        Beverage beverageRequest = vm.getRequest().getValue();

        ActionResult result = vm.sendRequest(beverageRequest);
        result.ShowToastMessage(getContext());
    }

    private void delete() {
        ActionResult result = vm.deleteRequest();
        result.ShowToastMessage(getContext());
    }


}
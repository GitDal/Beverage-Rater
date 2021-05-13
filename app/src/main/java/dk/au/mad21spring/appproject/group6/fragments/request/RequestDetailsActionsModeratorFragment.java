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

import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.viewmodels.request.RequestDetailsActionsModeratorViewModel;

public class RequestDetailsActionsModeratorFragment extends Fragment {

    private static final String REQUEST_ID = "request_id";

    RequestDetailsActionsModeratorViewModel vm;
    Button approveBtn, declineBtn;

    public RequestDetailsActionsModeratorFragment() {
        // Required empty public constructor
    }

    public static RequestDetailsActionsModeratorFragment newInstance(String requestId) {
        RequestDetailsActionsModeratorFragment fragment = new RequestDetailsActionsModeratorFragment();
        Bundle args = new Bundle();
        args.putString(REQUEST_ID, requestId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = new ViewModelProvider(this).get(RequestDetailsActionsModeratorViewModel.class);

        if (getArguments() != null) {
            String beverageRequestId = getArguments().getString(REQUEST_ID);
            vm.setRequestWithId(beverageRequestId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_details_actions_moderator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUI(getView());
    }

    private void setupUI(View view) {
        approveBtn = view.findViewById(R.id.requestDetailsActionsModeratorApproveBtn);
        declineBtn = view.findViewById(R.id.requestDetailsActionsModeratorDeclineBtn);
        approveBtn.setOnClickListener(v -> approve() );
        declineBtn.setOnClickListener(v -> decline() );
    }

    private void approve() {
        Beverage beverageRequest = vm.getRequest().getValue();
        vm.approveRequest(beverageRequest);
    }

    private void decline() {
        Beverage beverageRequest = vm.getRequest().getValue();
        vm.declineRequest(beverageRequest);
    }
}
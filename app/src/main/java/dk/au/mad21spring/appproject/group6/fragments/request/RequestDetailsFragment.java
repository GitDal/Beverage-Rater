package dk.au.mad21spring.appproject.group6.fragments.request;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import dk.au.mad21spring.appproject.group6.EditTextExtensions;
import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.models.BeverageRequestDetailsDTO;
import dk.au.mad21spring.appproject.group6.models.db.Beverage;
import dk.au.mad21spring.appproject.group6.models.db.RequestStatus;
import dk.au.mad21spring.appproject.group6.viewmodels.request.RequestDetailsViewModel;

public class RequestDetailsFragment extends Fragment {

    private static final String TAG = "RequestDetailsFrag";
    private static final String REQUEST_ID = "request_id";

    //Fragments
    private static final String ACTIONS_USER_FRAG = "actions_user_fragment";
    private static final String ACTIONS_MODERATOR_FRAG = "actions_moderator_fragment";
    private RequestDetailsActionsUserFragment requestDetailsActionsUserFragment;
    private RequestDetailsActionsModeratorFragment requestDetailsActionsModeratorFragment;

    //State
    private RequestDetailsViewModel vm;

    //UI
    private ImageView imgBeverage;
    private EditText txtBeverageName, txtBeverageCompanyName, txtBeverageInfo;

    public RequestDetailsFragment() {
        // Required empty public constructor
    }

    public static RequestDetailsFragment newInstance(String requestId) {
        RequestDetailsFragment fragment = new RequestDetailsFragment();
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
            vm.setRequestWithId(beverageRequestId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeFragments();
        setupUI(view);
        vm.getRequest().observe(getViewLifecycleOwner(), beverageRequest -> updateContent(view, beverageRequest));
    }

    private void initializeFragments() {
        requestDetailsActionsUserFragment = (RequestDetailsActionsUserFragment) getChildFragmentManager().findFragmentByTag(ACTIONS_USER_FRAG);
        if(requestDetailsActionsUserFragment == null){
            String requestId = vm.getRequestId();
            Log.d(TAG, "initializeFragments: initializing requestDetailsActionsUserFragment (requestId = " + requestId + ")");
            requestDetailsActionsUserFragment = RequestDetailsActionsUserFragment.newInstance(requestId);
        }
        requestDetailsActionsModeratorFragment = (RequestDetailsActionsModeratorFragment) getChildFragmentManager().findFragmentByTag(ACTIONS_MODERATOR_FRAG);
        if(requestDetailsActionsModeratorFragment == null){
            String requestId = vm.getRequestId();
            Log.d(TAG, "initializeFragments: initializing requestDetailsActionsModeratorFragment (requestId = " + requestId + ")");
            requestDetailsActionsModeratorFragment = RequestDetailsActionsModeratorFragment.newInstance(requestId);
        }

        if(vm.IsCurrentUserAdmin()) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.requestDetailsActionsContainer, requestDetailsActionsModeratorFragment, ACTIONS_MODERATOR_FRAG)
                    .commit();
        } else {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.requestDetailsActionsContainer, requestDetailsActionsUserFragment, ACTIONS_USER_FRAG)
                    .commit();
        }
    }

    public String getCurrentRequestId() { return vm.getRequestId(); }

    public void updateShownRequest(String requestId) {
        vm.setRequestWithId(requestId);

        if(vm.IsCurrentUserAdmin()) {
            requestDetailsActionsModeratorFragment.vm.setRequestWithId(requestId);
        } else {
            requestDetailsActionsUserFragment.vm.setRequestWithId(requestId);
        }

        //updateContent(getView());
    }

    private void setupUI(View view) {
        imgBeverage = view.findViewById(R.id.requestDetailsBeverageImg);
        txtBeverageName = view.findViewById(R.id.requestDetailsBeverageName);
        txtBeverageCompanyName = view.findViewById(R.id.requestDetailsCompanyName);
        txtBeverageInfo = view.findViewById(R.id.requestDetailsInfoTxt);
    }

    private void updateContent(View view, Beverage beverageRequest) {
        if(beverageRequest != null) {
            Log.d(TAG, "updateContent: Updating content");
            txtBeverageName.setText(beverageRequest.Name);
            txtBeverageCompanyName.setText(beverageRequest.CompanyName);
            Glide.with(imgBeverage.getContext()).load(beverageRequest.ImageUrl).into(imgBeverage);
            txtBeverageInfo.setText(beverageRequest.BeverageInfo);

            changeUIAccordingToStatusAndRole(view, beverageRequest.Status);
        } else {
            Log.d(TAG, "updateContent: The beverageRequest for fragment is null");
        }
    }

    private void changeUIAccordingToStatusAndRole(View view, RequestStatus status) {

        if(status == RequestStatus.DRAFT) {
            //DRAFT --> user can edit and save/send
            EditTextExtensions.Enable(txtBeverageName, view.getContext());
            EditTextExtensions.Enable(txtBeverageCompanyName, view.getContext());
            EditTextExtensions.Enable(txtBeverageInfo, view.getContext());

        } else {
            //NOT DRAFT --> readonly view
            EditTextExtensions.Disable(txtBeverageName, view.getContext());
            EditTextExtensions.Disable(txtBeverageCompanyName, view.getContext());
            EditTextExtensions.Disable(txtBeverageInfo, view.getContext());
        }

        updateActionButtons(status);
    }

    private void updateActionButtons(RequestStatus status) {
        if(vm.IsCurrentUserAdmin()) {
            // Good to go! (Show approve/decline)
        } else  {
            // show buttons
            requestDetailsActionsUserFragment.updateActionButtons(status);
        }
    }

    public BeverageRequestDetailsDTO getBeverageFormData() {

        BeverageRequestDetailsDTO beverageRequestData = new BeverageRequestDetailsDTO();
        beverageRequestData.Name = txtBeverageName.getText().toString();
        beverageRequestData.CompanyName = txtBeverageCompanyName.getText().toString();
        beverageRequestData.BeverageInfo = txtBeverageInfo.getText().toString();

        return beverageRequestData;
    }
}
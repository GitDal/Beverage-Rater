package dk.au.mad21spring.appproject.group6.fragments.request;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import dk.au.mad21spring.appproject.group6.BarcodeScanningActivity;
import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.adapters.BeverageRequestAdapter;
import dk.au.mad21spring.appproject.group6.constants.InstanceStateExtras;
import dk.au.mad21spring.appproject.group6.constants.ResultExtras;
import dk.au.mad21spring.appproject.group6.models.ActionResult;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.viewmodels.request.RequestViewModel;

import static android.app.Activity.RESULT_OK;

public class RequestFragment extends Fragment implements BeverageRequestAdapter.IBeverageRequestItemClickedListener {

    private static final String TAG = "RequestFragment";

    //Fragment
    private static final String DETAILS_FRAG = "details_fragment";
    private static final int CAMERA_REQUEST = 888;
    private RequestDetailsFragment requestDetailsFragment;

    //State
    private RequestViewModel vm;
    private String latestAddedRequestId = null;

    //UI
    private RecyclerView rcvList;
    private BeverageRequestAdapter adapter;
    private Button addBeverageBtn;
    private ImageView barcodeScannerBtnIcon;

    public RequestFragment() {
        // Required empty public constructor
    }

    public static RequestFragment newInstance() {
        RequestFragment fragment = new RequestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: retrieving viewmodel");
        vm = new ViewModelProvider(this).get(RequestViewModel.class);

        int selectedPos = (savedInstanceState != null) ?
                savedInstanceState.getInt(InstanceStateExtras.REQUEST_SELECTED_ITEM_POS, 0) : 0;

        adapter = new BeverageRequestAdapter(this, selectedPos);

        // So that the details fragment is shown correctly when editing text.
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUI(view);
        vm.GetRequests().observe(getViewLifecycleOwner(), this::handleOnNewRequestsReceived);
    }

    @Override
    public void onBeverageRequestClicked(int index) {
        Beverage bRequest = adapter.getBeverageRequests().get(index);
        Log.d(TAG, "onBeverageRequestClicked: updating shown request (Id: " + bRequest.Id + ", Name: " + bRequest.Name + ")");

        if(requestDetailsFragment == null) {
            initializeFragment(bRequest.Id);
        } else {
            requestDetailsFragment.updateShownRequest(bRequest.Id);
            if(requestDetailsFragment.isHidden()) {
                showFragment();
            }
        }
    }

    private void setupUI(View view) {
        rcvList = view.findViewById(R.id.requestRcv);
        rcvList.setAdapter(adapter);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));

        addBeverageBtn = view.findViewById(R.id.requestAddBeverageBtn);
        barcodeScannerBtnIcon = view.findViewById(R.id.requestScanBeverageBtnImage);
        addBeverageBtn.setOnClickListener(v -> onAddNewBeverageRequestClicked() );
        barcodeScannerBtnIcon.setOnClickListener(v -> onScannerClicked());

        addBeverageBtn.setVisibility(vm.currentUserIsAdmin() ? View.GONE : View.VISIBLE);
        barcodeScannerBtnIcon.setVisibility(vm.currentUserIsAdmin() ? View.GONE : View.VISIBLE);
    }

    private void handleOnNewRequestsReceived(List<Beverage> updatedBeverages) {
        Log.d(TAG, "handleOnNewRequestsReceived: updating adapter with beverages");

        adapter.setBeverageRequests(updatedBeverages);

        if(latestAddedRequestId != null) {
            adapter.setSelectedBeverage(latestAddedRequestId);
            latestAddedRequestId = null;
        }

        if(adapter.getSelectedBeverage() != null) {
            initializeFragment(adapter.getSelectedBeverage().Id);
        } else if(requestDetailsFragment != null) {
            Log.d(TAG, "handleOnNewRequestsReceived: The request in detailsFragment was deleted - hiding fragment");
            hideFragment();
        }
    }

    private void onAddNewBeverageRequestClicked() {
        Log.d(TAG, "onAddNewBeverageRequestClicked: adding new draft");
        String id = vm.CreateNewBeverageRequest("");
        latestAddedRequestId = id; //So that we can select this item, when it becomes available in the list
        Toast.makeText(getContext(), "New draft added", Toast.LENGTH_SHORT).show();
    }

    private void onScannerClicked() {
        Intent barcodeScannerIntent = new Intent(getContext(), BarcodeScanningActivity.class);
        startActivityForResult(barcodeScannerIntent, CAMERA_REQUEST);
    }

    private void onScannerResult(String eanNumber) {
        Log.d(TAG, "onScannerResult: adding new draft");
        String id = vm.CreateNewBeverageRequest(eanNumber);
        latestAddedRequestId = id; //So that we can select this item, when it becomes available in the list
        Toast.makeText(getContext(), "New draft added (EAN-number \'" + eanNumber + "\')", Toast.LENGTH_SHORT).show();
    }

    private void initializeFragment(String requestId) {
        requestDetailsFragment = (RequestDetailsFragment) getChildFragmentManager().findFragmentByTag(DETAILS_FRAG);
        if(requestDetailsFragment == null){
            Log.d(TAG, "initializeFragment: initializing requestDetailsFragment (requestId = " + requestId + ")");
            requestDetailsFragment = RequestDetailsFragment.newInstance(requestId);
            insertFragment();
        } else if(requestDetailsFragment.getCurrentRequestId() != requestId) {
            Log.d(TAG, "initializeFragment: updating requestDetailsFragment (requestId = " + requestId + ")");
            requestDetailsFragment.updateShownRequest(requestId);
            if(requestDetailsFragment.isHidden()) {
                showFragment();
            }
        }
    }

    private void insertFragment() {
        if(requestDetailsFragment != null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.requestDetailsContainer, requestDetailsFragment, DETAILS_FRAG)
                    .commit();
        }
    }

    private void showFragment() {
        if(requestDetailsFragment != null) {
            getChildFragmentManager().beginTransaction()
                    .show(requestDetailsFragment)
                    .commit();
        }
    }

    private void hideFragment() {
        if(requestDetailsFragment != null) {
            getChildFragmentManager().beginTransaction()
                    .hide(requestDetailsFragment)
                    .commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                String eanNumber = data.getStringExtra(ResultExtras.BARCODE_RESULT);
                onScannerResult(eanNumber);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: Saving selected item position");
        outState.putInt(InstanceStateExtras.REQUEST_SELECTED_ITEM_POS, adapter.getSelectedPosition());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: Setting SoftInputMode back to default");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        super.onStop();
    }
}
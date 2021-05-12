package dk.au.mad21spring.appproject.group6.fragments.request;

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

import java.util.List;

import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.adapters.BeverageRequestAdapter;
import dk.au.mad21spring.appproject.group6.constants.InstanceStateExtras;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.viewmodels.request.RequestViewModel;

public class RequestFragment extends Fragment implements BeverageRequestAdapter.IBeverageRequestItemClickedListener {

    private static final String TAG = "RequestFragment";

    //Fragment
    private static final String DETAILS_FRAG = "details_fragment";
    private RequestDetailsFragment requestDetailsFragment;

    RequestViewModel vm;
    RecyclerView rcvList;
    BeverageRequestAdapter adapter;
    Button addBeverageBtn;

    public RequestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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
        rcvList = view.findViewById(R.id.requestRcv);
        rcvList.setAdapter(adapter);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setBeverageRequests(vm.GetRequests());
        addBeverageBtn = view.findViewById(R.id.requestAddBeverageBtn);
        addBeverageBtn.setOnClickListener(v -> addNewBeverageRequest() );

        initializeFragment();
    }

    public void addNewBeverageRequest() {
        Log.d(TAG, "addNewBeverageRequest: adding new draft");
        vm.CreateNewBeverageRequest();
        List<Beverage> updatedBeverageRequests = vm.GetNewRequests();

        adapter.setBeverageRequests(updatedBeverageRequests);
        int index = adapter.getItemCount() - 1;
        adapter.setSelectedPosition(index);
        requestDetailsFragment.updateShownRequest(updatedBeverageRequests.get(index).Id);
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

    @Override
    public void onBeverageRequestClicked(int index) {
        Beverage bRequest = vm.GetRequests().get(index);

        Log.d(TAG, "onBeverageRequestClicked: updating shown request (Id: " + bRequest.Id + ", Name: " + bRequest.Name + ")");
        requestDetailsFragment.updateShownRequest(bRequest.Id);
    }

    private void initializeFragment() {
        requestDetailsFragment = (RequestDetailsFragment) getChildFragmentManager().findFragmentByTag(DETAILS_FRAG);
        if(requestDetailsFragment == null){
            String defaultSelectedRequestId = vm.GetRequests().get(0).Id;
            Log.d(TAG, "initializeFragment: initializing requestDetailsFragment (requestId = " + defaultSelectedRequestId + ")");
            requestDetailsFragment = RequestDetailsFragment.newInstance(defaultSelectedRequestId);

            //TODO: If no items in vm, hide detailsFragment
        }

        getChildFragmentManager().beginTransaction()
                .replace(R.id.requestDetailsContainer, requestDetailsFragment, DETAILS_FRAG)
                .commit();
    }
}
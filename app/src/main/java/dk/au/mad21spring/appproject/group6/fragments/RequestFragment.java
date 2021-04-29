package dk.au.mad21spring.appproject.group6.fragments;

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
import android.widget.Toast;

import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.adaptors.BeverageRequestAdaptor;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.viewmodels.RequestViewModel;

public class RequestFragment extends Fragment implements BeverageRequestAdaptor.IBeverageRequestItemClickedListener {

    private static final String TAG = "RequestFragment";

    //Fragment Tags
    private static final String DETAILS_USER_FRAG = "details_user_fragment";

    RequestViewModel vm;
    RecyclerView rcvList;
    BeverageRequestAdaptor adapter;

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
        adapter = new BeverageRequestAdaptor(this);
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
    }

    @Override
    public void onBeverageRequestClicked(int index) {
        Toast.makeText(getContext(), vm.GetRequests().get(index).Name, Toast.LENGTH_SHORT).show();

        Beverage bRequest = vm.GetRequests().get(index);

        // begin fragment transaction
        // if user is user --> BeverageRequestForUser
            // if the request is draft --> you can edit
            // else --> readonly
            // save changes / send request buttons
        // if user is mod --> BeverageRequestForModerator
            // always readonly
            // approve/decline buttons

        getChildFragmentManager().beginTransaction()
                .replace(R.id.requestDetailsContainer, RequestDetailsUserFragment.newInstance(bRequest.Id), DETAILS_USER_FRAG)
                .commit();
    }
}
package dk.au.mad21spring.appproject.group6;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import dk.au.mad21spring.appproject.group6.adaptors.BeverageRequestAdaptor;
import dk.au.mad21spring.appproject.group6.viewmodels.RequestViewModel;

public class RequestFragment extends Fragment implements BeverageRequestAdaptor.IBeverageRequestItemClickedListener {

    private static final String TAG = "RequestFragment";
    
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: Inflating layout for fragment");
        View v = inflater.inflate(R.layout.fragment_request, container, false);
        rcvList = v.findViewById(R.id.requestRcv);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated: Callback to fragment");
        
        adapter = new BeverageRequestAdaptor(this);
        rcvList.setAdapter(adapter);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setBeverageRequests(vm.GetRequests());
    }

    @Override
    public void onBeverageRequestClicked(int index) {
        Toast.makeText(getContext(), vm.GetRequests().get(index).Name, Toast.LENGTH_SHORT).show();
    }
}
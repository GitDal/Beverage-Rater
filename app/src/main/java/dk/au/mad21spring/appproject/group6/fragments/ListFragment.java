package dk.au.mad21spring.appproject.group6.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.transform.Result;

import dk.au.mad21spring.appproject.group6.BarcodeScanningActivity;
import dk.au.mad21spring.appproject.group6.constants.ResultExtras;
import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.adapters.BeverageListAdapter;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.models.RequestStatus;
import dk.au.mad21spring.appproject.group6.constants.ResultExtras;
import dk.au.mad21spring.appproject.group6.viewmodels.ListViewModel;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CAMERA_SERVICE;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class ListFragment extends Fragment implements BeverageListAdapter.IBeverageClickedListener {
    private static final int CAMERA_REQUEST = 999;

    RecyclerView rcvList;
    BeverageListAdapter adapter;
    ListViewModel vm;
    ImageView barcodeScannerIcon;
    EditText searchBox;
    Button addBeverageBtn;

    public ListFragment() {
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        rcvList = v.findViewById(R.id.listRecyclerView);
        barcodeScannerIcon = v.findViewById(R.id.listScanBeverageIcon);
        searchBox = v.findViewById(R.id.listSearchBox);
        addBeverageBtn = v.findViewById(R.id.listAddBeverageButton);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new BeverageListAdapter(this);
        rcvList.setAdapter(adapter);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));

        vm = new ViewModelProvider(getActivity()).get(ListViewModel.class);
        vm.getBeverages().observe(getViewLifecycleOwner(), new Observer<List<Beverage>>() {
            @Override
            public void onChanged(List<Beverage> beverages) {
                adapter.setBeverages(beverages);
            }
        });


        barcodeScannerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScannerClicked();
            }
        });

        addBeverageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Beverage b = new Beverage(
                        UUID.randomUUID().toString(),
                        "Coca-Cola",
                        "Borat",
                        "Borat",
                        "https://soundvenue.com/wp-content/uploads/2020/10/Borat-2-2192x1233.jpg?v=1603479596",
                        RequestStatus.APPROVED.getId(),
                        "FNQRsSdq6IawAohh9MuxKCFxAxw2"
                );
                vm.addBeverage(b);
                Toast.makeText(getContext(),"Test", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStop() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        super.onStop();
    }


    @Override
    public void onBeverageClicked(int index) {
        // Open details fragment for selected beverage
        Toast.makeText(getContext(), vm.getBeverages().getValue().get(index).Name, Toast.LENGTH_SHORT).show();
    }

    private void onScannerClicked() {
        Intent barcodeScannerIntent  = new Intent(getContext(), BarcodeScanningActivity.class);
        startActivityForResult(barcodeScannerIntent, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                String barcode = data.getStringExtra(ResultExtras.BARCODE_RESULT);
                searchBox.setText(barcode);
            }
        }
    }
}
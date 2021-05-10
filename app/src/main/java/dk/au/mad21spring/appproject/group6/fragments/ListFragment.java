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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
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

import javax.xml.transform.Result;

import dk.au.mad21spring.appproject.group6.BarcodeScanningActivity;
import dk.au.mad21spring.appproject.group6.Constants;
import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.adapters.BeverageListAdapter;
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
    SearchView searchBox;

    public ListFragment() {
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        rcvList = v.findViewById(R.id.listRecyclerView);
        barcodeScannerIcon = v.findViewById(R.id.listScanBeverageIcon);
        searchBox = v.findViewById(R.id.listSearchBeverage);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new BeverageListAdapter(this);
        rcvList.setAdapter(adapter);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));

        vm = new ViewModelProvider(getActivity()).get(ListViewModel.class);
        adapter.setBeverages(vm.getAllBeverages());

        barcodeScannerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScannerClicked();
            }
        });
    }


    @Override
    public void onBeverageClicked(int index) {
        // Open details fragment for selected beverage
        Toast.makeText(getContext(), vm.getAllBeverages().get(index).Name, Toast.LENGTH_SHORT).show();
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
                String barcode = data.getStringExtra(Constants.BARCODE_RESULT);
                searchBox.setQuery(barcode, true);
            }
        }
    }
}
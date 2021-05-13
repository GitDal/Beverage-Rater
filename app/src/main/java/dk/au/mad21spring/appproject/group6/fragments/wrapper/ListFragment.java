package dk.au.mad21spring.appproject.group6.fragments.wrapper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;
import dk.au.mad21spring.appproject.group6.BarcodeScanningActivity;
import dk.au.mad21spring.appproject.group6.constants.ResultExtras;
import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.adapters.BeverageListAdapter;
import dk.au.mad21spring.appproject.group6.models.Beverage;
import dk.au.mad21spring.appproject.group6.viewmodels.wrapper.ListViewModel;

import static android.app.Activity.RESULT_OK;

public class ListFragment extends Fragment implements BeverageListAdapter.IBeverageClickedListener {
    private static final int CAMERA_REQUEST = 999;

    private RecyclerView rcvList;
    private BeverageListAdapter adapter;
    private ListViewModel vm;
    private ImageView barcodeScannerIcon;
    private EditText searchBox;

    private WrapperInterface beverageSelectionHandler;

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
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvList = view.findViewById(R.id.listRecyclerView);
        barcodeScannerIcon = view.findViewById(R.id.listScanBeverageIcon);
        searchBox = view.findViewById(R.id.listSearchBox);

        adapter = new BeverageListAdapter(this);
        rcvList.setAdapter(adapter);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));

        vm = new ViewModelProvider(this).get(ListViewModel.class);
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

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                beverageSelectionHandler.queryList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setHandler(WrapperInterface handler) {
        beverageSelectionHandler = handler;
    }

    public List<Beverage> getAdapterList(){
        return adapter.getBeverages();
    }

    @Override
    public void onBeverageClicked(int index) {
        if (beverageSelectionHandler != null) {
            beverageSelectionHandler.onBeverageSelected(index);
        }
    }

    private void onScannerClicked() {
        Intent barcodeScannerIntent = new Intent(getContext(), BarcodeScanningActivity.class);
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

    public void setBeverages(List<Beverage> beverages) {
        vm.setBeverages(beverages);
    }
}
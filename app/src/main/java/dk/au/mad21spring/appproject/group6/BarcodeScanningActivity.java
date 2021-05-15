package dk.au.mad21spring.appproject.group6;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import dk.au.mad21spring.appproject.group6.constants.ResultExtras;


// Inspiration on how to work with camera2 and barcode scanning using ML Kits bar code scanner came from:
// https://www.youtube.com/watch?v=kuv8uK-5CLY
// https://proandroiddev.com/building-barcode-qr-code-scanner-for-android-using-google-ml-kit-and-camerax-220b2852589e
// https://developers.google.com/ml-kit/vision/barcode-scanning/android

// The article from proAndroiddev was used to begin with as a guide, but the youtube video and the
// documentation gave sufficient information on how to use barcode scanning with camera2

public class BarcodeScanningActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION = 1;
    private static final String TAG = "BarcodeScanningActivity";

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanning);

        if(cameraPermissionGranted()){
            cameraSetup();
        } else{
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private boolean cameraPermissionGranted() {
        return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION) {
            if (cameraPermissionGranted()) {
                cameraSetup();
            } else {
                Log.d(TAG, "Camera permission not given");
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void cameraSetup() {
        PreviewView cameraPreviewView = findViewById(R.id.barcodePreviewView);
        ListenableFuture cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
                    try {
                        ProcessCameraProvider cameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();

                        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

                        Preview preview = new Preview.Builder().build();
                        preview.setSurfaceProvider(cameraPreviewView.createSurfaceProvider());

                        ImageAnalysis analyzer = new ImageAnalysis.Builder().build();
                        analyzer.setAnalyzer(getMainExecutor(), new ImageAnalysis.Analyzer() {
                            @Override
                            public void analyze(@NonNull ImageProxy imageProxy) {
                                @SuppressLint("UnsafeExperimentalUsageError") Image mediaImage = imageProxy.getImage();
                                if (mediaImage != null) {
                                    InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                                    BarcodeScanner scanner = BarcodeScanning.getClient();

                                    scanner.process(image).addOnSuccessListener(barcodes -> {
                                        if(barcodes.size() > 0){
                                            Barcode barcode = barcodes.get(0);

                                            Intent result = new Intent();
                                            result.putExtra(ResultExtras.BARCODE_RESULT, barcode.getRawValue());
                                            setResult(RESULT_OK, result);
                                            finish();
                                        }
                                    }).addOnFailureListener(failure ->
                                            Log.e(TAG, "Failure Scanning: " + failure.getMessage())
                                    ).addOnCompleteListener(complete ->
                                            imageProxy.close()
                                    );
                                }
                            }
                        });

                        cameraProvider.unbindAll();
                        cameraProvider.bindToLifecycle(this, cameraSelector, preview, analyzer);
                    } catch (Exception e) {
                        Log.e(TAG, "cameraSetup: Failed with message: " + e.getMessage());
                    }
                },
                ContextCompat.getMainExecutor(this)
        );
    }

   
}
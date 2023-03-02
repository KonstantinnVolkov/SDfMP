package com.example.qr_reader;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends AppCompatActivity {

    private Button generateQrBtn;
    private Button openScannerBtn;
    private EditText textToEncodeInp;
    private ImageView qrImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
    }

    private void initComponents() {
        generateQrBtn = findViewById(R.id.generateQrBtn);
        generateQrBtn.setEnabled(false);
        generateQrBtn.setOnClickListener((v) -> {
            handleGenerateQrBtnClick();
        });

        openScannerBtn = findViewById(R.id.openScannerBtn);
        openScannerBtn.setOnClickListener(v -> {
            handleOpenScannerBtnClick();
        });

        qrImageView = findViewById(R.id.qr_image);

        textToEncodeInp = findViewById(R.id.textToEncodeInput);
        textToEncodeInp.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                generateQrBtn.setEnabled(!s.toString().isEmpty());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void handleOpenScannerBtnClick() {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("Volume up to flash on");
        scanOptions.setBeepEnabled(false);
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(QrReaderActivity.class);
        barLauncher.launch(scanOptions);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Scanning Result");
                builder.setMessage(result.getContents());
                builder.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                }).show();
            }
    );

    private void handleGenerateQrBtnClick() {
        generateQrFromText(textToEncodeInp.getText().toString());
    }

    private void generateQrFromText(String textToEncode) {
        QRGEncoder qrGenerator = new QRGEncoder(
                textToEncode,
                null,
                QRGContents.Type.TEXT,
                700
        );
        try {
            Bitmap bitMap = qrGenerator.getBitmap();
            qrImageView.setImageBitmap(bitMap);

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }


}
package com.example.qr_reader;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

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
        generateQrBtn.setOnClickListener((v) -> handleGenerateQrBtnClick());

        openScannerBtn = findViewById(R.id.openScannerBtn);
        openScannerBtn.setOnClickListener(v -> handleOpenScannerBtnClick());

        qrImageView = findViewById(R.id.qr_image);
        qrImageView.setOnLongClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenu().add("Save");
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getTitle().toString().equals("Save")) {
                    saveImageToGallery();
                    return true;
                }
                return false;
            });
            popupMenu.show();
            return true;
        });

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

    private void saveImageToGallery() {
        //init dir to save
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File dir = new File(root + "/QR_SCANNER");
        if (!dir.exists()) {
            dir.mkdir();
        }

        //Build image name and create file obj
        final String imgName = String.format("img-%s.jpg", UUID.randomUUID().toString());
        File img = new File(dir, imgName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(img);
            Drawable drawable = qrImageView.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
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
                final String decodedText = result.getContents();
                final boolean isUrl = URLUtil.isValidUrl(decodedText);
                if (isUrl) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(decodedText)));
                } else {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied!", decodedText);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
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
package com.arisu.mungexambro;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences.Editor SeEditor;
    private EditText URLTextInput;
    public MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainActivity = this;
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        URLTextInput = findViewById(R.id.URLInputText);
        SharedPreferences settings = getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);
        URLTextInput.setText(settings.getString("LastURL", null));
        SeEditor = settings.edit();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle(R.string.str_exit)
                .setMessage(R.string.str_makesure_exit)
                .setPositiveButton(R.string.yes, (arg0,arg1) -> {
                    MainActivity.super.onBackPressed();
                    MainActivity.this.finish();})
                .setNegativeButton(R.string.no, null).show();
    }

    private ActivityResultLauncher<Intent> ResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                Log.i("WATASHIIIII",result.toString());
                if(result.getResultCode() == RESULT_OK) MainActivity.this.finish();
            });

    public void GO_Onclick(View view){
        String URL_Link = URLTextInput.getText().toString();
        if(URL_Link.contains("http://")) URL_Link = URL_Link.replace("http://","");
        if(!URL_Link.contains("http")) URL_Link = "https://" +URL_Link;
        SeEditor.putString("LastURL",URL_Link);
        SeEditor.apply();
        //startActivity(new Intent(this, BrowserActivity.class).putExtra("TheURL", URL_Link));
        ResultLauncher.launch(new Intent(this, BrowserActivity.class).putExtra("TheURL", URL_Link));
    }

    private ActivityResultLauncher<Intent> QRLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == RESULT_OK){
                    IntentResult intentResult =  IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                    Log.i("WATASHII DESUUU",intentResult.getContents().toString());
                    URLTextInput.setText(intentResult.getContents().toString());
                    GO_Onclick(null);
                }
    });

    public void QRScan_Onclick(View view){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setPrompt(getString(R.string.str_scannig_qrcode));
        QRLaucher.launch(intentIntegrator.createScanIntent());
    }
}
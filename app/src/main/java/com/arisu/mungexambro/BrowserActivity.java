package com.arisu.mungexambro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends AppCompatActivity {
    private WebView PageWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Browser);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        PageWebView = findViewById(R.id.Page_WebView);
        PageWebView.getSettings().setJavaScriptEnabled(true);
        PageWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        PageWebView.setWebViewClient(new WebViewClient());
        PageWebView.setWebChromeClient(new WebChromeClient());
        PageWebView.loadUrl(getIntent().getExtras().getString("TheURL"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser_menu,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle(R.string.str_exit)
                .setMessage(R.string.str_makesure_exit)
                .setPositiveButton(R.string.yes, (arg0,arg1)-> {
                    setResult(RESULT_OK);
                    finish();
                })
                .setNegativeButton(R.string.no, (arg0, arg1) -> {}).show();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.BrMn_Refresh:
                PageWebView.reload();
                return true;
            case R.id.BrMn_Print:

                return true;
            case R.id.BrMn_About:
                Dialog AbtDialog = new Dialog(this);
                AbtDialog.setContentView(R.layout.about_page);
                AbtDialog.setCancelable(true);
                /*AbtDialog.findViewById(R.id.Abt_Wakarimashita).setOnClickListener(view -> {
                    AbtDialog.dismiss();
                });*/
                AbtDialog.show();
                return true;
            case R.id.BrMn_Exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
package com.arisu.mungexambro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends AppCompatActivity {
    private WebView PageWebView;

    @SuppressLint("SetJavaScriptEnabled")
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

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser_menu,menu);
        //Show IC
        if(menu instanceof MenuBuilder) ((MenuBuilder) menu).setOptionalIconsVisible(true);
        return true;
    }

    @Override
    public void onBackPressed() { //To-do double back
        new AlertDialog.Builder(this).setTitle(R.string.str_exit)
                .setMessage(R.string.str_makesure_exit)
                .setPositiveButton(R.string.yes, (arg0,arg1)-> {
                    setResult(RESULT_OK);
                    finish();
                })
                .setNegativeButton(R.string.no, (arg0, arg1) -> {}).show();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.BrMn_Refresh:
                PageWebView.reload();
                return true;
            case R.id.BrMn_PBack:
                PageWebView.goBack();
                return true;
            case R.id.BrMn_PForward:
                PageWebView.goForward();
                return true;
            case R.id.BrMn_Print:
                PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
                String JobName = PageWebView.getTitle();
                PrintDocumentAdapter printAdapter = PageWebView.createPrintDocumentAdapter(JobName);
                //PrintJob printJob = printManager.print(JobName, printAdapter, new PrintAttributes.Builder().build());
                printManager.print(JobName, printAdapter, new PrintAttributes.Builder().build());
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
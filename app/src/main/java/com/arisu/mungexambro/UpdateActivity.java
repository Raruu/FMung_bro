package com.arisu.mungexambro;

import static androidx.core.content.FileProvider.getUriForFile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Browser);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.str_update);
        ((TextView)findViewById(R.id.UpdatePage_Changelog)).setMovementMethod(new ScrollingMovementMethod());
        CheckWritePermission();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_DENIED){
            new AlertDialog.Builder(this).setMessage(R.string.str_plsallowpermissionrequest)
                    .setPositiveButton(R.string.str_understand, (arg0,arg1) ->{ CheckWritePermission();})
                    .setCancelable(false).show();
        }
    }

    private void CheckWritePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void UpdateBtn_click(View view){
        ((Button)findViewById(R.id.UpdatePage_Button)).setEnabled(false);
        if(IsUpdateAvailable){
            ((Button)findViewById(R.id.UpdatePage_Button)).setText(R.string.str_downloading);
            DownloadUpdateAPKInBg();
        } else {
            ((TextView)findViewById(R.id.UpdatePage_TextStatus)).setText(R.string.str_checking);
            ((Button)findViewById(R.id.UpdatePage_Button)).setText(R.string.str_checking);
            CheckUpdateInBg();
        }
    }

    private boolean IsUpdateAvailable = false;
    private String Version, UpdateAPKURL = null;
    private ExecutorService executorService;
    private Handler handler = new Handler(Looper.getMainLooper());


     private void CheckUpdateInBg(){
         final OnProcessedListener listener = new AbstractOnProcessedListener() {
             @Override
             public void onProcessedCheckUpdate(final String Result) {
                 handler.post(new Runnable() {
                     @Override
                     public void run() {
                         //Update Ui Here
                         if(IsUpdateAvailable){
                             ((TextView)findViewById(R.id.UpdatePage_TextStatus)).setText(R.string.str_updateavailable);
                             ((TextView)findViewById(R.id.UpdatePage_Changelog)).setText(Result);
                             ((Button)findViewById(R.id.UpdatePage_Button)).setText(R.string.str_update);
                             ((Button)findViewById(R.id.UpdatePage_Button)).setEnabled(true);
                         }
                         else{
                             ((TextView)findViewById(R.id.UpdatePage_TextStatus)).setText(R.string.str_noupdateavailable);
                             ((TextView)findViewById(R.id.UpdatePage_Changelog)).setText(Result);
                             ((Button)findViewById(R.id.UpdatePage_Button)).setText(R.string.str_check);
                             ((Button)findViewById(R.id.UpdatePage_Button)).setEnabled(true);
                         }
                         executorService.shutdownNow();
                     }
                 });
             }
         };

        Runnable backgroundRunnable = () -> {
            // Perform background and result here
            try {
                HttpsURLConnection UpdateHttps = (HttpsURLConnection) new URL("https://api.github.com/repos/Raruu/FMung_bro/releases").openConnection();
                UpdateHttps.setRequestMethod("GET");
                UpdateHttps.connect();

                BufferedReader GitReleaseRepo = new BufferedReader(new InputStreamReader(UpdateHttps.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line, ChangeLog = null;
                while((line = GitReleaseRepo.readLine()) != null){
                    stringBuilder.append(line + "\n");
                    if(line.contains("\"body\"")){
                        ChangeLog = line.substring(line.indexOf("\"body\"") + 8);
                        break;
                    }
                }
                String StrRepo = stringBuilder.toString();
                Version = StrRepo.substring(StrRepo.indexOf("\"name\":\"v") + 9, StrRepo.indexOf("\",", StrRepo.indexOf("\"name\":\"v")));
                UpdateAPKURL = StrRepo.substring(StrRepo.indexOf("browser_download_url") + 23, StrRepo.indexOf(".apk\"", StrRepo.indexOf("browser_download_url")) + 4);
                ChangeLog = ChangeLog.substring(0, ChangeLog.length() - 3);

                if(Float.parseFloat(Version) > Float.parseFloat(BuildConfig.VERSION_NAME)){
                    IsUpdateAvailable = true;
                    updateFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            +"/FakeMung/app-"+ Version +".apk");
                }
                listener.onProcessedCheckUpdate(Version +"\n\n" + getString(R.string.str_changelog) + ChangeLog);
                GitReleaseRepo.close();
            } catch (IOException e) {
                Log.e("GitLink", e.toString());
                e.printStackTrace();
            }
        };
        if(executorService == null || executorService.isShutdown()) executorService = Executors.newSingleThreadExecutor();
        executorService.execute(backgroundRunnable);
     }

     private File updateFile;
     private void DownloadUpdateAPKInBg(){
         Uri ContentUri = getUriForFile(this, BuildConfig.APPLICATION_ID, updateFile);
         final OnProcessedListener listener = new AbstractOnProcessedListener() {
            @Override
            public void onProcessedDlUpdate() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    // Plan Add: progressbar
                    }
                });
            }
             @Override
             public void onFinishedDlUpdate() {
                 handler.post(new Runnable() {
                     @Override
                     public void run() {
                         Intent OpenAPKIntent = new Intent(Intent.ACTION_VIEW, ContentUri);
                         OpenAPKIntent.setDataAndType(ContentUri,"application/vnd.android.package-archive");
                         OpenAPKIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                         startActivity(OpenAPKIntent);
                         ((Button)findViewById(R.id.UpdatePage_Button)).setText(R.string.str_install);
                         ((Button)findViewById(R.id.UpdatePage_Button)).setEnabled(true);
                         executorService.shutdownNow();
                     }
                 });
             }
         };

        Runnable backgroundRunnable = () -> {
            try {
                if(!updateFile.exists()){
                    if(!updateFile.getParentFile().exists()) updateFile.getParentFile().mkdirs();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(UpdateAPKURL).openStream());
                    FileOutputStream fileOutputStream = new FileOutputStream(updateFile);
                    int count;
                    byte data[]= new byte[1024];
                    while((count = bufferedInputStream.read(data)) != -1){
                        fileOutputStream.write(data, 0, count);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    bufferedInputStream.close();
                }
                listener.onFinishedDlUpdate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        if(executorService == null || executorService.isShutdown()) executorService = Executors.newSingleThreadExecutor();
        executorService.execute(backgroundRunnable);
     }
}
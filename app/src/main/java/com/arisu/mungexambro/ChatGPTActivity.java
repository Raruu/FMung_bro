package com.arisu.mungexambro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.arisu.mungexambro.databinding.ActivityChatGptactivityBinding;

public class ChatGPTActivity extends AppCompatActivity {

    private ActivityChatGptactivityBinding binding;
    public String ChatToken;
    public float ChatTemperature;
    public int ChatMaxToken;
    public SharedPreferences.Editor SeEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Browser);
        super.onCreate(savedInstanceState);
        ConfigurePreferences();

        binding = ActivityChatGptactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_chatgpt_home, R.id.navigation_chatgpt_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_chat_gptactivity);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    private void ConfigurePreferences(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("ChatGPT",MODE_PRIVATE);
        ChatMaxToken = settings.getInt("ChatMaxToken", 0);
        ChatTemperature = settings.getFloat("ChatTemperature", 0);
        ChatToken = settings.getString("ChatToken", null);
        SeEditor = settings.edit();
    }

    public void SavePreferences(){
        SeEditor.putString("ChatToken", ChatToken);
        SeEditor.putFloat("ChatTemperature", ChatTemperature);
        SeEditor.putInt("ChatMaxToken", ChatMaxToken);
        SeEditor.apply();
    }
}
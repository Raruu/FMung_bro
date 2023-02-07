package com.arisu.mungexambro.ui.chatgpt_settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arisu.mungexambro.ChatGPTActivity;
import com.arisu.mungexambro.databinding.FragmentChatgptSettingsBinding;

public class ChatGPTSettingsFragment extends Fragment {

    private FragmentChatgptSettingsBinding binding;
    private ChatGPTActivity chatGPTActivity;
    private ChatGPTSettingsViewModel chatGPTSettingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        chatGPTSettingsViewModel = new ViewModelProvider(this).get(ChatGPTSettingsViewModel.class);
        chatGPTActivity = (ChatGPTActivity) this.getActivity();

        binding = FragmentChatgptSettingsBinding.inflate(inflater, container, false);

        binding.BtnSetToken.setOnClickListener(view -> { OnClickSetToken(view);});
        //final TextView textView = binding.InputToken;
        //chatGPTSettingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadTokenSettings();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void OnClickSetToken(View view){
        chatGPTActivity.ChatToken = binding.InputToken.getText().toString();
        chatGPTActivity.ChatTemperature = binding.sliderTemperature.getValue();
        chatGPTActivity.ChatMaxToken = Math.round(binding.sliderMaxToken.getValue());
        chatGPTActivity.SavePreferences();
        Toast.makeText(this.getContext(), "Changed", Toast.LENGTH_SHORT).show();
        Log.i("Watashi","SetTokenCalled");
    }

    private void LoadTokenSettings(){
        //((TextView)binding.getRoot().findViewById(R.id.InputToken)).setText(chatGPTActivity.ChatToken);
        Toast.makeText(this.getContext(),"LoadTokenCalled",Toast.LENGTH_SHORT).show();
        binding.InputToken.setText(chatGPTActivity.ChatToken);
        binding.sliderTemperature.setValue((float)chatGPTActivity.ChatTemperature);
        binding.sliderMaxToken.setValue(chatGPTActivity.ChatMaxToken);
    }

}
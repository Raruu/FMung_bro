package com.arisu.mungexambro.ui.chatgpt_home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arisu.mungexambro.ChatGPTActivity;
import com.arisu.mungexambro.R;
import com.arisu.mungexambro.databinding.FragmentChatgptHomeBinding;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;

import java.util.List;

public class ChatGPTHomeFragment extends Fragment {

    private FragmentChatgptHomeBinding binding;
    private ChatGPTActivity chatGPTActivity;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ChatGPTHomeViewModel chatGPTHomeViewModel = new ViewModelProvider(this).get(ChatGPTHomeViewModel.class);
        chatGPTActivity = (ChatGPTActivity) this.getActivity();

        binding = FragmentChatgptHomeBinding.inflate(inflater, container, false);

        binding.btnSendPrompt.setOnClickListener(view -> {BtnSendPrompt(view);});
        //final TextView textView = binding.textHome;
        //chatGPTHomeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void BtnSendPrompt(View view){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            OpenAiService openAiService = new OpenAiService(chatGPTActivity.ChatToken);


            CompletionRequest completionRequest = CompletionRequest.builder().model("ada")
                    .prompt(binding.InputPrompt.getText().toString())
                    .temperature((double)chatGPTActivity.ChatTemperature)
                    .maxTokens(chatGPTActivity.ChatMaxToken).build();
            List<CompletionChoice> choices = openAiService.createCompletion(completionRequest).getChoices();
            if(choices != null && !choices.isEmpty()){
                binding.ChatOutput.setText(choices.get(0).getText());
            }else{
                binding.ChatOutput.setText("No response from OpenAI");
            }

            //((TextView)findViewById(R.id.TextViewChatGPTOutput)).setText(openAiService.createCompletion(completionRequest).getChoices().get(0).getText());
            //Log.e("GPTTTTTT",openAiService.listModels().toString());

        }catch (Exception e){
            Log.e("GPTAPI",e.toString());
            new AlertDialog.Builder(this.getContext()).setTitle("Nani Kore!?").setMessage(e.toString()+
                    "\n#Args:\nToken: "+chatGPTActivity.ChatToken+"\n"
                    +"Temperature: "+chatGPTActivity.ChatTemperature+"\n"
                    +"MaxToken: "+chatGPTActivity.ChatMaxToken).setPositiveButton("Wakata",null).show();
        }

    }
}
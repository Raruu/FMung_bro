package com.arisu.mungexambro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;

import java.util.List;


public class ChatGPTActivityTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_gptactivitytest);

    }
    public void ChatGPTBtn(View view){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            String token = "###";
            OpenAiService openAiService = new OpenAiService(token);


            CompletionRequest completionRequest = CompletionRequest.builder().model("ada")
                    .prompt("Say this is a test").temperature(0d).maxTokens(7).build();
            List<CompletionChoice> choices = openAiService.createCompletion(completionRequest).getChoices();
            if(choices != null && !choices.isEmpty()){
                ((TextView)findViewById(R.id.TextViewChatGPTOutput)).setText(choices.get(0).getText());
            }else{
                ((TextView)findViewById(R.id.TextViewChatGPTOutput)).setText("No response from OpenAI");
            }

            //((TextView)findViewById(R.id.TextViewChatGPTOutput)).setText(openAiService.createCompletion(completionRequest).getChoices().get(0).getText());
            //Log.e("GPTTTTTT",openAiService.listModels().toString());

        }catch (Exception e){
            Log.e("GPTTTTTT",e.toString());
        }


    }
}
package com.iarpg.app.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.iarpg.app.data.AIIntegration;
import com.iarpg.app.data.Utils;
import com.iarpg.app.databinding.FragmentCharacterBinding;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterFragment extends Fragment {

    private FragmentCharacterBinding binding;
    private boolean isCharacterClassEmpty = true;
    private boolean isCharacterPromptEmpty = true;

    private String characterBackstory;

    private final String theme = "Fantasy burlesque et amusante";

    public static CharacterFragment newInstance() {
        CharacterFragment fragment = new CharacterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCharacterBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        CharacterFragment self = this;

        ExecutorService service = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());
        
        binding.textZone.setMovementMethod(new android.text.method.ScrollingMovementMethod());
        binding.generateButton.setEnabled(false);
        //binding.startButton.setEnabled(false);

        initStartButton(self);

        initGenerateButton(self, service, handler);

        initTextFieldToCheckUnemptiness(self, binding.characterPrompt, "characterPrompt");
        initTextFieldToCheckUnemptiness(self, binding.characterClass, "characterClass");


    }

    private void initGenerateButton(CharacterFragment self, ExecutorService service, Handler handler) {
        binding.generateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               binding.generateButton.setEnabled(false);
                service.execute(new Runnable() {

                   @Override
                   public void run() {
                       String characterBackstory = null;
                       binding.startButton.setEnabled(false);

                       try {
                           characterBackstory = AIIntegration.generateCharacterBackstory(binding.characterClass.getText().toString(), binding.characterPrompt.getText().toString(), self.theme);
                       } catch (IOException | JSONException e) {
                           throw new RuntimeException(e);
                       }


                       String finalCharacterBackstory = characterBackstory;
                       handler.post(new Runnable() {
                           @Override
                           public void run() {
                               if (finalCharacterBackstory != null) {
                                   binding.textZone.setText(finalCharacterBackstory);
                                   self.characterBackstory = finalCharacterBackstory;
                                   binding.startButton.setEnabled(true);
                                   binding.generateButton.setEnabled(true);
                               }
                           }
                       });
                   }


               });

               binding.textZone.setText(self.characterBackstory);
           }
       });
    }

    private void initStartButton(CharacterFragment self) {

        binding.startButton.setEnabled(false);
        binding.startButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               binding.startButton.setEnabled(false);
               binding.generateButton.setEnabled(false);

               Bundle bundle = new Bundle();

               bundle.putString("characterClass", binding.characterClass.getText().toString());
               bundle.putString("theme", self.theme);

                FragmentManager fragmentManager = getParentFragmentManager();
                GameFragment gameFragment = GameFragment.newInstance();
                gameFragment.setArguments(bundle);
                Utils.fragmentTransition(fragmentManager, gameFragment, true);
           }
        }
        );
    }

    private void initTextFieldToCheckUnemptiness(CharacterFragment self, EditText textField, String textFieldName) {
        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                switch(textFieldName) {
                    case "characterPrompt":
                        self.isCharacterPromptEmpty = s.toString().isEmpty();
                        break;
                    case "characterClass":
                        self.isCharacterClassEmpty = s.toString().isEmpty();
                        break;
                }

                self.checkGenerateButtonEnablement();
                System.out.println("text Changed, new button state " + binding.generateButton.isEnabled());
            }});
    }

    private void checkGenerateButtonEnablement() {
        boolean enableCondition = (! this.isCharacterPromptEmpty) && (! this.isCharacterClassEmpty);
        binding.generateButton.setEnabled(enableCondition);
    }
}
package com.iarpg.app.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iarpg.app.R;
import com.iarpg.app.data.Utils;
import com.iarpg.app.databinding.FragmentCharacterBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterFragment extends Fragment {

    private FragmentCharacterBinding binding;
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
        binding.generateButton.setEnabled(false);
        //binding.startButton.setEnabled(false);

        binding.startButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                GameFragment gameFragment = GameFragment.newInstance();
                Utils.fragmentTransition(fragmentManager, gameFragment);
           }
        }
        );

        binding.characterPrompt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean enableButton = !s.toString().isEmpty();
                binding.generateButton.setEnabled(enableButton);
            }
        });
    }
}
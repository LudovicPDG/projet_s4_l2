package com.iarpg.app.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iarpg.app.R;
import com.iarpg.app.data.Utils;
import com.iarpg.app.databinding.FragmentLoseBinding;
import com.iarpg.app.databinding.FragmentWinBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WinFragment extends Fragment {

    private FragmentWinBinding binding;

    public static WinFragment newInstance() {
        WinFragment fragment = new WinFragment();
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
        binding = FragmentWinBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backToMenuButton.setEnabled(true);
        binding.backToMenuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                binding.backToMenuButton.setEnabled(false);
                FragmentManager fragmentManager = getParentFragmentManager();
                WelcomeFragment welcomeFragment = WelcomeFragment.newInstance();

                Utils.fragmentTransition(fragmentManager, welcomeFragment, false);
            }
        });

    }
}
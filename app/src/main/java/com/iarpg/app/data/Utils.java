package com.iarpg.app.data;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.iarpg.app.R;
import com.iarpg.app.ui.CharacterFragment;

public class Utils {

    public static void fragmentTransition(FragmentManager fragmentManager, Fragment newFragment, boolean canGoBack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_view, newFragment);
        if (canGoBack) { fragmentTransaction.addToBackStack(null);}

        fragmentTransaction.commit();
    }
}

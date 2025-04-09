package com.iarpg.app;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.iarpg.app.R;
import com.iarpg.app.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.action.ViewActions.click;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;

public class WelcomeFragmentFunctionalTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void testStartButtonIsInitiallyDisabled() {
        onView(withId(R.id.startButton)).check(ViewAssertions.matches(not(isEnabled()))); // On vérifie que le bouton est bien désactivé
    }

    @Test
    public void testStartButtonBecomesEnabledAfterTyping() {
        onView(withId(R.id.themeText)).perform(ViewActions.typeText("Fantasy"));

        onView(withId(R.id.startButton)).check(ViewAssertions.matches(isEnabled()));
    }


}
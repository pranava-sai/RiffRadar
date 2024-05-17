package com.example.androidexample;

import android.app.UiAutomation;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import android.view.View;
import org.hamcrest.Matcher;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

public class TestUtils {

    // View action to disable animations
    public static ViewAction disableAnimations() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Disabling animations";
            }

            @Override
            public void perform(UiController uiController, View view) {
                try {
                    UiAutomation uiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
                    uiAutomation.executeShellCommand("settings put global window_animation_scale 0");
                    uiAutomation.executeShellCommand("settings put global transition_animation_scale 0");
                    uiAutomation.executeShellCommand("settings put global animator_duration_scale 0");
                } catch (Throwable e) {
                    throw new PerformException.Builder()
                            .withActionDescription(this.getDescription())
                            .withCause(e)
                            .build();
                }
            }
        };
    }
}

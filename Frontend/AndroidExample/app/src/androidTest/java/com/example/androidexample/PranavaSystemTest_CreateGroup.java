package com.example.androidexample;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

@RunWith(AndroidJUnit4.class)
public class PranavaSystemTest_CreateGroup {

    @Rule
    public ActivityScenarioRule<CreateGroupActivity> activityScenarioRule = new ActivityScenarioRule<>(CreateGroupActivity.class);

    @Test
    public void testCreateGroup1() {
        Espresso.onView(ViewMatchers.withId(R.id.groupName))
                .perform(ViewActions.typeText("COM S 309"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.groupBio))
                .perform(ViewActions.typeText("Group of Elementary Java Programmers"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.passcode))
                .perform(ViewActions.typeText("3090"),ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.createGroup))
                .perform(ViewActions.click());
    }

    @Test
    public void testCreateGroup2() {
        Espresso.onView(ViewMatchers.withId(R.id.groupName))
                .perform(ViewActions.typeText("COM S 319"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.groupBio))
                .perform(ViewActions.typeText("Group of Elementary React Programmers"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.passcode))
                .perform(ViewActions.typeText("3190"),ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.createGroup))
                .perform(ViewActions.click());
    }

    @Test
    public void testCreateGroup3() {
        Espresso.onView(ViewMatchers.withId(R.id.groupName))
                .perform(ViewActions.typeText("COM S 228"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.groupBio))
                .perform(ViewActions.typeText("Group of Kiddo DS Learners"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.passcode))
                .perform(ViewActions.typeText("2280"),ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.createGroup))
                .perform(ViewActions.click());
    }

    @Test
    public void testEmptyGroupNameCreation() {
        // Type empty group name
        Espresso.onView(withId(R.id.groupName))
                .perform(ViewActions.clearText(), ViewActions.closeSoftKeyboard());

        // Type group bio
        Espresso.onView(withId(R.id.groupBio))
                .perform(ViewActions.typeText("Testing Empty Group Bio"), ViewActions.closeSoftKeyboard());

        // Type group passcode
        Espresso.onView(withId(R.id.passcode))
                .perform(ViewActions.typeText("1111"), ViewActions.closeSoftKeyboard());

        // Click create button
        Espresso.onView(withId(R.id.createGroup))
                .perform(ViewActions.click());

        // Verify error message is displayed for empty group name
        Espresso.onView(withId(R.id.groupName))
                .check(matches(hasErrorText("Group cannot be created without a name"))); // Use appropriate matcher

        Espresso.onView(ViewMatchers.withId(R.id.groupName))
                .perform(ViewActions.typeText("Tested Empty Group Name"),ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.createGroup))
                .perform(ViewActions.click());
    }


    @Test
    public void testEmptyGroupBioCreation() {
        Espresso.onView(ViewMatchers.withId(R.id.groupName))
                .perform(ViewActions.typeText("Testing Empty Group Name"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.groupBio))
                .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.passcode))
                .perform(ViewActions.typeText("2222"),ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.createGroup))
                .perform(ViewActions.click());

        Espresso.onView(withId(R.id.groupBio))
                .check(matches(hasErrorText("Group cannot be created without a bio"))); // Use appropriate matcher

        Espresso.onView(ViewMatchers.withId(R.id.groupBio))
                .perform(ViewActions.typeText("Tested Empty Group Bio"),ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.createGroup))
                .perform(ViewActions.click());
    }

    @Test
    public void testEmptyGroupPasscodeCreation() {
        // Type group name
        Espresso.onView(withId(R.id.groupName))
                .perform(ViewActions.typeText("Testing Empty Group Name"), ViewActions.closeSoftKeyboard());

        // Type group bio
        Espresso.onView(withId(R.id.groupBio))
                .perform(ViewActions.typeText("Testing Empty Group Bio"), ViewActions.closeSoftKeyboard());

        // Type empty group passcode
        Espresso.onView(withId(R.id.passcode))
                .perform(ViewActions.clearText(), ViewActions.closeSoftKeyboard());

        // Click create button
        Espresso.onView(withId(R.id.createGroup))
                .perform(ViewActions.click());

        // Verify error message is displayed for empty group passcode
        Espresso.onView(withId(R.id.passcode))
                .check(matches(hasErrorText("Group cannot be created without a passcode"))); // Use appropriate matcher

        Espresso.onView(ViewMatchers.withId(R.id.groupBio))
                .perform(ViewActions.typeText("Tested Empty Group Bio"),ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.createGroup))
                .perform(ViewActions.click());
    }
}

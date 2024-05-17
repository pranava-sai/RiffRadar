package com.example.androidexample;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BandLoginLogoutTest {

    @Rule
    public ActivityScenarioRule<GreeterActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(GreeterActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION");

    @Test
    public void bandLoginLogoutTest() {
        onView(isRoot()).perform(TestUtils.disableAnimations());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.bandButton), withText("Band"),
                        childAtPosition(
                                allOf(withId(R.id.typeContainer),
                                        childAtPosition(
                                                withId(R.id.cardView),
                                                5)),
                                1)));
        materialTextView.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.profileButton), withContentDescription("Logout"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fanFeed),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView.perform(click());

        assert UserInfo.hasInstance();

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.menuIcon), withContentDescription("map"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.appcompat.widget.LinearLayoutCompat")),
                                        0),
                                4),
                        isDisplayed()));
        appCompatImageView2.perform(click());

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.logoutButton), withText("LOGOUT"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.dropdownMenu),
                                        3),
                                0),
                        isDisplayed()));
        materialTextView2.perform(click());

        assert !UserInfo.hasInstance();
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }


}

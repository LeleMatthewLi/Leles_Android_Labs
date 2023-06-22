package com.algonquincollege.lelesandroidlabs;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.passwordEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(withId(R.id.passwordEditText));
        appCompatEditText2.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.loginBtn));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
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

    /**
     * Test for validating password with missing uppercase character.
     * Password "password123*" is used as the test input.
     * The system should indicate that the password does not meet the requirements.
     */
    @Test
    public void testFindMissingUpperCase () {
        //find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.passwordEditText));
        //type in password123#$*
        appCompatEditText.perform(replaceText("password123*"));
        //find the button
        ViewInteraction materialButton = onView(withId(R.id.loginBtn));
        //click the button
        materialButton.perform(click());
        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView));
        //check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test for validating password with missing digit.
     * Password "Password*" is used as the test input.
     * The system should indicate that the password does not meet the requirements.
     */
    @Test
    public void testFindMissingDigit(){
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.passwordEditText));
        //type in password123#$*
        appCompatEditText.perform(replaceText ( "Password*"));
        //find the button
        ViewInteraction materialButton = onView(withId(R.id.loginBtn) );
        //click the button
        materialButton.perform(click());
        //find the text view
        ViewInteraction textView = onView( withId(R.id.textView) );
        //check the text
        textView.check (matches (withText ("You shall not pass!")));
    }

    /**
     * Test for validating password with missing lowercase character.
     * Password "P123*" is used as the test input.
     * The system should indicate that the password does not meet the requirements.
     */
    @Test
    public void testFindMissingLowerCase(){
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.passwordEditText));
        //type in password123#$*
        appCompatEditText.perform(replaceText ( "P123*"));
        //find the button
        ViewInteraction materialButton = onView(withId(R.id.loginBtn) );
        //click the button
        materialButton.perform(click());
        //find the text view
        ViewInteraction textView = onView( withId(R.id.textView) );
        //check the text
        textView.check (matches (withText ("You shall not pass!")));
    }

    /**
     * Test for validating password with missing special character.
     * Password "Password123" is used as the test input.
     * The system should indicate that the password does not meet the requirements.
     */
    @Test
    public void testFindMissingSpecial(){
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.passwordEditText));
        //type in password123#$*
        appCompatEditText.perform(replaceText ( "Password123"));
        //find the button
        ViewInteraction materialButton = onView(withId(R.id.loginBtn) );
        //click the button
        materialButton.perform(click());
        //find the text view
        ViewInteraction textView = onView( withId(R.id.textView) );
        //check the text
        textView.check (matches (withText ("You shall not pass!")));
    }

    /**
     * Test for validating password with all required elements.
     * Password "Password123*" is used as the test input.
     * The system should indicate that the password meets the requirements.
     */
    @Test
    public void testFindNoErrors(){
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.passwordEditText));
        //type in password123#$*
        appCompatEditText.perform(replaceText ( "Password123*"));
        //find the button
        ViewInteraction materialButton = onView(withId(R.id.loginBtn) );
        //click the button
        materialButton.perform(click());
        //find the text view
        ViewInteraction textView = onView( withId(R.id.textView) );
        //check the text
        textView.check (matches (withText ("Your password meets the requirements")));
    }


}

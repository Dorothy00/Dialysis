package test.dorothy.graduation.activity;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.test.ActivityInstrumentationTestCase2;

import com.reader.dialysis.activity.WelcomeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Test Case:
 * 1.Test login Button
 * 2.Test register Button
 * <p/>
 * Created by dorothy on 15/5/27.
 */
public class WelcomeEspressoTest extends ActivityInstrumentationTestCase2<WelcomeActivity> {

    public WelcomeEspressoTest() {
        super(WelcomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    public void testLoginAndRegister() {
        //1. login
        final ViewInteraction perform = onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));

        pressBack();

        //2. register
        onView(withId(R.id.register)).perform(click());
        onView(withId(R.id.email)).check(matches(isDisplayed()));
        onView(withId(R.id.confirm_password)).check(matches(isDisplayed()));
    }

}

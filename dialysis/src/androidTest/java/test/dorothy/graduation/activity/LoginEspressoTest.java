package test.dorothy.graduation.activity;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.reader.dialysis.activity.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Created by dorothy on 15/5/27.
 */
public class LoginEspressoTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    public LoginEspressoTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    public void testSuccessLogin() {
        // 1. right user info
        onView(withId(R.id.username)).perform(typeText("di00di"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("woshizhangdi"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(scrollTo(), click());
        onView(withId(R.id.tool_bar)).check(matches(isDisplayed()));
    }

    public void testFailureLogin() {
        //1. input nothing
        onView(withId(R.id.login)).check(matches(not(isEnabled())));

        //2. only input username
        onView(withId(R.id.username)).perform(typeText("di00di"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(scrollTo());
        onView(withId(R.id.login)).check(matches(not(isEnabled())));

        //2. only input password
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.password)).perform(typeText("woshizhangdi"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(scrollTo());
        onView(withId(R.id.login)).check(matches(not(isEnabled())));

        //3 input wrong username or password
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.username)).perform(typeText("di00di"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("wewewfff"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(scrollTo());
        onView(withId(R.id.login)).check(matches(isEnabled()));
        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.tool_bar)).check(doesNotExist());
    }
}

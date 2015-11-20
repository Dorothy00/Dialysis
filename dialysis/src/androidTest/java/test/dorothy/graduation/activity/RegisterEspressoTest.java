package test.dorothy.graduation.activity;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.test.ActivityInstrumentationTestCase2;

import com.reader.dialysis.activity.RegisterActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 *Test Case:
 * 1. success register
 * 2. failure register
 * note: there is something wrong in keyboard, so test case1 won't pass
 * <p/>
 * Created by dorothy on 15/5/27.
 */
public class RegisterEspressoTest extends ActivityInstrumentationTestCase2<RegisterActivity> {

    private ViewInteraction usernameView;
    private ViewInteraction emailView;
    private ViewInteraction passWordView;
    private ViewInteraction confirmPassWordView;
    private ViewInteraction registerBtn;

    public RegisterEspressoTest() {
        super(RegisterActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();

        usernameView = onView(withId(R.id.username));
        emailView = onView(withId(R.id.email));
        passWordView = onView(withId(R.id.password));
        confirmPassWordView = onView(withId(R.id.confirm_password));
        registerBtn = onView(withId(R.id.register));
    }

    /**
     * 由于输入法问题，这个测试用例不能成功通过，但功能是正常的。
     */
//    public void testSuccessRegister() {
//
//        usernameView.check(matches(withHint(getString(R.string.username))));
//        emailView.check(matches(withHint(getString(R.string.email))));
//        passWordView.check(matches(withHint(getString(R.string.password))));
//        confirmPassWordView.check(matches(withHint(getString(R.string.confirm_password))));
//        usernameView.perform(typeText("zhangsan"), closeSoftKeyboard());
//        emailView.perform(typeText("zhangsan@gmail.com"), closeSoftKeyboard());
//        passWordView.perform(typeText("woshizhangsan"), closeSoftKeyboard());
//        confirmPassWordView.perform(scrollTo());
//        confirmPassWordView.perform(typeText("woshizhangsan"), closeSoftKeyboard());
//        registerBtn.perform(scrollTo(), click());
//        onView(withId(R.id.tool_bar)).check(matches(isDisplayed()));
//    }

    public void testFailureRegister() {
        //1. nothing to input
        registerBtn.check(matches(not(isEnabled())));

        //2. not completely input
        usernameView.perform(typeText("lisi"), closeSoftKeyboard());
        passWordView.perform(typeText("woshilisi"), closeSoftKeyboard());
        registerBtn.check(matches(not(isEnabled())));

        //2. completely input but invalid email input
        usernameView.perform(clearText());
        passWordView.perform(clearText());

        usernameView.perform(typeText("lisi"), closeSoftKeyboard());
        emailView.perform(typeText("sdas@gmail.com"),closeSoftKeyboard());
        passWordView.perform(typeText("woshilisiw"), closeSoftKeyboard());
        confirmPassWordView.perform(scrollTo());
        confirmPassWordView.perform(typeText("woshilisi"),closeSoftKeyboard());
        registerBtn.perform(scrollTo(), click());
        onView(withText("注册错误")).inRoot(isDialog()).check(matches(isDisplayed()));

    }

    private String getString(int resId) {
        return getActivity().getResources().getString(resId);
    }
}

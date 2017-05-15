package ep1.joaofran.com.ep1;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by joaofran on 15/05/17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityBehaviorTest {

    private static class LoginBox {
        String NUSP;
        String PASS;
        boolean is_student;

        public LoginBox (String NUSP, String PASS, boolean is_student) {
            this.NUSP = NUSP;
            this.PASS = PASS;
            this.is_student = is_student;
        }
    }

    private LoginBox[] valids = new LoginBox[2];
    private LoginBox[] invalids = new LoginBox[2];

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public IntentsTestRule<ProfileActivity> profileActivityIntentsTestRule =
            new IntentsTestRule<>(ProfileActivity.class);


    @Before
    public void initStrings() {
        valids[0] = new LoginBox("7895656","123456",true);
        valids[1] = new LoginBox("7895656","123456",false);

        invalids[0] = new LoginBox("7895656","654321",true);
        invalids[1] = new LoginBox("7895656","654321",false);
    }


    private void fillLogin(LoginBox login) {
        onView(withId(R.id.LetNUSP))
                .perform(typeText(login.NUSP));
        onView(withId(R.id.LetPassword))
                .perform(typeText(login.PASS));
        onView(withId(login.is_student ? R.id.LrbStudent : R.id.LrbTeacher)).perform(click());

        onView(withId(R.id.LbtnLogin)).perform(click());
    }

    @Test
    public void loginSuccess () {
        int i = (int) Math.random() * 2;
        LoginBox login = valids[i];

        fillLogin(login);

        intended(hasComponent(ProfileActivity.class.getName()));
    }

    @Test
    public void loginFail () {
        int i = (int) Math.random() * 2;
        LoginBox login = invalids[i];

        fillLogin(login);
    }
}

package ep1.joaofran.com.ep1;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
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


    @Before
    public void initStrings() {
        valids[0] = new LoginBox("7895656","123456",true);
        valids[1] = new LoginBox("7895656","123456",false);

        invalids[0] = new LoginBox("7895656","654321",true);
        invalids[1] = new LoginBox("7895656","654321",false);
    }

    @Test
    public void fillLogin_sameActivity () {
        onView(withId(R.id.LetNUSP))
                .perform(typeText(valids[0].NUSP));
        onView(withId(R.id.LetPassword))
                .perform(typeText(valids[0].PASS));
        onView(withId(valids[0].is_student ? R.id.LrbStudent : R.id.LrbTeacher)).perform(click());
    }
}

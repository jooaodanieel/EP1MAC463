package ep1.joaofran.com.ep1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;


/**
 * Created by joaofran on 15/05/17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BehaviorTest {

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

    private static class SignupBox {
        String NAME;
        String NUSP;
        String PASS;
        boolean is_student;

        public SignupBox (String NAME, String NUSP, String PASS, boolean is_student) {
            this.NAME = NAME;
            this.NUSP = NUSP;
            this.PASS = PASS;
            this.is_student = is_student;
        }
    }

    private LoginBox[] validsLogin = new LoginBox[2];
    private LoginBox[] invalidsLogin = new LoginBox[2];
    private SignupBox[] validsSignup = new SignupBox[2];
    private SignupBox[] invalidsSignup = new SignupBox[2];

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public IntentsTestRule<ProfileActivity> profileActivityIntentsTestRule =
            new IntentsTestRule<>(ProfileActivity.class);

    @Before
    public void init() {
        Activity activity = loginActivityActivityTestRule.getActivity();
        String sp_name = activity.getString(R.string.shared_preferences_file);
        SharedPreferences.Editor sp_editor = activity.getSharedPreferences(sp_name,Context.MODE_PRIVATE).edit();
        sp_editor.clear();
        sp_editor.commit();

        validsLogin[0] = new LoginBox("7895656","123456",true);
        validsLogin[1] = new LoginBox("7895656","123456",false);

        invalidsLogin[0] = new LoginBox("7895656","654321",true);
        invalidsLogin[1] = new LoginBox("7895656","654321",false);

        validsSignup[0] = new SignupBox("HARDCODED NAME ONE",
                String.valueOf((int) (1000000 + Math.random() * 8999999)),
                "123456",true);
        validsSignup[1] = new SignupBox("HARDCODED NAME TWO",
                String.valueOf((int) (1000000 + Math.random() * 8999999)),
                "123456",false);

        invalidsSignup[0] = new SignupBox("HARDCODED NAME THREE","7578279","123456",true);
        invalidsSignup[1] = new SignupBox("HARDCODED NAME FOUR","7578279","123456",false);
    }

    private void fillLogin(LoginBox login) {
        onView(withId(R.id.LetNUSP))
                .perform(typeText(login.NUSP));
        onView(withId(R.id.LetPassword))
                .perform(typeText(login.PASS));
        onView(withId(login.is_student ? R.id.LrbStudent : R.id.LrbTeacher)).perform(click());

        onView(withId(R.id.LbtnLogin)).perform(click());
    }

    private void checkToast(int ToastStringId, ActivityTestRule rule) {
        onView(withText(ToastStringId))
                .inRoot(withDecorView(not(
                        rule.getActivity().getWindow().getDecorView())
                ))
                .check(matches(isDisplayed()));
    }

    private void fillSignup (SignupBox signupBox) {
        onView(withId(R.id.SetInsertName))
                .perform(typeText(signupBox.NAME), closeSoftKeyboard());
        onView(withId(R.id.SetInsertNUSP))
                .perform(typeText(signupBox.NUSP), closeSoftKeyboard());
        onView(withId(R.id.SetInsertPassword))
                .perform(typeText(signupBox.PASS), closeSoftKeyboard());
        onView(withId(signupBox.is_student ? R.id.SrbStudent : R.id.SrbTeacher))
                .perform(click());

        onView(withId(R.id.SbtnSignUp))
                .perform(click());
    }

    @Test
    public void loginFail () {
        int i = (int) Math.random() * 2;
        LoginBox login = invalidsLogin[i];

        fillLogin(login);

        checkToast(R.string.login_fail, loginActivityActivityTestRule);
    }

    @Test
    public void loginSuccess () {
        int i = (int) Math.random() * 2;
        LoginBox login = validsLogin[i];

        fillLogin(login);

        intended(hasComponent(ProfileActivity.class.getName()));
    }

    @Test
    public void signupFail_fromLogin () {
        // click para mudar de activity
        onView(withId(R.id.LtvLinkSignUp))
                .perform(click());

        // changed to SignUpActivity
        int i = (int) Math.random() * 2;
        fillSignup(invalidsSignup[i]);

        checkToast(R.string.signup_fail, profileActivityIntentsTestRule);

    }

    /**
     * É importante ressaltar neste teste que um SignUp de sucesso
     * significa que o número USP utilizado ainda não constava no
     * banco de dados do Web Service. Para poder haver uma sequência
     * automática de testes, há um gerador de números aleatórios no
     * intervalo [1000000,9999999]. A possibilidade de pegar um número
     * já cadastrado é inicialmente pequena, porém existente, e tende
     * a aumentar à medida em que são feitos mais testes.
     */
    @Test
    public void signupSuccess_fromLogin () {
        // click para mudar de activity
        onView(withId(R.id.LtvLinkSignUp))
                .perform(click());

        int i = (int) Math.random() * 2;
        fillSignup(validsSignup[i]);

        intended(hasComponent(ProfileActivity.class.getName()));
    }
}

package lib;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.toolbox.StringRequest;

import ep1.joaofran.com.ep1.SeminarActivity;

/**
 * Created by vwraposo on 22/04/17.
 */

public class User {

    public static final String ID = "USER_ID";
    public static final String TYPE = "USER_TYPE";

    private String NUSP;
    private Boolean is_student;
    private String name;

    public static final String INTENT_KEY = "user_intent";

    public User (String NUSP, Boolean is_student) {
        this.NUSP = NUSP;
        this.is_student = is_student;
    }

    public String getId() {
        return NUSP;
    }

    public Boolean isStudent() {
        return is_student;
    }

    public void setName (String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return this.name;
    }

}

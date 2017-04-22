package lib;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vwraposo on 22/04/17.
 */

public class User {
    private String name;
    private String NUSP;
    private String password;
    private Boolean student;

    public static final String INTENT_KEY = "user_intent";

    public User (String name, String NUSP, String password, Boolean student) {
        this.name = name;
        this.NUSP = NUSP;
        this.password = password;
        this.student = student;
    }

    public String getName() {
        return this.name;
    }

    public String getNUSP() {
        return this.NUSP;
    }

    public String getPassword() {
        return this.password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNUSP(String NUSP) {
        this.NUSP = NUSP;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isStudent() {
       return this.student;
    }
}

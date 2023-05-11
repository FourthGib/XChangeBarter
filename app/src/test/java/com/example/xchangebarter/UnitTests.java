package com.example.xchangebarter;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {

    @Test
    public void emailAuthenticationTest(){
        String email = "test000@csusm.edu";
        String userName = "default";
        // extracted code from RegisterActivity
        Pattern p = Pattern.compile("\\w+\\d{3}@csusm.edu");
        Matcher m = p.matcher(email);

        if (m.matches()) {
            // take @csusm.edu off email string to allow firebase to add
            int atIndex = email.indexOf('@');
            userName = email.substring(0, atIndex);
        }
        assertEquals(userName, "test000");
    }
}
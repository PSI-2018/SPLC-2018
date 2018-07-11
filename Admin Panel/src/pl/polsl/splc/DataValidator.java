package pl.polsl.splc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidator {
    public static boolean isValidEmail(String email) {
        Pattern p = Pattern.compile("^.+@.+(\\.[^\\.]+)+$");
        Matcher m = p.matcher(email);
        return m.matches();
    }
}

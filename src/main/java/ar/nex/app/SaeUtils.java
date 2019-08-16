package ar.nex.app;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class SaeUtils {

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public static String formatDateTimeString(Date date) {
        return DATE_TIME_FORMAT.format(date);
    }

    public static String formatDateTimeString(Long time) {
        return DATE_TIME_FORMAT.format(new Date(time));
    }

    public static String getDateString(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        try {
            return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    public static Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public static Integer getEdad(Date date) { 
        LocalDate ahora = LocalDate.now();
        Period periodo = Period.between(convertToLocalDateViaSqlDate(date), ahora);
        return periodo.getYears();
    }

    public static boolean validateEmailAddress(String emailID) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(emailID).matches();
    }

    public static String capitailizeString(String str) {
// Create a char array of given String 
        char ch[] = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {

            // If first character of a word is found 
            if (i == 0 && ch[i] != ' '
                    || ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case 
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case 
                    ch[i] = (char) (ch[i] - 'a' + 'A');
                }
            } // If apart from first character 
            // Any one is in Upper-case 
            else if (ch[i] >= 'A' && ch[i] <= 'Z') // Convert into Lower-Case 
            {
                ch[i] = (char) (ch[i] + 'a' - 'A');
            }
        }

        // Convert the char array to equivalent String 
        String st = new String(ch);
        return st.trim();
    }
}

package ar.nex.equipo.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author
 * https://www.mkyong.com/regular-expressions/how-to-validate-date-with-regular-expression/
 */
public class DateUtils {

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat DATE_FORMAT_Y = new SimpleDateFormat("yyyy-mm-dd");

    public static String formatDateTimeString(Date date) {
        return DATE_TIME_FORMAT.format(date);
    }

    public static String formatDateTimeString(Long time) {
        return DATE_TIME_FORMAT.format(new Date(time));
    }

    /**
     * 
     * @param date
     * @return DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
     */
    public static String getDateString(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * 
     * @param date
     * @return DATE_FORMAT_Y = new SimpleDateFormat("yyyy-mm-dd");
     */
    public static String getDateYString(Date date) {
        return DATE_FORMAT_Y.format(date);
    }

    private static Pattern pattern;
    private static Matcher matcher;

    //Date Format (dd/mm/yyyy) Regular Expression Pattern
    private static final String DATE_PATTERN
            = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

    public DateUtils() {
        //pattern = Pattern.compile(DATE_PATTERN);        
    }
   public static Integer getEdad(Date date) { 
        LocalDate ahora = LocalDate.now();
        Period periodo = Period.between(convertToLocalDateViaSqlDate(date), ahora);
        return periodo.getYears();
    }
   
    /**
     * Validate date format with regular expression
     *
     * @param localdate date address for validation
     * @return true valid date fromat, false invalid date format
     */
    public static boolean validate(LocalDate localdate) {
        if (localdate != null) {
            return validate(getDateString(convertToDateViaSqlDate(localdate)));
        } else {
            return false;
        }
    }

    /**
     * Validate date format with regular expression
     *
     * @param date date address for validation
     * @return true valid date fromat, false invalid date format
     */
    public static boolean validate(String date) {

        System.out.println("ar.nex.util.DateValidator.validate(): " + date);

        pattern = Pattern.compile(DATE_PATTERN);
        matcher = pattern.matcher(date);

        if (matcher.matches()) {
            matcher.reset();

            if (matcher.find()) {

                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (year <= 2018) {
                    return false;
                }
                if (day.equals("31")
                        && (month.equals("4") || month.equals("6") || month.equals("9")
                        || month.equals("11") || month.equals("04") || month.equals("06")
                        || month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if (year % 4 == 0) {
                        if (day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        if (day.equals("29") || day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

//    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
//        return dateToConvert.toInstant()
//                .atZone(ZoneId.systemDefault())
//                .toLocalDate();
//    }
    public static LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        try {
            return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
        } catch (Exception e) {
            return  LocalDate.now();
        }        
    }

    public static Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

//    public Date convertToDateViaInstant(LocalDateTime dateToConvert) {
//        return java.util.Date
//                .from(dateToConvert.atZone(ZoneId.systemDefault())
//                        .toInstant());
//    }
    public boolean compareDateToLocalDate(Date date, LocalDate localDate) {
        String dt = convertToLocalDateViaSqlDate(date).toString();
        String ld = convertToDateViaSqlDate(localDate).toString();
        return dt.contentEquals(ld);
    }

}

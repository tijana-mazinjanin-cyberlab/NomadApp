package util;

import model.DateRange;
import model.enums.UserType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Helper {
    public static boolean isEmailPatternValid(String emailAddress) {
        return Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
                .matcher(emailAddress)
                .matches();
    }
    public static boolean isPasswordValid(String password) {
//        return Pattern.compile("^(?=.*[0-9])"
//                        + "(?=.*[a-z])(?=.*[A-Z])"
//                        + "(?=.*[@#$%^&+=])"
//                        + "(?=\\S+$).{8,20}$")
//                .matcher(password)
//                .matches();
        return password.length() > 7;
    }
        public static boolean isStringValid(String str) {
            return str.length() > 1;
        }

        public static DateRange getStartEndDates(int year){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);

            // Get the start date of the year
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date startDate = calendar.getTime();

            // Get the end date of the year
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            Date endDate = calendar.getTime();
            return new DateRange(startDate, endDate);
        }
        public static Date setMiliseconds(Date date){
            Calendar c = Calendar.getInstance();
            c.set(Helper.getYear(date), Helper.getMonth(date)-1, Helper.getDay(date), 0, 0,0); // Month is 0-based in Calendar
            c.set(Calendar.MILLISECOND, 0);
            return c.getTime();
        }
        public static int getMonth(Date date){
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return localDate.getMonthValue();
        }
        public static int getDay(Date date){
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return localDate.getDayOfMonth();
        }
        public static int getYear(Date date){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            return Integer.parseInt(dateFormat.format(date));
        }
        public static String dateToString(Date date){
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            return dateFormat.format(date);
        }
}

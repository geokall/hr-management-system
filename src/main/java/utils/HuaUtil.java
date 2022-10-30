package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HuaUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuaUtil.class);

    public static final String HUA_EMAIL = "@hua.gr";
    public static final String DAY_MONTH_YEAR_PATTERN = "dd/MM/yyyy";

    public static String generateEmail(String username) {
        return username.concat(HUA_EMAIL);
    }

    public static String formatDateToString(Date date) {
        Format formatter = new SimpleDateFormat(DAY_MONTH_YEAR_PATTERN);

        return formatter.format(date);
    }

    public static Date formatStringToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DAY_MONTH_YEAR_PATTERN);
        Date yearMonthDay = null;

        try {
            yearMonthDay = formatter.parse(date);
        } catch (ParseException e) {
            LOGGER.info(e.getMessage());
        }

        return yearMonthDay;
    }

}

package utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class HuaUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuaUtil.class);

    public static final String HUA_EMAIL = "@hua.gr";
    public static final String DAY_MONTH_YEAR_PATTERN = "dd/MM/yyyy";
    public static final String AT_SIGN = "@";
    public static final String HUA_PREFIX = "hua";
    public static final String HYPHEN = "-";
    public static final String SPACE = " ";
    public static final String BREAK_LINE = "<br>";
    public static final String OPEN_BOLD = "<b>";
    public static final String CLOSE_BOLD = "</b>";
    public static final String OPEN_LINK = "<a href=";
    public static final String CLOSE_LINK = "</a>";

    public static final String YEAR_MONTH_DAY_PATTERN = "yyyy-MM-dd";

    public static String generateEmailBy(String username) {
        return username.concat(HUA_EMAIL);
    }

    public static String generateUsernameBy(String email) {
        String[] split = email.split(AT_SIGN);

        return split[0];
    }

    public static String formatDateToString(Date date) {
        if (ObjectUtils.isNotEmpty(date)) {
            Format formatter = new SimpleDateFormat(DAY_MONTH_YEAR_PATTERN);

            return formatter.format(date);
        }

        return null;
    }

    public static Date formatStringToDate(String date) {
        if (ObjectUtils.isNotEmpty(date)) {
            SimpleDateFormat formatter = new SimpleDateFormat(DAY_MONTH_YEAR_PATTERN);
            Date yearMonthDay = null;

            try {
                yearMonthDay = formatter.parse(date);
            } catch (ParseException e) {
                LOGGER.info(e.getMessage());
            }

            return yearMonthDay;
        }

        return null;
    }

    public static String generateRandomPasswordBy() {
        String randomString = RandomStringUtils.randomAlphabetic(10);

        return HUA_PREFIX.concat(HYPHEN).concat(randomString);
    }

    public static LocalDate toLocalDateBy(Date date) {
        if (ObjectUtils.isNotEmpty(date)) {
            return LocalDate.parse(new SimpleDateFormat(YEAR_MONTH_DAY_PATTERN)
                    .format(date));
        }

        return null;
    }

}

package utils;

public class HuaUtil {

    public static final String HUA_EMAIL = "@hua.gr";

    public static String generateEmail(String username) {
        return username.concat(HUA_EMAIL);
    }

}

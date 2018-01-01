package com.instadownloader.instadpdownloader.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ayush on 23/1/17.
 */

public class AppUtils {
    public static String filterUserName(String userName)    {

        Pattern r = Pattern.compile(Constants.REGEX_FOR_FILTER_USERNAME_JAVA);

        // Now create matcher object.
        Matcher m = r.matcher(userName);
        if (m.find( )) {
            return m.group(m.groupCount());
        }
        return null;
    }

    public static boolean isInstaData(String url){
        Pattern r = Pattern.compile(Constants.REGEX_FOR_FILTER_USERNAME_JAVA);

        Matcher m = r.matcher(url);
        return m.find();
    }

}

package com.jeremy.tripcord.common.utils;

import java.util.List;

/**
 * Created by asura1983 on 2014. 7. 7..
 */
public class StringUtil {

    public static boolean isEmpty(String value) {

        if (value == null || value.trim().equals("")) {
            return true;
        }

        return false;
    }

    public static String convertStringArrayToString(List<String> stringList, String seperator) {

        String value = "";
        for (int i = 0; i < stringList.size(); i++) {

            String selectedItemValue = stringList.get(i);

            if (i != 0) {
                value += seperator + " ";
            }

            value += selectedItemValue;
        }

        return value;
    }

}

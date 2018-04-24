package dg.util;

import dg.model.DailyS11;

import java.util.ArrayList;

/**
 * convert S11 object into to a string
 *
 */
public class DailyS11ToStringUtil {
    public static String dailyS11ToString(ArrayList<DailyS11> dailyS11_list) {
        String result = "";
        for(int i = 0; i < dailyS11_list.size(); i++) {
            result += dailyS11_list.get(i).convertToString();
        }

        return result;
    }
}

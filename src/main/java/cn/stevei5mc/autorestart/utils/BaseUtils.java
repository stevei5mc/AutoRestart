package cn.stevei5mc.autorestart.utils;

import cn.stevei5mc.autorestart.tasks.RestartTask;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.nukkit.Player;
import cn.nukkit.Server;
//import java.util.*;

public class BaseUtils {
    private static AutoRestartPlugin main = AutoRestartPlugin.getInstance();

    /**
     * 获取在重启前的开始提示的时间
     * @return 开始提示的时间
    */
    public static int getRestartTipTime() {
        int time = 30;
        int i = main.getConfig().getInt("tips_time", 30);
        if (time != 0) {
            time = i;
        }
        return time;
    }

    /**
     * 获取在重启服务器需要的时间
     * @return 重启服务器需要的时间
    */
    public static int getRestartUseTime() {
        int time = 2;
        int i = main.getConfig().getInt("restart_time", 2);
        if (i != 0) {
            time = i;
        }
        return time;
    }

    /**
     * 获取剩余时间
     * @param player player
     * @return remainder
    */
    public static String getRemainder(Player player) {
        int time = RestartTask.time2;
        int hours = time / 3600;
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;
        String hourUnit = main.getLang(player).translateString("time_unit_hour");
        String minuteUnit = main.getLang(player).translateString("time_unit_minutes");
        String secondUnit = main.getLang(player).translateString("time_unit_seconds");
        String timee = "";
        if (hours > 0) {
            timee = String.valueOf(hours) + hourUnit + String.valueOf(minutes) + minuteUnit + String.valueOf(seconds) + secondUnit;
        } else if (minutes > 0) {
            timee = String.valueOf(minutes) + minuteUnit + String.valueOf(seconds) + secondUnit;
        } else {
            timee = String.valueOf(seconds) + secondUnit;
        }
        String remainder = main.getLang(player).translateString("variable_remainder",timee);
        return remainder;
    }
}
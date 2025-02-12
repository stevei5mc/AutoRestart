package cn.stevei5mc.autorestart.utils;

import cn.nukkit.Player;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.tasks.RestartTask;

public class BaseUtils {
    private static AutoRestartPlugin main = AutoRestartPlugin.getInstance();

    /**
     * 获取在重启前的开始提示的时间
     * @return 开始提示的时间
    */
    public static int getRestartTipTime() {
        int time = 30;
        int i = main.getConfig().getInt("tips_time", 30);
        if (i > 0) {
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
        if (i > 0) {
            time = i;
        }
        return time;
    }

    /**
     * 获取剩余时间
     * @param player 传入player参数以实现多语言
     * @return remainder
    */
    public static String getRemainder(Player player) {
        String hourUnit = main.getLang(player).translateString("time_unit_hour");
        String minuteUnit = main.getLang(player).translateString("time_unit_minutes");
        String secondUnit = main.getLang(player).translateString("time_unit_seconds");
        if (TasksUtils.getRestartTaskState() >= 1 && TasksUtils.getRestartTaskType() != 3) {
            int time = RestartTask.getTime();
            int hours = time / 3600;
            int minutes = (time % 3600) / 60;
            int seconds = time % 60;
            String timee = "";
            if (hours > 0) {
                timee = hours + hourUnit + minutes + minuteUnit + seconds + secondUnit;
            } else if (minutes > 0) {
                timee = minutes + minuteUnit + seconds + secondUnit;
            } else {
                timee = seconds + secondUnit;
            }
            return main.getLang(player).translateString("variable_remainder",timee);
        }else {
            return "--"+secondUnit;
        }
    }

    /**
     * 获取重启任务名
     * @param player 传入player参数以实现多语言
     * @return Task name
    */
    public static String getRestartTaskName(Player player) {
        switch (TasksUtils.getRestartTaskType()) {
            case 1:
                return main.getLang(player).translateString("restart_task_type_auto");
            case 2:
                return main.getLang(player).translateString("restart_task_type_manual_restart");
            case 3:
                return main.getLang(player).translateString("restart_task_type_no_player");
            case 4:
                return main.getLang(player).translateString("restart_task_type_vote");
            case 5:
                return main.getLang(player).translateString("restart_task_type_time");
            default:
                return "§cUnknown type§r";
        }
    }
}
package cn.stevei5mc.autorestart;

import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.stevei5mc.autorestart.tasks.RestartTask;
import cn.nukkit.scheduler.TaskHandler;
import cn.stevei5mc.autorestart.tasks.VoteTask;
//import java.util.*;

public class Utils {
    private static AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    public static boolean taskState = false;//任务状态，默认为 false
    public static boolean voteTaskState = false;
    public static int taskType = 0;//任务类型，默认编号为 0
    private static int taskId;
    private static int voteTaskId;

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
     * 运行重启任务
     * @param type 重启任务类型
    */
    public static void runRestartTask(int type) {
        runRestartTask(0,type);
    }

    /**
     * 运行重启任务
     * @param restartTime 重启需要的时间
     * @param type 重启任务类型
    */
    public static void runRestartTask(int restartTime,int type) {
        cancelTask();//不管重启任务在不在运行都取消一遍再运行任务，以防出现一些奇怪的问题
        int runTick = 20;
        int time = 30;
        String unit = "time_unit_seconds";
        switch (type) {
            case 1:
                time = restartTime * 60;
                runTick = 20;
                unit = "time_unit_minutes";
                break;
            case 2:
                time = restartTime;
                runTick = 20;
                unit = "time_unit_seconds";
                break;
            case 3:
                time = 0;
                runTick = 100;
                break;
            default:
                time = 30;
                runTick = 20;
                break;
        }       
        TaskHandler taskHandler = main.getServer().getScheduler().scheduleRepeatingTask(main, new RestartTask(time), runTick, true);
        taskId = taskHandler.getTaskId();
        taskState = true;
        taskType = type;
        if (type <= 2) { 
            main.getLogger().info((main.getLang().translateString("restart_task_restart", restartTime, main.getLang().translateString(unit))));
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                player.sendMessage(main.getLang(player).translateString("restart_task_restart",restartTime, main.getLang(player).translateString(unit)));
            }
        }
    }

    public static void cancelTask() {
        main.getServer().getScheduler().cancelTask(taskId);
        taskState = false;
        taskType = 0;//重置任务编号
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

    public static void runVoteTask(int time) {
        TaskHandler taskHandler = main.getServer().getScheduler().scheduleRepeatingTask(main, new VoteTask(time), 20, true);
        voteTaskId = taskHandler.getTaskId();
        voteTaskState = true;
    }

    public static void cancelVoteTask() {
        main.getServer().getScheduler().cancelTask(voteTaskId);
        voteTaskState = false;
        for (Player player : main.getServer().getOnlinePlayers().values()) {
            player.sendMessage(main.getLang(player).translateString("vote_restart_msg_failed_veto"));
        }
    }
}
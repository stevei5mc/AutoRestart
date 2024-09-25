package cn.stevei5mc.autorestart.utils;

import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.stevei5mc.autorestart.tasks.RestartTask;
import cn.nukkit.scheduler.TaskHandler;
import cn.stevei5mc.autorestart.tasks.VoteTask;

public class TasksUtils {
    private static AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    public static boolean restartTaskState = false;//任务状态，默认为 false
    public static boolean voteTaskState = false;
    public static int restartTaskType = 0;//任务类型，默认编号为 0
    private static int restartTaskId;
    private static int voteTaskId;

    /**
     * 运行重启任务
     * @param taskType 任务类型
     * @param timeUnit 时间类型
    */
    public static void runRestartTask(int taskType,int timeUnit) {
        runRestartTask(0,taskType,timeUnit);
    }

    /**
     * 运行重启任务
     * @param restartTime 重启需要的时间
     * @param taskType 任务类型
     * @param timeUnit 时间类型
    */
    public static void runRestartTask(int restartTime,int taskType,int timeUnit) {
        cancelRestartTask();//不管重启任务在不在运行都取消一遍再运行任务，以防出现一些奇怪的问题
        int runTick = 20;
        int time = 30;
        String unit = "time_unit_seconds";
        switch (taskType) {
            case 1:
                runTick = 20;
                restartTaskType = 1;
                break;
            case 2:
                runTick = 20;
                restartTaskType = 2;
                break;
             case 3:
                runTick = 100;
                restartTaskType = 3;
                break;
            default:
                runTick = 20;
                restartTaskType = 0;
                break;
        }
        switch (timeUnit) {
            case 1:
                time = restartTime * 60;
                unit = "time_unit_minutes";
                break;
            case 2:
                time = restartTime;
                unit = "time_unit_seconds";
                break;
/*             case 3:
                time = 0;
                runTick = 100;
                break; */
            default:
                time = 30;
                break;
        }
        TaskHandler taskHandler = main.getServer().getScheduler().scheduleRepeatingTask(main, new RestartTask(time), runTick, true);
        restartTaskId = taskHandler.getTaskId();
        restartTaskState = true;
        if (taskType <= 2) { 
            main.getLogger().info((main.getLang().translateString("restart_task_restart", restartTime, main.getLang().translateString(unit))));
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                player.sendMessage(main.getLang(player).translateString("restart_task_restart",restartTime, main.getLang(player).translateString(unit)));
            }
        }
    }

    public static void cancelRestartTask() {
        main.getServer().getScheduler().cancelTask(restartTaskId);
        restartTaskState = false;
        restartTaskType = 0;//重置任务编号
    }

    /**
     * 运行投票任务
     * @param time 投票任务的时间
     * @param vote 投票的发起者
    */
    public static void runVoteTask(int time,String vote) {
        TaskHandler taskHandler = main.getServer().getScheduler().scheduleRepeatingTask(main, new VoteTask(time,vote), 20, true);
        voteTaskId = taskHandler.getTaskId();
        voteTaskState = true;
    }

    public static void cancelVoteTask() {
        main.getServer().getScheduler().cancelTask(voteTaskId);
        voteTaskState = false;
    }
}
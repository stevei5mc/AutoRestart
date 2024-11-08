package cn.stevei5mc.autorestart.utils;

import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.stevei5mc.autorestart.tasks.RestartTask;
import cn.nukkit.scheduler.TaskHandler;
import cn.stevei5mc.autorestart.tasks.VoteTask;

public class TasksUtils {
    private static AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    public static int restartTaskState = 0;//任务状态，默认为 0
    public static boolean voteTaskState = false;
    public static int restartTaskType = 0;//任务类型，默认编号为 0
    private static int restartTaskId;
    private static int voteTaskId;

    /**
     * 运行重启任务
     * @param taskType 任务类型
    */
    public static void runRestartTask(int taskType) {
        runRestartTask(0,taskType,0);
    }

    /**
     * 运行重启任务
     * @param restartTime 重启需要的时间
     * @param taskType 任务类型
     * @param timeUnit 时间类型
    */
    public static void runRestartTask(int restartTime,int taskType,int timeUnit) {
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
            // case 3:
            //     time = 0;
            //     runTick = 100;
            //     break;
            default:
                time = 30;
                break;
        }
        main.getLogger().info("task type = "+taskType+"time unit = "+timeUnit+"restart Task Type = "+restartTaskType);
        if (taskType <= 2 && restartTaskState != 2) { 
            main.getLogger().info(main.msgPrefix +(main.getLang().translateString("restart_task_restart", restartTime, main.getLang().translateString(unit))));
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                player.sendMessage(main.msgPrefix +main.getLang(player).translateString("restart_task_restart",restartTime, main.getLang(player).translateString(unit)));
            }
            // cancelRestartTask(); //这里取消掉现有的重启任务，再运行新的重启任务以免出现奇怪的问题
        }
        TaskHandler taskHandler = main.getServer().getScheduler().scheduleRepeatingTask(main, new RestartTask(time), runTick, true);
        restartTaskId = taskHandler.getTaskId();
        restartTaskState = 1;
    }

    public static void cancelRestartTask() {
        main.getServer().getScheduler().cancelTask(restartTaskId);
        restartTaskState = 0;
        restartTaskType = 0;//重置任务编号
    }

    /**
     * 运行投票任务
     * @param voter 投票的发起者
    */
    public static void runVoteTask(Player voter) {
        int startPlayer = main.getConfig().getInt("vote_start_player",3);
        int voteTime = main.getConfig().getInt("vote_time",5);
        //这里为了防止有人把时间设置为0或>5
        if (voteTime == 0 || voteTime > 5) {
            voteTime = 5;
        }
        //这里写死最低发起投票人数为3，在配置文件中定义低于3也会按照3来判断
        if (startPlayer < 3) {
            startPlayer = 3;
        }
        int time = voteTime * 60;
        String player = voter.getName();
        boolean normalCondition = Server.getInstance().getOnlinePlayers().size() >= startPlayer && time < RestartTask.time2 && restartTaskState != 2;
        boolean debugCondition = main.getConfig().getBoolean("debug",false) && voter.hasPermission("autorestart.admin.vote.force");
        if (normalCondition || debugCondition) {
            TaskHandler taskHandler = main.getServer().getScheduler().scheduleRepeatingTask(main, new VoteTask(time,voter), 20, true);
            voteTaskId = taskHandler.getTaskId();
            voteTaskState = true;
        } else {
            voter.sendMessage(main.msgPrefix +main.getLang(voter).translateString("vote_restart_msg_not_initiate"));
        }
    }

    public static void cancelVoteTask() {
        main.getServer().getScheduler().cancelTask(voteTaskId);
        voteTaskState = false;
    }

    //这里我并没有找到Nukkit有提供暂停任务的方法，所以我自己实现了一个方法
    public static void pauseRestartTask() {
        restartTaskState = 2;
        cancelVoteTask(); //取消掉投票重启的任务防止因投票重启而导致服务器重启
        main.getServer().getScheduler().cancelTask(restartTaskId);
    }

    public static void continueRunRestartTask() {
        runRestartTask(RestartTask.time2,restartTaskType,2);
    }
}
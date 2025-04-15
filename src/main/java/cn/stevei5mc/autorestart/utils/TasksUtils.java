package cn.stevei5mc.autorestart.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.TaskHandler;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.tasks.RestartTask;
import cn.stevei5mc.autorestart.tasks.VoteTask;

public class TasksUtils {
    private static final AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    private static int restartTaskState = 0;//任务状态，默认为 0
    private static boolean voteTaskState = false;
    private static int restartTaskType = 0;//任务类型，默认编号为 0
    private static int restartTaskId;
    private static int voteTaskId;
    private static boolean autoTaskState = false;

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
        if(taskType == 1 && autoTaskState) {
            throw new IllegalArgumentException();
        }else {
            if (taskType == 1) {
                autoTaskState = true;
            }
            cancelRestartTask(); //这里取消掉现有的重启任务，再运行新的重启任务以免出现奇怪的问题
            int runTick = 20;
            int time;
            restartTaskType = taskType;
            String unit = "time_unit_seconds";
            if (getRestartTaskType() == 3) {
                runTick = 100;
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
                case 3:
                    time = restartTime * 3600;
                    unit = "time_unit_hour";
                    break;
                default:
                    time = 30;
            }
            if (taskType != 3) {
                main.getLogger().info(main.getMessagePrefix() +(main.getLang().translateString("restart_task_restart", restartTime, main.getLang().translateString(unit))));
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    player.sendMessage(main.getMessagePrefix() +main.getLang(player).translateString("restart_task_restart",restartTime, main.getLang(player).translateString(unit)));
                }
            }
            TaskHandler taskHandler = main.getServer().getScheduler().scheduleRepeatingTask(main, new RestartTask(time), runTick, true);
            restartTaskId = taskHandler.getTaskId();
            restartTaskState = 1;
        }
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

        boolean normalCondition = !voteTaskState && Server.getInstance().getOnlinePlayers().size() >= startPlayer && time < RestartTask.getTime() && getRestartTaskType() != 2;
        boolean debugCondition = !voteTaskState && main.getConfig().getBoolean("debug",false) && voter.hasPermission("autorestart.admin.vote.force");
        if (normalCondition || debugCondition) {
            TaskHandler taskHandler = main.getServer().getScheduler().scheduleRepeatingTask(main, new VoteTask(time,voter), 20, true);
            voteTaskId = taskHandler.getTaskId();
            voteTaskState = true;
        } else {
            if (voteTaskState) {
                voter.sendMessage(main.getMessagePrefix() + main.getLang(voter).translateString("vote_restart_msg_is_initiate"));
            }else {
                voter.sendMessage(main.getMessagePrefix() + main.getLang(voter).translateString("vote_restart_msg_not_initiate"));
            }
        }
    }

    public static void cancelVoteTask() {
        main.getServer().getScheduler().cancelTask(voteTaskId);
        voteTaskState = false;
        VoteUtils.getInstance().clearData(); // 投票任务结束后清理掉无用的数据
    }

    //这里我并没有找到Nukkit有提供暂停任务的方法，所以我自己实现了一个方法
    public static void pauseRestartTask() {
        restartTaskState = 2;
        cancelVoteTask(); //取消掉投票重启的任务防止因投票重启而导致服务器重启
        main.getServer().getScheduler().cancelTask(restartTaskId);
    }

    public static void continueRunRestartTask() {
        restartTaskState = 1;
        int runTick = 20;
        if (getRestartTaskType() == 3) {
            runTick = 100;
        }
        TaskHandler taskHandler = main.getServer().getScheduler().scheduleRepeatingTask(main, new RestartTask(RestartTask.getTime()), runTick, true);
        restartTaskId = taskHandler.getTaskId();
    }

    public static int getRestartTaskType() {
        return restartTaskType;
    }

    /**
     * 获取重启任务的状态
     * <br><br>0 = 停止<br>1= 运行<br>2 = 暂停
     */
    public static int getRestartTaskState() {
        return restartTaskState;
    }

    public static boolean getVoteTaskState() {
        return voteTaskState;
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
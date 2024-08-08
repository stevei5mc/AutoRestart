package cn.stevei5mc.autorestart;

import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Sound;
import cn.stevei5mc.autorestart.tasks.RestartTask;
import cn.nukkit.scheduler.TaskHandler;
import java.util.*;

public class Utils {
    private static AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    public static boolean taskState = false;//任务状态，默认为 false
    public static int taskType = 0;//任务类型，默认编号为 0
    private static int taskId;

    /**
     * 在指定时间内发送服务器需要重启的消息及播放音效 
     * @param seconds 传入剩余秒数
    */
    public static void sendRestartMsg(int seconds) {
        for (Player player : main.getServer().getOnlinePlayers().values()) {
            if (main.getConfig().getBoolean("show_title",true)) {
                player.sendTitle(
                    (main.getLang(player).translateString("restart_msg_title", seconds, main.getLang(player).translateString("time_unit_seconds"))), 
                    (main.getLang(player).translateString("restart_msg_subtitle", seconds, main.getLang(player).translateString("time_unit_seconds"))),
                0, 20, 0);
            }
            if (main.getConfig().getBoolean("show_tip",true)) {
                player.sendTip(main.getLang(player).translateString("restrat_msg_tip", seconds, main.getLang(player).translateString("time_unit_seconds")));
            }
            if (main.getConfig().getBoolean("play_sound",true)) {
                //参考和使用部分代码 https://github.com/glorydark/CustomForm/blob/main/src/main/java/glorydark/nukkit/customform/scriptForms/data/SoundData.java
                //读取配置中音效的设置
                String soundName = main.getConfig().getString("sound.name","random.toast");
                float volume = (float) main.getConfig().getDouble("sound.volume",1.0);
                float pitch = (float) main.getConfig().getDouble("sound.pitch",1.0);
                Optional<Sound> find = Arrays.stream(Sound.values()).filter(get -> get.getSound().equals(soundName)).findAny();// 获取音效对象
                Sound sound = find.orElse(null);
                // 检查音效是否存在
                if (sound != null) {
                    player.getLevel().addSound(player.getLocation(), sound, volume, pitch);// 播放音效给玩家
                }
            }
        }
    }

    
    // 踢出玩家及关闭服务器
    public static void shutdownServer() {
        cancelTask();
        Server.getInstance().getScheduler().scheduleDelayedTask(main, () -> {
            runCommand();
            kickOnlinePlayers();
            main.getServer().shutdown(); // 关闭服务器  注意：这里不会自动重启，你需要配置服务器管理工具或脚本来自动重启服务器进程
        }, 0);
    }

    //在重启前执行命令
    private static void runCommand() {
        if (main.getConfig().getBoolean("runcommand",true) && taskType <= 2) {
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                ArrayList<String> commands;
                commands = new ArrayList<>(main.getConfig().getStringList("commands"));
                for (String s : commands) {
                    String[] cmd = s.split("&");
                    if ((cmd.length > 1) && ("con".equals(cmd[1]))) {
                        main.getServer().dispatchCommand(main.getServer().getConsoleSender(), cmd[0].replace("@p", player.getName()));
                    }else {
                        main.getServer().dispatchCommand(player, cmd[0].replace("@p", player.getName()));
                    }
                }
            }
        }
    }

    //踢出在线玩家
    private static void kickOnlinePlayers() {
        if (main.getConfig().getBoolean("kick_player",true) && taskType <= 2) {
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                player.kick((main.getLang(player).translateString("kick_player_msg")), false);
            }
        }
    }

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
        cancelTask();//不管定时重启任务在不在运行都取消一遍再运行手动的任务，以防出现一些奇怪的问题
        int runTick = 20;
        int time = 30;
        switch (type) {
            case 1:
                time = restartTime * 60;
                runTick = 20;
                break;
            case 2:
                time = restartTime;
                runTick = 20;
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
    }

    public static void cancelTask() {
        main.getServer().getScheduler().cancelTask(taskId);
        taskState = false;
        taskType = 0;//重置任务编号
    }
}
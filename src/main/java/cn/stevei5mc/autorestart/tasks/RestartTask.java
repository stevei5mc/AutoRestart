package cn.stevei5mc.autorestart.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.utils.BaseUtils;
import cn.stevei5mc.autorestart.utils.TasksUtils;

import java.util.ArrayList;

public class RestartTask extends PluginTask<AutoRestartPlugin> {
    private static int time2 =30;
    private static int broadcastCycle = 0;
    public static AutoRestartPlugin main = AutoRestartPlugin.getInstance();

    public RestartTask(AutoRestartPlugin main,int time) {
        super(main);
        time2 = time;
        broadcastCycle = time;
    }

    @Override
    public void onRun(int currentTick) {
        int taskType = TasksUtils.getRestartTaskType();
        if (taskType != 3) {
            if (main.getConfig().getBoolean("enable_reminder_timer",true) && time2 == broadcastCycle && broadcastCycle > 0) {
                broadcastCycle = broadcastCycle -main.getConfig().getInt("broadcast_restart_reminder_cycle",30) * 60;
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    player.sendMessage(main.getMessagePrefix()+main.getLang(player).translateString("broadcast_restart_reminder_time",getRestartRemainder(player)));
                }
            }
            if (time2 <= BaseUtils.getRestartTipTime()) {
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    String unit = main.getLang(player).translateString("time_unit_seconds");
                    if (main.getConfig().getBoolean("show_title",true)) {
                        player.sendTitle(
                            (main.getLang(player).translateString("restart_msg_title", time2, unit)), 
                            (main.getLang(player).translateString("restart_msg_subtitle", time2, unit)),
                        0, 20, 0);
                    }
                    if (main.getConfig().getBoolean("show_tip",true)) {
                        player.sendTip(main.getLang(player).translateString("restrat_msg_tip", time2, unit));
                    }
                    if (main.getConfig().getBoolean("play_sound",true)) {
                        String soundName = main.getConfig().getString("sound.name","random.toast");
                        float volume = (float) main.getConfig().getDouble("sound.volume",1.0);
                        float pitch = (float) main.getConfig().getDouble("sound.pitch",1.0);
                        BaseUtils.playSound(soundName,volume,pitch,player);
                    }
                }
            }
            // 如果时间到了，重启服务器
            if (time2 <= 0) {
                TasksUtils.cancelRestartTask();
                Server.getInstance().getScheduler().scheduleDelayedTask(main, () -> {
                    runCommand();
                    if (main.getConfig().getBoolean("kick_player",true)) {
                        for (Player player : main.getServer().getOnlinePlayers().values()) {
                            player.kick((main.getLang(player).translateString("kick_player_msg")), false);
                        }
                    }
                    main.getServer().shutdown(); // 关闭服务器  注意：这里不会自动重启，你需要配置服务器管理工具或脚本来自动重启服务器进程
                }, 0);
            }
            time2--;// 每秒减少时间
        }
        //这段代码是给任务ID为3的任务来使用的
        if (taskType == 3 && Server.getInstance().getOnlinePlayers().size() == 0) {
            runCommand();
            main.getServer().shutdown(); // 关闭服务器  注意：这里不会自动重启，你需要配置服务器管理工具或脚本来自动重启服务器进程
        }
    }

    public static int getTime() {
        return time2;
    }

    /**
     * 获取剩余时间
     * @param player 传入player参数以实现多语言
     * @return remainder
     */
    public static String getRestartRemainder(Player player) {
        String hourUnit = main.getLang(player).translateString("time_unit_hour");
        String minuteUnit = main.getLang(player).translateString("time_unit_minutes");
        String secondUnit = main.getLang(player).translateString("time_unit_seconds");
        if (TasksUtils.getRestartTaskState() >= 1 && TasksUtils.getRestartTaskType() != 3) {
            int time = getTime();
            int hours = time / 3600;
            int minutes = (time % 3600) / 60;
            int seconds = time % 60;
            String timee = "";
            if (hours > 0) {
                timee = hours + hourUnit;
            }
            if (minutes > 0) {
                timee = timee + minutes + minuteUnit;
            }
            timee = timee + seconds + secondUnit;
            return main.getLang(player).translateString("variable_remainder",timee);
        }else {
            return "--"+secondUnit;
        }
    }

    public static void runCommand() {
        if (main.getConfig().getBoolean("runcommand",true)) {
            ArrayList<String> globalCommands = new ArrayList<>(main.getConfig().getStringList("commands.global"));
            for (String s : globalCommands) {
                String[] cmd = s.split("&");
                main.getServer().dispatchCommand(main.getServer().getConsoleSender(), cmd[0]);
            }
            if (TasksUtils.getRestartTaskType() != 3) {
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    ArrayList<String> playerCommands = new ArrayList<>(main.getConfig().getStringList("commands.player"));
                    for (String s : playerCommands) {
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
    }
}
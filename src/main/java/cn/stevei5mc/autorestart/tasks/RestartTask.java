package cn.stevei5mc.autorestart.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.Task;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.utils.BaseUtils;
import cn.stevei5mc.autorestart.utils.TasksUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class RestartTask extends Task {
    public static int time2 =30;
    public static AutoRestartPlugin main = AutoRestartPlugin.getInstance();

    public RestartTask(int time1) {
        time2 = time1;
    }

    @Override
    public void onRun(int currentTick) {
        int taskType = TasksUtils.getRestartTaskType();
        if (taskType <= 2 || taskType == 4) {
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
            // 如果时间到了，重启服务器
            if (time2 <= 0) {
                TasksUtils.cancelRestartTask();
                Server.getInstance().getScheduler().scheduleDelayedTask(main, () -> {
                    if (main.getConfig().getBoolean("runcommand",true)) {
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
            main.getServer().shutdown(); // 关闭服务器  注意：这里不会自动重启，你需要配置服务器管理工具或脚本来自动重启服务器进程
        }
    }
}
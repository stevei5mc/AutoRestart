package cn.stevei5mc.autorestart;

import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Sound;
import java.util.*;

public class Utils {
    private static AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    public static boolean taskState = false;

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
        main.cancelTask();
        Server.getInstance().getScheduler().scheduleDelayedTask(main, () -> {
            runCommand();
            kickOnlinePlayers();
            main.getServer().shutdown(); // 关闭服务器  注意：这里不会自动重启，你需要配置服务器管理工具或脚本来自动重启服务器进程
        }, 0);
    }

    //在重启前执行命令
    public static void runCommand() {
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
    }

    //踢出在线玩家
    public static void kickOnlinePlayers() {
        if (main.getConfig().getBoolean("kick_player",true)) {
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
}
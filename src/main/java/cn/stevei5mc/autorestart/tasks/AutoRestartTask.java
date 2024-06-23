package cn.stevei5mc.autorestart.tasks;

import cn.nukkit.scheduler.Task;
import cn.nukkit.Player;
import cn.nukkit.level.Sound;
import cn.lanink.gamecore.utils.Language;
import cn.stevei5mc.autorestart.AutoRestartPlugin;

import java.util.*;

public class AutoRestartTask extends Task {
    protected AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    private int restartTime = main.getConfig().getInt("restart_time", 2);// 设置重启前的延迟时间（单位：分钟）
    private int time = restartTime * 60; // 将分钟转换为秒
    
    @Override
    public void onRun(int currentTick) {
        time--;// 每秒减少时间
        int tipTime = main.getConfig().getInt("tips_time", 30);
        if (time <= tipTime) {
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                if (main.getConfig().getBoolean("show_title",true)) {
                    player.sendTitle((main.getLang(player).translateString("restart_msg_title", time)), (main.getLang(player).translateString("restart_msg_subtitle", time)), 0, 20, 0);
                }
                if (main.getConfig().getBoolean("show_tip",true)) {
                    player.sendTip(main.getLang(player).translateString("restrat_msg_tip", time));
                }
                if (main.getConfig().getBoolean("play_sound",true)) {
                    //参考和使用部分代码 https://github.com/glorydark/CustomForm/blob/main/src/main/java/glorydark/nukkit/customform/scriptForms/data/SoundData.java
                    // 读取配置中音效的设置
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
        if (time <= 0) {
            this.cancel();
            if (main.getConfig().getBoolean("kick_player",true)) {
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    player.kick((main.getLang(player).translateString("kick_player_msg")), false);
                }
            }
            main.getServer().shutdown(); // 关闭服务器  注意：这里不会自动重启，你需要配置服务器管理工具或脚本来自动重启服务器进程
        }
    }
}
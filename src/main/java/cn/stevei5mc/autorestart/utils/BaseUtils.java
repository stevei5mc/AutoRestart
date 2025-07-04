package cn.stevei5mc.autorestart.utils;

import cn.nukkit.Player;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.Config;
import cn.stevei5mc.autorestart.AutoRestartPlugin;

import java.util.Arrays;
import java.util.Optional;

public class BaseUtils {
    private static final Config config = AutoRestartPlugin.getInstance().getConfig();

    /**
     * 获取在重启前的开始提示的时间
     * @return 开始提示的时间
    */
    public static int getRestartTipTime() {
        int time = config.getInt("pre_restart_tip_time", 30);
        if (time < 1) {
            time = 30;
        }
        return time;
    }

    /**
     * 获取在重启服务器需要的时间
     * @return 重启服务器需要的时间
    */
    public static int getRestartUseTime() {
        int time  = config.getInt("restart_time", 2);
        if (time  < 1) {
            time = 30;
        }
        return time;
    }

    public static void playSound(String soundName, float volume, float pitch, Player target) {
        //参考和使用部分代码 https://github.com/glorydark/CustomForm/blob/main/src/main/java/glorydark/nukkit/customform/scriptForms/data/SoundData.java
        Optional<Sound> find = Arrays.stream(Sound.values()).filter(get -> get.getSound().equals(soundName)).findAny();// 获取音效对象
        if (volume < 0 ||  volume >= 1) {
            volume = 1;
        }
        if (pitch < 0 || pitch >= 1) {
            pitch = 1;
        }
        Sound sound = find.orElse(null);
        if (sound != null) {
            target.getLevel().addSound(target.getLocation(), sound, volume, pitch);
        }
    }
}
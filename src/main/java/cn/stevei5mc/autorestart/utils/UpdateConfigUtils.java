package cn.stevei5mc.autorestart.utils;

import cn.nukkit.utils.Config;
import cn.stevei5mc.autorestart.AutoRestartPlugin;

import java.io.File;
import java.util.Arrays;

public class UpdateConfigUtils {
    public static AutoRestartPlugin main = AutoRestartPlugin.getInstance();

    public static void updateConfig() {
        int latest = 6;
        Config config = AutoRestartPlugin.getInstance().getConfig();
        if (config.getInt("version", 1) < latest) {
            if (config.getInt("version", 1) < 2) {
                config.set("version", 2);
                if (!config.exists("vote_start_player")) {
                    config.set("vote_start_player",3);
                }
                if (!config.exists("vote_time")) {
                    config.set("vote_time",5);
                }
                if (!config.exists("runcommand")) {
                    config.set("runcommand",true);
                }
                if (!config.exists("commands")) {
                    config.set("commands", Arrays.asList("help", "say hello \"@p\"&con"));
                }
                if (!config.exists("debug")) {
                    config.set("debug",false);
                }
                config.save();
            }
            if (config.getInt("version") < 5) {
                config.set("version", 5);
                if (!config.exists("message_prefix")) {
                    config.set("message_prefix","§l§bAutoRestart §r§7>> ");
                }
                if (!config.exists("local_language_flies")) {
                    config.set("local_language_flies",false);
                }
                if (!config.exists("auto_update_language_files")) {
                    config.set("auto_update_language_files",false);
                }
                config.save();
            }
            if (config.getInt("version") < 6) {
                config.set("version",6);
                if (!config.exists("local_language_flies")) {
                    config.set("local_language_flies",false);
                }
                if (!config.exists("auto_update_language_files")) {
                    config.set("auto_update_language_files",false);
                }
                config.save();
            }
            main.getLogger().info("§a配置文件更新完毕，现在的配置文件版本已经是最新的了");
        } else if (config.getInt("version", 1) > latest) {
            main.getLogger().error("§c配置文件的版本出现异常，将对配置文件进行重置");
            config.save(new File(main.getDataFolder() + "/config.yml.bak"));
            main.saveResource("config.yml",true);
        }
    }
}

package cn.stevei5mc.autorestart.utils;

import cn.nukkit.utils.Config;
import cn.stevei5mc.autorestart.AutoRestartPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class UpdateConfigUtils {
    public static AutoRestartPlugin main = AutoRestartPlugin.getInstance();

    public static void updateConfig() {
        int latestVersion = 12;
        Config config = AutoRestartPlugin.getInstance().getConfig();
        if (config.getInt("version", 1) < latestVersion) {
            if (config.getInt("version", 1) < 2) {
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
            }
            if (config.getInt("version") < 5) {
                if (!config.exists("message_prefix")) {
                    config.set("message_prefix","§l§bAutoRestart §r§7>> ");
                }
                if (!config.exists("local_language_flies")) {
                    config.set("local_language_flies",false);
                }
                if (!config.exists("auto_update_language_files")) {
                    config.set("auto_update_language_files",false);
                }
            }
            if (config.getInt("version") < 7) {
                if (!config.exists("prompt_voting_status")) {
                    config.set("prompt_voting_status",true);
                }
                if (!config.exists("prompt_type")) {
                    config.set("prompt_type",1);
                }
            }
            if (config.getInt("version") < 9) {
                if (!config.exists("ignore_vote_remainder_time")) {
                    config.set("ignore_vote_remainder_time",false); // 考虑到有的用户是从旧版本一下子更新至最新版本所以也在这里同步修改
                }
                if (config.exists("commands")) {
                    ArrayList<String> commands = new ArrayList<>(main.getConfig().getStringList("commands"));
                    config.set("commands","");
                    config.set("commands.global",Arrays.asList("list", "status"));
                    config.set("commands.player",commands);
                }
            }
            if (config.getInt("version") < 12) {
                if (config.exists("tips_time")) {
                    int i = config.getInt("tips_time");
                    config.remove("tips_time");
                    config.set("pre_restart_tip_time", i);
                }
                if (config.exists("ignore_remainder_time")) {
                    boolean b = config.getBoolean("ignore_remainder_time");
                    config.remove("ignore_remainder_time");
                    config.set("ignore_vote_remainder_time",b);
                }
                if(!config.exists("enable_reminder_timer")) {
                    config.set("enable_reminder_timer",true);
                }
                if (!config.exists("broadcast_restart_reminder_cycle")) {
                    config.set("broadcast_restart_reminder_cycle",30);
                }
                if (config.exists("auto_update_language_files")) {
                    config.remove("auto_update_language_files");
                }
            }
            config.set("version", latestVersion);
            config.save();
            main.getLogger().info("§a配置文件更新完毕，现在的配置文件版本已经是最新的了");
        } else if (config.getInt("version", 1) > latestVersion) {
            main.getLogger().error("§c配置文件的版本出现异常，将对配置文件进行重置");
            config.save(new File(main.getDataFolder() + "/config.yml.bak"));
            main.saveResource("config.yml",true);
        }
    }
}
package cn.stevei5mc.autorestart;

import cn.lanink.gamecore.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.stevei5mc.autorestart.command.admin.AdminMain;
import cn.stevei5mc.autorestart.command.vote.VoteMain;
import cn.stevei5mc.autorestart.utils.BaseUtils;
import cn.stevei5mc.autorestart.utils.TasksUtils;
import tip.utils.Api;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AutoRestartPlugin extends PluginBase {
    private String defaultLanguage;
    private final HashMap<String, Language> languageMap = new HashMap<>();
    private final List<String> languages = Arrays.asList("zh_CN", "zh_TW","en_US");
    private static AutoRestartPlugin instance;
    private Config config;
    private static boolean tips = false;

    public Config getConfig() {
        return this.config;
    }

    public static AutoRestartPlugin getInstance() {
        return instance;
    }

    public void onLoad() {
        instance = this;
        saveDefaultConfig();
        this.config = new Config(this.getDataFolder() + "/config.yml", Config.YAML);
        for(String lang: languages){
            saveResource("language/"+lang+".yml",false);
        }
        updateConfig();
    }

    public void onEnable() {
        if (this.getServer().getPluginManager().getPlugin("MemoriesOfTime-GameCore") != null) {
            tips = false; //这是为了防止一些意外的情况准备的
            loadLanguage();
            this.getServer().getCommandMap().register("", new AdminMain());//注册命令
            this.getServer().getCommandMap().register("", new VoteMain());
            int i = BaseUtils.getRestartUseTime();
            TasksUtils.runRestartTask(i,1,1);
            if (this.getServer().getPluginManager().getPlugin("Tips") != null) {
                tips = true;
                Api.registerVariables("TipsVar",TipsVar.class);
            }
            Server.getInstance().getScheduler().scheduleDelayedTask(this, () -> {
                if (config.getBoolean("local_language_flies",false)) {
                    checkLanguageFilesVersion();
                }
                if (!tips) {
                    this.getLogger().warning("§c未检测到前置插件§aTips§c，相关变量无法生效");
                    this.getLogger().warning("§b下载地址: §ehttps://motci.cn/job/Tips/");
                }
                getLogger().info(this.getLang().translateString("restart_task_restart", i, getLang().translateString("time_unit_minutes")));
                getLogger().warning("§c警告! §c本插件为免费且开源的一款插件，如果你是付费获取到的那么你就被骗了");
                getLogger().info("§a开源链接和使用方法: §bhttps://github.com/stevei5mc/AutoRestart");
            },20);
        } else {
            //不存在作为卸载该插件
            this.getLogger().warning("§c未检测到前置插件§aMemoriesOfTime-GameCore§c，请安装后再试!!!");
            this.getLogger().warning("§b下载地址: §ehttps://motci.cn/job/GameCore/");
            this.onDisable();
        }
    }

    public void onDisable() {
        this.getLogger().info("已停止运行，感谢你的使用");
    }

    //使用(有改动)https://github.com/MemoriesOfTime/CrystalWars/blob/master/src/main/java/cn/lanink/crystalwars/CrystalWars.java
    private void loadLanguage() {
        this.defaultLanguage = this.config.getString("default_language", "zh_CN");
        if (!languages.contains(this.defaultLanguage)) {
            this.getLogger().error("Language" + this.defaultLanguage + "Not supported, will load Chinese!");
            this.defaultLanguage = "zh_CN";
        }
        for (String language : languages) {
            Config languageConfig = new Config(Config.YAML);
            if (config.getBoolean("local_language_flies",false)) {
                languageConfig.load(this.getDataFolder() + "/language/" + language + ".yml");
            } else {
                languageConfig.load(this.getResource("language/" + language + ".yml"));
            }
            this.languageMap.put(language, new Language(languageConfig));
        }
        this.getLogger().info(this.getLang().translateString("plugin_language"));
    }
    //同上
    public Language getLang() {
        return this.getLang(null);
    }
    //同上
    public Language getLang(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String playerLanguage = player.getLoginChainData().getLanguageCode();
            if (!this.languageMap.containsKey(playerLanguage)) {
                playerLanguage = this.defaultLanguage;
            }
            return this.languageMap.get(playerLanguage);
        }
        return this.languageMap.get(this.defaultLanguage);
    }

    public void reload() {
        this.config = new Config(this.getDataFolder() + "/config.yml", Config.YAML);
        loadLanguage();
    }

    public void updateConfig() {
        int latest = 5;
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
                    config.set("commands",Arrays.asList("help", "say hello \"@p\"&con"));
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
            getLogger().info("§a配置文件更新完毕，现在的配置文件版本已经是最新的了");
        } else if (config.getInt("version", 1) > latest) {
            getLogger().error("§c配置文件的版本出现异常，将对配置文件进行重置");
            config.save(new File(this.getDataFolder() + "/config.yml.bak"));
            saveResource("config.yml",true);
        }
    }

    public void checkLanguageFilesVersion() {
        int latestVersion = 3;
        for (String lang : languages) {
            Config language = new Config(this.getDataFolder()+"/language/"+lang+".yml");
            int version = language.getInt("language_version",1);
            if (version == latestVersion) {
                this.getLogger().info("语言文件" + lang + ".yml 的版本是最新的版本");
            }else if (version < latestVersion) {
                this.getLogger().warning("语言文件" + lang + ".yml 的版本需要进行更新，如果开启了自动更新则无视该消息");
                saveLanguageFile(lang);
            }else {
                this.getLogger().error("语言文件" + lang + ".yml 的版本出现了异常，如果开启了自动更新则无视该消息");
                saveLanguageFile(lang);
            }
        }
        reload();
    }

    private void saveLanguageFile(String file) {
        if (config.getBoolean("auto_update_language_files",false)) {
            saveResource("language/"+file+".yml",true);
        }
    }

    public String getMessagePrefix() {
        return config.getString("message_prefix","§l§bAutoRestart §r§7>> ");
    }
}
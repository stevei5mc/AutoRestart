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
import cn.stevei5mc.autorestart.utils.UpdateConfigUtils;
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
        for(String lang: languages){
            saveResource("language/"+lang+".yml",false);
        }
        loadConfig();
        UpdateConfigUtils.updateConfig();
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
                checkLanguageFilesVersion();
                if (!tips) {
                    this.getLogger().warning("§c未检测到前置插件§aTips§c，相关变量无法生效");
                    this.getLogger().warning("https://motci.cn/job/GameCore/          https://ci.lanink.cn/job/Tips/");
                }
                getLogger().info(this.getLang().translateString("restart_task_restart", i, getLang().translateString("time_unit_minutes")));
                getLogger().warning("§c警告! §c本插件为免费且开源的，如果您付费获取获取的，则有可能被误导");
                getLogger().info("§aGITHUB:§b https://github.com/stevei5mc/AutoRestart");
            },20);
        } else {
            //不存在作为卸载该插件
            this.getLogger().warning("§c未检测到前置插件§aMemoriesOfTime-GameCore§c，请安装后再试!!!");
            this.getLogger().warning("https://ci.lanink.cn/job/GameCore/         https://motci.cn/job/GameCore/");
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
        this.getLogger().info("Default language code "+defaultLanguage);
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
        loadConfig();
        loadLanguage();
    }

    public void loadConfig() {
        this.config = new Config(this.getDataFolder() + "/config.yml", Config.YAML);
    }

    public void checkLanguageFilesVersion() {
        if (config.getBoolean("local_language_flies",false)) {
            int latestVersion = 6;
            for (String lang : languages) {
                Config langFile = new Config(this.getDataFolder() + "/language/" + lang + ".yml");
                int version = langFile.getInt("language_version", 1);
                if (version == latestVersion) {
                    this.getLogger().info("语言文件" + lang + ".yml 的版本是最新的版本");
                } else if (version < latestVersion) {
                    if (config.getBoolean("auto_update_language_files", false)) {
                        boolean needSave = false;
                        if (!langFile.exists("language_version")) {
                            needSave = true;
                        }
                        langFile.set("language_version", latestVersion);
                        if (needSave) {
                            langFile.save();
                        }
                        Config newLangFile = new Config(Config.YAML);
                        newLangFile.load(this.getResource("language/" + lang + ".yml"));

                        Language updateLang = new Language(langFile);
                        updateLang.update(newLangFile);
                        this.getLogger().info("语言文件 " + lang + ".yml 已更新至最新版本，请查看是否有新内容");
                    } else {
                        this.getLogger().warning("语言文件 " + lang + ".yml 有新版本，请及时更新以免影响正常使用");
                    }
                } else {
                    // 检查出语言文件出现版本号出现问题就进行重置操作并对有问题的语言文件生成对应的备份文件
                    langFile.save(new File(this.getDataFolder() + "/language/" + lang + ".yml.backup"));
                    saveResource("language/" + lang + ".yml", true);
                    this.getLogger().error("§c语言文件 "+lang+".yml 出现版本号异常，现已自动修复，请检查备份文件 "+lang+".yml.backup 来排查问题所在！");
                }
            }
            reload();
        }
    }

    public String getMessagePrefix() {
        return config.getString("message_prefix","§l§bAutoRestart §r§7>> ");
    }

    public List<String> getLanguages() {
        return languages;
    }
}
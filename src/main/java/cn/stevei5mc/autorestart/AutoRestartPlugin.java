package cn.stevei5mc.autorestart;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import cn.lanink.gamecore.utils.Language;
import cn.nukkit.command.CommandSender;
import cn.stevei5mc.autorestart.command.admin.AdminMain;
import cn.stevei5mc.autorestart.command.user.UserMain;
import cn.stevei5mc.autorestart.Utils;
import tip.utils.Api;
import cn.stevei5mc.autorestart.TipsVar;
import java.util.*;

public class AutoRestartPlugin extends PluginBase {
    private Language language;
    private String defaultLanguage;
    private final HashMap<String, Language> languageMap = new HashMap<>();
    private List<String> languages = Arrays.asList("zh_CN", "zh_TW","en_US");
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
        saveLanguageFile();
    }

    public void onEnable() {
        if (this.getServer().getPluginManager().getPlugin("MemoriesOfTime-GameCore") != null) {
            tips = false;
            loadLanguage();
            this.getServer().getCommandMap().register("", new AdminMain());//注册命令
            this.getServer().getCommandMap().register("", new UserMain());
            int i = Utils.getRestartUseTime();
            Utils.runRestartTask(i,1);
            if (this.getServer().getPluginManager().getPlugin("Tips") != null) {
                tips = true;
                Api.registerVariables("TipsVar",TipsVar.class);
            }
            Server.getInstance().getScheduler().scheduleDelayedTask(this, () -> {
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

    private void saveLanguageFile() {
        for(String lang: languages){
            saveResource("language/"+lang+".yml",false);
        }
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
            languageConfig.load(this.getDataFolder() + "/language/" + language + ".yml");
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
    }
}
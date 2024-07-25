package cn.stevei5mc.autorestart;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import cn.lanink.gamecore.utils.Language;
import cn.nukkit.command.CommandSender;
import cn.stevei5mc.autorestart.command.AutoRestartCommand;
import cn.stevei5mc.autorestart.tasks.RestartTask;
import cn.nukkit.scheduler.TaskHandler;
import cn.stevei5mc.autorestart.Utils;
import java.util.*;

public class AutoRestartPlugin extends PluginBase {
    private Language language;
    private String defaultLanguage;
    private final HashMap<String, Language> languageMap = new HashMap<>();
    private List<String> languages = Arrays.asList("zh_CN", "zh_TW","en_US");
    private static AutoRestartPlugin instance;
    private Config config;
    private int taskId;

    public Config getConfig() {
        return this.config;
    }

    public static AutoRestartPlugin getInstance() {
        return instance;
    }

    public void onLoad() {
        instance = this;
        saveDefaultConfig();
        saveLanguageFile();
        this.config = new Config(this.getDataFolder() + "/config.yml", Config.YAML);
    }

    public void onEnable() {
        if (this.getServer().getPluginManager().getPlugin("MemoriesOfTime-GameCore") != null) {
            loadLanguage();
            this.getServer().getCommandMap().register("", new AutoRestartCommand());//注册命令
            int ia = 2;
            int ib = config.getInt("restart_time", 2);
            if (ib != 0) {
                ib = ia;
            }
            TaskHandler taskHandler = getServer().getScheduler().scheduleRepeatingTask(this, new RestartTask("min",ia), 20, true); // 每20tick执行一次 20tick=1s
            taskId = taskHandler.getTaskId();
            Utils.taskState = true;
            Server.getInstance().getScheduler().scheduleDelayedTask(this, () -> {
                getLogger().info(this.getLang().translateString("restart_task_restart", ia, getLang().translateString("time_unit_minutes")));
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
    //使用https://github.com/MemoriesOfTime/CrystalWars/blob/master/src/main/java/cn/lanink/crystalwars/CrystalWars.java
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

    public void cancelTask() {
        getServer().getScheduler().cancelTask(taskId);
        Utils.taskState = false;
    }

    public void dispatchRestart(int ia) {
        cancelTask();//不管定时重启任务在不在运行都取消一遍再运行手动的任务，以防出现一些奇怪的问题
        TaskHandler taskHandler = getServer().getScheduler().scheduleRepeatingTask(this, new RestartTask("seconds",ia), 20, true); // 每20tick执行一次 20tick=1s
        taskId = taskHandler.getTaskId();
        Utils.taskState = true;
    }

    public void errorSetting() {
        this.getLogger().info(this.getLang().translateString("plugin_language"));
    }
}
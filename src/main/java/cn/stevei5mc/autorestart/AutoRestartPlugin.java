package cn.stevei5mc.autorestart;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import cn.nukkit.Player;
import cn.nukkit.level.Sound;
import cn.lanink.gamecore.utils.Language;
import cn.nukkit.command.CommandSender;

import java.util.*;

public class AutoRestartPlugin extends PluginBase {
    private int restartTime = 2; // 设置重启前的延迟时间（单位：分钟）
    private Language language;
    private String defaultLanguage;
    private final HashMap<String, Language> languageMap = new HashMap<>();
    private Config config;

    public Config getConfig() {
        return this.config;
    }

    public void onLoad() {
        saveDefaultConfig();
        saveLanguageFile();
        this.config = new Config(this.getDataFolder() + "/config.yml", Config.YAML);
    }

    public void onEnable() {
        if (this.getServer().getPluginManager().getPlugin("MemoriesOfTime-GameCore") != null) {
            restartTime = config.getInt("restart_time", 2);// 设置重启前的延迟时间（单位：分钟）
            loadLanguage();
            scheduleRestart();// 当插件被启用时，计划自动重启任务
            getLogger().info(this.getLang().translateString("server_msg_restart_time", restartTime));
            getLogger().warning("§c警告! §c本插件为免费且开源的一款插件，如果你是付费获取到的那么你就被骗了");
            getLogger().info("§a开源链接和使用方法: §bhttps://github.com/stevei5mc/AutoRestart");
        } else {
            //不存在作为卸载该插件
            this.getLogger().warning("§c未检测到前置插件§aTips§c，请安装§aMemoriesOfTime-GameCore§c再试!!!");
            this.getLogger().warning("§b下载地址: §ehttps://motci.cn/job/GameCore/");
            this.onDisable();
        }
    }

    public void onDisable() {
        this.getLogger().info("已停止运行，感谢你的使用");
    }

    private void scheduleRestart() {
        // 使用 Nukkit 的定时任务系统来计划重启
        getServer().getScheduler().scheduleRepeatingTask(this, new Task() {
            int timeLeft = restartTime * 60; // 将分钟转换为秒
            @Override
            public void onRun(int currentTick) {
                timeLeft--;// 每秒减少时间
                getLogger().info("the server restart in "+ timeLeft);
                int tipTime = config.getInt("tips_time", 30);
                if (timeLeft <= tipTime) {
                    for (Player player : getServer().getOnlinePlayers().values()) {
                        if (config.getBoolean("show_title",true)) {
                            player.sendTitle((getLang(player).translateString("restart_msg_title", timeLeft)), (getLang(player).translateString("restart_msg_subtitle", timeLeft)), 0, 20, 0);
                        }
                        if (config.getBoolean("show_tip",true)) {
                            player.sendTip(getLang(player).translateString("restrat_msg_tip", timeLeft));
                        }
                        if (config.getBoolean("play_sound",true)) {
                            //参考和使用部分代码 https://github.com/glorydark/CustomForm/blob/main/src/main/java/glorydark/nukkit/customform/scriptForms/data/SoundData.java
                            // 读取配置中音效的设置
                            String soundName = config.getString("sound.name","random.toast");
                            float volume = (float) config.getDouble("sound.volume",1.0);
                            float pitch = (float) config.getDouble("sound.pitch",1.0);
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
                if (timeLeft <= 0) {
                    this.cancel();
                    if (config.getBoolean("kick_player",true)) {
                        for (Player player : getServer().getOnlinePlayers().values()) {
                            player.kick((getLang(player).translateString("kick_player_msg")), false);
                        }
                    }
                    getServer().shutdown(); // 关闭服务器  注意：这里不会自动重启，你需要配置服务器管理工具或脚本来自动重启服务器进程
                }
            }
        }, 20, true); // 每20tick运行一次 20tick=1s
    }

    private void saveLanguageFile() {
        String[] langList = new String[]{"zh_CN", "zh_TW","en_US"};
        for(String lang: langList){
            saveResource("language/"+lang+".properties",false);
            getLogger().info("已加载语言文件 "+lang+".properties");
        }
    }
    //使用https://github.com/MemoriesOfTime/CrystalWars/blob/master/src/main/java/cn/lanink/crystalwars/CrystalWars.java
    private void loadLanguage() {
        List<String> languages = Arrays.asList("zh_CN", "zh_TW","en_US");
        this.defaultLanguage = this.config.getString("default_language", "zh_CN");
        if (!languages.contains(this.defaultLanguage)) {
            this.getLogger().error("Language" + this.defaultLanguage + "Not supported, will load Chinese!");
            this.defaultLanguage = "zh_CN";
        }
        for (String language : languages) {
            Config languageConfig = new Config(Config.PROPERTIES);
            languageConfig.load(this.getDataFolder() + "/language/" + language + ".properties");
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
}
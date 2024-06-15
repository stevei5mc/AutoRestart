package cn.stevei5mc.autorestart;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import cn.nukkit.Player;

import java.util.concurrent.TimeUnit;

public class AutoRestartPlugin extends PluginBase {
    private static final int restartTime = 2; // 设置重启前的延迟时间（单位：分钟）

    public void onLoad() {
        this.saveDefaultConfig();
    }

    public void onEnable() {
        Config config = this.getConfig();
        scheduleRestart();// 当插件被启用时，计划自动重启任务
        getLogger().info("自动重启插件已启用，将在 " + restartTime + " 分钟后重启服务器。");
        getLogger().warning("§c警告! §c本插件为免费且开源的一款插件，如果你是付费获取到的那么你就被骗了");
        getLogger().info("§a开源链接和使用方法: §bhttps://github.com/stevei5mc/AutoRestart");
        if (config.getBoolean("debug")) {
            getLogger().info("debug 模式已开启");
        }
    }

    private void scheduleRestart() {
        Config config = this.getConfig();
        // 使用 Nukkit 的定时任务系统来计划重启
        getServer().getScheduler().scheduleRepeatingTask(this, new Task() {
            int timeLeft = restartTime * 60; // 将分钟转换为秒

            @Override
            public void onRun(int currentTick) {
                timeLeft--;// 每秒减少时间
                getLogger().info("the server restart in "+ timeLeft);
                if (timeLeft <= 30) {
                    for (Player player : getServer().getOnlinePlayers().values()) {
                        String title = "§c即将重启";
                        String subtitle = "§e本分支服即将在 §6{seconds} §e秒后重启"; 
                        if (config.getBoolean("show_title")) {
                            player.sendTitle(title.replace("{seconds}",String.valueOf(timeLeft)), subtitle.replace("{seconds}",String.valueOf(timeLeft)), 0, 20, 0);
                        }
                        if (config.getBoolean("show_tip")) {
                            player.sendTip(subtitle.replace("{seconds}",String.valueOf(timeLeft)));
                        }
                    }
                }
                // 如果时间到了，重启服务器
                if (timeLeft <= 0) {
                    this.cancel();
                    if (config.getBoolean("kick_player")) {
                        for (Player player : getServer().getOnlinePlayers().values()) {
                            player.kick("§e服务器正在重启\n稍后再会", false);
                        }
                    }
                    getLogger().info("自动重启服务器...");
                    if (!config.getBoolean("debug")) {
                        getServer().shutdown(); // 关闭服务器  注意：这里不会自动重启，你需要配置服务器管理工具或脚本来自动重启服务器进程
                    }
                }
            }
        }, 20, true); // 每20tick运行一次 20tick=1s
    }
}
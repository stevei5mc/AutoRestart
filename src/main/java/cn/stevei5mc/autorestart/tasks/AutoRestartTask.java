package cn.stevei5mc.autorestart.tasks;

import cn.nukkit.scheduler.Task;
import cn.lanink.gamecore.utils.Language;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.Utils;

public class AutoRestartTask extends Task {
    protected AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    private int restartTime = main.getConfig().getInt("restart_time", 2);// 设置重启前的延迟时间（单位：分钟）
    private int time = restartTime * 60; // 将分钟转换为秒
    
    @Override
    public void onRun(int currentTick) {
        time--;// 每秒减少时间
        int tipTime = main.getConfig().getInt("tips_time", 30);
        if (time <= tipTime) {
            Utils.sendRestartMsg(time);
        }
        // 如果时间到了，重启服务器
        if (time <= 0) {
            Utils.shutdownServer();
        }
    }
}
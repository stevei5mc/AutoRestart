package cn.stevei5mc.autorestart.tasks;

import cn.nukkit.scheduler.Task;
import cn.lanink.gamecore.utils.Language;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.Utils;


public class DispatchRestartTask extends Task {
    protected AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    private int time = main.getConfig().getInt("tips_time", 30);
    
    @Override
    public void onRun(int currentTick) {
        time--;// 每秒减少时间
        Utils.sendRestartMsg(time);
        // 如果时间到了，重启服务器
        if (time <= 0) {
            Utils.shutdownServer();
        }
    }
}
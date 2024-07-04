package cn.stevei5mc.autorestart.tasks;

import cn.nukkit.scheduler.Task;
import cn.lanink.gamecore.utils.Language;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.Utils;

public class RestartTask extends Task {
    private static AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    private static int time2 =30;
    
    /**
     * 初始化一些东西
     * @param type 时间单位(默认为秒)
     * @param time1 时间范围
    */
    public RestartTask(String type,int time1) {
        if (type == "min") {
            time2 = time1 * 60; // 将分钟转换为秒
        } else {
            time2 = time1;
        }
    }

    @Override
    public void onRun(int currentTick) {
        time2--;// 每秒减少时间
        int tipTime = main.getConfig().getInt("tips_time", 30);
        if (time2 <= tipTime) {
            Utils.sendRestartMsg(time2);
        }
        // 如果时间到了，重启服务器
        if (time2 <= 0) {
            Utils.shutdownServer();
        }
    }
}
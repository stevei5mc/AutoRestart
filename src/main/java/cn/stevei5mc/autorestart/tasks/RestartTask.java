package cn.stevei5mc.autorestart.tasks;

import cn.nukkit.scheduler.Task;
import cn.lanink.gamecore.utils.Language;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.Utils;

public class RestartTask extends Task {
    private static AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    public static int time2 =30;
    
    /**
     * 初始化一些东西
     * @param unit 时间单位(默认为秒)
     * @param time1 时间范围
    */
    public RestartTask(String unit,int time1) {
        if (unit == "min") {
            time2 = time1 * 60; // 将分钟转换为秒
        } else if (unit == "seconds") {
            time2 = time1;
        } else {
            time2 = 30;//如果重启任务的时间单位不是 min/seconds 就让服务器在30s候重启
        }
    }

    @Override
    public void onRun(int currentTick) {
        int tipTime = main.getConfig().getInt("tips_time", 30);
        if (time2 <= tipTime) {
            Utils.sendRestartMsg(time2);
        }
        // 如果时间到了，重启服务器
        if (time2 <= 0) {
            Utils.shutdownServer();
        }
        time2--;// 每秒减少时间
    }
}
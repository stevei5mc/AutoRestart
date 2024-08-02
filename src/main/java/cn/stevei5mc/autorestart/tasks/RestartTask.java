package cn.stevei5mc.autorestart.tasks;

import cn.nukkit.scheduler.Task;
import cn.lanink.gamecore.utils.Language;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.Utils;
import cn.nukkit.Server;

public class RestartTask extends Task {
    private static AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    public static int time2 =30;
    
    /**
     * 初始化一些东西
     * @param unit 时间单位(默认为秒)
     * @param time1 时间范围
    */
    public RestartTask(int type,int time1) {
        switch (type) {
            case 1:
                time2 = time1 * 60; // 将分钟转换为秒
                break;
            case 2:
                time2 = time1;
                break;
            default:
                time2 = 30;//如果重启任务的时间单位不是 min/seconds 就让服务器在30s候重启
                break;
        }
    }

    @Override
    public void onRun(int currentTick) {
        int t = Utils.taskType;
        if (t <= 2) {
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
        if (t == 3 && Server.getInstance().getOnlinePlayers().size() == 0) {
            Utils.shutdownServer();
        }
    }
}
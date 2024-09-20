package cn.stevei5mc.autorestart.tasks;

import cn.nukkit.scheduler.Task;
import cn.lanink.gamecore.utils.Language;
import cn.stevei5mc.autorestart.Utils;
import cn.nukkit.Server;

public class RestartTask extends Task {
    public static int time2 =30;
    public RestartTask(int time1) {
        time2 = time1;
    }

    @Override
    public void onRun(int currentTick) {
        int t = Utils.taskType;
        if (t <= 2) {
            if (time2 <= Utils.getRestartTipTime()) {
                Utils.sendRestartMsg(time2);
            }
            // 如果时间到了，重启服务器
            if (time2 <= 0) {
                Utils.shutdownServer();
            }
            time2--;// 每秒减少时间
        }
        //这段代码是给任务ID为3的任务来使用的
        if (t == 3 && Server.getInstance().getOnlinePlayers().size() == 0) {
            Utils.shutdownServer();
        }
    }
}
package cn.stevei5mc.autorestart.utils;

import cn.stevei5mc.autorestart.AutoRestartPlugin;

public class BaseUtils {
    private static final AutoRestartPlugin main = AutoRestartPlugin.getInstance();

    /**
     * 获取在重启前的开始提示的时间
     * @return 开始提示的时间
    */
    public static int getRestartTipTime() {
        int time = 30;
        int i = main.getConfig().getInt("tips_time", 30);
        if (i > 0) {
            time = i;
        }
        return time;
    }

    /**
     * 获取在重启服务器需要的时间
     * @return 重启服务器需要的时间
    */
    public static int getRestartUseTime() {
        int time = 2;
        int i = main.getConfig().getInt("restart_time", 2);
        if (i > 0) {
            time = i;
        }
        return time;
    }
}
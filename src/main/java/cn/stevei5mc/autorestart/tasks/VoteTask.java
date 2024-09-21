package cn.stevei5mc.autorestart.tasks;

import cn.nukkit.scheduler.Task;
import cn.lanink.gamecore.utils.Language;
import cn.stevei5mc.autorestart.Utils;
import cn.nukkit.Server;

import java.util.*;

public class VoteTask extends Task {
    public static int approval = 0;
    public static int oppose = 0;
    public static int abstention = 0;
    public static int time2 = 0;
    public static LinkedList<String> votePlayer = new LinkedList<String>();

    public VoteTask(int time) {
        //先重置数据
        approval = 0;
        oppose = 0;
        abstention = 0;
        votePlayer.clear();
        time2 = time;//设置投票时间
        
    }

    @Override
    public void onRun(int currentTick) {
        time2--;
    }
}
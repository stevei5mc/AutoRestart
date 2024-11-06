package cn.stevei5mc.autorestart.tasks;

import cn.nukkit.scheduler.Task;
import cn.lanink.gamecore.utils.Language;
import cn.stevei5mc.autorestart.utils.TasksUtils;
import cn.stevei5mc.autorestart.utils.BaseUtils;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.nukkit.Server;
import cn.nukkit.Player;

import java.util.*;

public class VoteTask extends Task {
    public static int approval = 0;
    public static int oppose = 0;
    public static int abstention = 0;
    public static int time2 = 0;
    public static int approvalVotes = 0;
    public static LinkedList<String> votePlayer = new LinkedList<String>();
    private static int msgTime = 0;
    private static AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    private static String voter;

    public VoteTask(int time,String vote) {
        //先重置数据
        approval = 0;
        oppose = 0;
        abstention = 0;
        votePlayer.clear();
        votePlayer.add(vote);
        approval++;
        time2 = time;//设置投票时间
        msgTime= time;
        voter = vote;
        approvalVotes = (int) Math.ceil(main.getServer().getOnlinePlayers().size() * 0.7);
    }

    @Override
    public void onRun(int currentTick) {
        if (msgTime == time2) {
            if (msgTime > 0) {
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    player.sendMessage(main.msgPrefix + main.getLang(player).translateString("vote_restart_msg_in_initiate",voter, "/voterestart"));
                }
                msgTime = msgTime - 60;
            }
        }
        if (time2 <= 0) {
            TasksUtils.cancelVoteTask();
            if (approval >= approvalVotes) {
                TasksUtils.cancelRestartTask(); //这里取消掉现有的重启任务，再运行新的重启任务以免出现奇怪的问题
                TasksUtils.runRestartTask(BaseUtils.getRestartTipTime(),2,2);
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    player.sendMessage(main.msgPrefix + main.getLang(player).translateString("vote_restart_msg_success", approval, approvalVotes));
                }
            } else {
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    player.sendMessage(main.msgPrefix + main.getLang(player).translateString("vote_restart_msg_failed_end", approval, approvalVotes));
                }
            }
        }
        time2--;
    }
}
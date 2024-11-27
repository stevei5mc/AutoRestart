package cn.stevei5mc.autorestart.tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.Task;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.utils.BaseUtils;
import cn.stevei5mc.autorestart.utils.TasksUtils;
import cn.stevei5mc.autorestart.utils.VoteUtils;


public class VoteTask extends Task {
    public static int time2 = 0;
    private static int msgTime = 0;
    private static AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    private static VoteUtils vu = VoteUtils.getInstance();
    private static String voterr;

    public VoteTask(int time,Player voter) {
        vu.initializedData(voter);
        time2 = time;//设置投票时间
        msgTime= time;
        voterr = voter.getName();
    }

    @Override
    public void onRun(int currentTick) {
        if (msgTime == time2) {
            if (msgTime > 0) {
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    player.sendMessage(main.msgPrefix + main.getLang(player).translateString("vote_restart_msg_in_initiate",voterr, "/voterestart"));
                }
                msgTime = msgTime - 30;
            }
        }
        if (time2 <= 0) {
            TasksUtils.cancelVoteTask();
            int approval = vu.getApproval();
            int approvalVotes = vu.getApprovalVotes();
            if (approval >= approvalVotes) {
                TasksUtils.runRestartTask(BaseUtils.getRestartTipTime(),4,2);
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
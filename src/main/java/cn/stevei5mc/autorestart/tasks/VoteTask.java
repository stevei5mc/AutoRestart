package cn.stevei5mc.autorestart.tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.Task;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.utils.BaseUtils;
import cn.stevei5mc.autorestart.utils.TasksUtils;
import cn.stevei5mc.autorestart.utils.VoteUtils;


public class VoteTask extends Task {
    private static int time2 = 0;
    private static int msgTime = 0;
    private static final AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    private static final VoteUtils vu = VoteUtils.getInstance();
    private static String voterr;

    public VoteTask(int time,Player voter) {
        vu.initializedData(voter);
        time2 = time;//设置投票时间
        msgTime= time;
        voterr = voter.getName();
    }

    @Override
    public void onRun(int currentTick) {
        int approval = vu.getApproval();
        int approvalVotes = vu.getApprovalVotes();
        int oppose = vu.getOppose();
        int abstention = vu.getAbstention();
        if (main.getConfig().getBoolean("prompt_voting_status",true)) {
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                String msg = main.getLang(player).translateString("prompt_voting_status_info","/voterestart",approval,approvalVotes,oppose,abstention,getVoteRemainder(player));
                switch (main.getConfig().getInt("prompt_type",0)) {
                    case 1:
                        player.sendTip(msg);
                        break;
                    case 2:
                        player.sendPopup(msg);
                        break;
                    default:
                        player.sendActionBar(msg);
                        break;
                }
            }
        }
        if (msgTime == time2 && msgTime > 0) {
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                player.sendMessage(main.getMessagePrefix() + main.getLang(player).translateString("vote_restart_msg_in_initiate",voterr, "/voterestart"));
            }
            msgTime = msgTime - 30;
        }
        if (time2 <= 0) {
            if (approval >= approvalVotes) {
                TasksUtils.runRestartTask(BaseUtils.getRestartTipTime(),4,2);
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    player.sendMessage(main.getMessagePrefix() + main.getLang(player).translateString("vote_restart_msg_success", approval, approvalVotes));
                }
            } else {
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    player.sendMessage(main.getMessagePrefix() + main.getLang(player).translateString("vote_restart_msg_failed_end", approval, approvalVotes));
                }
            }
            TasksUtils.cancelVoteTask();
        }
        time2--;
    }

    public static int getTime() {
        return time2;
    }

    /**
     * 获取剩余时间
     * @param player 传入player参数以实现多语言
     * @return remainder
     */
    public static String getVoteRemainder(Player player) {
        String minuteUnit = main.getLang(player).translateString("time_unit_minutes");
        String secondUnit = main.getLang(player).translateString("time_unit_seconds");
        if (TasksUtils.getVoteTaskState()) {
            int time = getTime();
            int minutes = (time % 3600) / 60;
            int seconds = time % 60;
            String timee = "";
            if (minutes > 0) {
                timee = minutes + minuteUnit;
            }
            timee = timee + seconds + secondUnit;
            return main.getLang(player).translateString("variable_remainder",timee);
        }else {
            return "--"+secondUnit;
        }
    }
}
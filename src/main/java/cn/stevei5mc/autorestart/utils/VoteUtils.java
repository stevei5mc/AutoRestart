package cn.stevei5mc.autorestart.utils;

import cn.nukkit.Player;
import cn.stevei5mc.autorestart.AutoRestartPlugin;

import java.util.LinkedList;

public class VoteUtils {
    private static VoteUtils instance;
    private int approval = 0;
    private int oppose = 0;
    private int abstention = 0;
    private int approvalVotes = 0;
    private  AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    private LinkedList<String> votePlayer = new LinkedList<String>();

    public static VoteUtils getInstance() {
        if (instance == null) {
            instance = new VoteUtils();
        }
        return instance;
    }

    public void initializedData(Player initiator) {
        String voter = initiator.getName();
        approval = 0;
        oppose = 0;
        abstention = 0;
        approvalVotes = 0;
        votePlayer.clear();
        votePlayer.add(voter);
        approval++;
    }

    public int getApproval() {
        return approval;
    }

    public int getOppose() {
        return oppose;
    }

    public int getAbstention() {
        return abstention;
    }

    public int getApprovalVotes() {
        return approvalVotes;
    }

    public boolean processVotingContent(Player voter, String voteContent) {
        String playerName = voter.getName();
        if (votePlayer.contains(playerName) && !voteContent.equals("veto")) {
            voter.sendMessage(main.msgPrefix + main.getLang(voter).translateString("vote_msg_failed_repeat"));
            return false;
        }
        if (voter.hasPermission("autorestart.user.vote")) {
            if (voteContent.equals("approval")) {
                approval++;
                voter.sendMessage(main.getLang(voter).translateString("vote_msg_vote",main.getLang(voter).translateString("vote_type_approval")));
            }
            if (voteContent.equals("oppose")) {
                oppose++;
                voter.sendMessage(main.getLang(voter).translateString("vote_msg_vote",main.getLang(voter).translateString("vote_type_oppose")));
            }
            if (voteContent.equals("veto") && voter.hasPermission("autorestart.admin.vote.veto")) {
                TasksUtils.cancelVoteTask();
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    player.sendMessage(main.getLang(player).translateString("vote_restart_msg_failed_veto"));
                }
            }
            if(voteContent.equals("abstention")) {
                abstention++;
                voter.sendMessage(main.getLang(voter).translateString("vote_msg_vote",main.getLang(voter).translateString("vote_type_abstention")));
            }
        }
        return true;
    }
}
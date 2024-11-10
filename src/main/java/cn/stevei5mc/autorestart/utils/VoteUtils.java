package cn.stevei5mc.autorestart.utils;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
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

    public void initializedData(CommandSender sender) {
        String voter = sender.getName();
        approval = 0;
        oppose = 0;
        abstention = 0;
        approvalVotes = (int) Math.ceil(main.getServer().getOnlinePlayers().size() * 0.7);
        votePlayer.clear();
        votePlayer.add(voter);
        approval++;
    }

    public void processVotingContent(CommandSender voter, String voteContent) {
        if (voter.isPlayer()) {
            Player player = (Player) voter;
            processVotingContent(player,voteContent);
        } else if (!voter.isPlayer() && voteContent.equals("veto")) {
            TasksUtils.cancelVoteTask();
            main.getLogger().info(main.msgPrefix + main.getLang().translateString("vote_restart_msg_failed_veto"));
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                player.sendMessage(main.msgPrefix + main.getLang(player).translateString("vote_restart_msg_failed_veto"));
            }
        } else {
            voter.sendMessage(main.msgPrefix +main.getLang(voter).translateString("command_in_game_run"));
        }

    }
    public void processVotingContent(Player voter, String voteContent) {
        String playerName = voter.getName();
        if (votePlayer.contains(playerName) && !voteContent.equals("veto")) {
            voter.sendMessage(main.msgPrefix + main.getLang(voter).translateString("vote_msg_failed_repeat"));
        }else if (voter.hasPermission("autorestart.user.vote") && voter.isPlayer()) {
            switch (voteContent) {
                case "approval":
                    approval++;
                    votePlayer.add(playerName);
                    voter.sendMessage(main.getLang(voter).translateString("vote_msg_vote", main.getLang(voter).translateString("vote_type_approval")));
                    break;
                case "oppose":
                    oppose++;
                    votePlayer.add(playerName);
                    voter.sendMessage(main.getLang(voter).translateString("vote_msg_vote", main.getLang(voter).translateString("vote_type_oppose")));
                    break;
                case "abstention":
                    if (!voter.hasPermission("autorestart.admin.vote.veto")) {
                        votePlayer.add(playerName);
                        abstention++;
                        voter.sendMessage(main.getLang(voter).translateString("vote_msg_vote", main.getLang(voter).translateString("vote_type_abstention")));
                    }
                    break;
                case "veto":
                    if (voter.hasPermission("autorestart.admin.vote.veto")) {
                        TasksUtils.cancelVoteTask();
                        for (Player player : main.getServer().getOnlinePlayers().values()) {
                            player.sendMessage(main.msgPrefix + main.getLang(player).translateString("vote_restart_msg_failed_veto"));
                        }
                    }
                    break;
                default:
                    voter.sendMessage(main.msgPrefix + main.getLang(voter).translateString("command_unknown"));
            }
        }
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
}
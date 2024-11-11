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

    /**
     * 初始化投票数据
     * 注：
     * 不建议直接使用此方法，而是使用TasksUtils的runVoteTask方法。
     * 如果直接使用此方法后果自负!!!!!!
     * @param initiated 投票发起者
     */
    public void initializedData(Player initiated) {
        String voter = initiated.getName();
        approval = 0;
        oppose = 0;
        abstention = 0;
        approvalVotes = (int) Math.ceil(main.getServer().getOnlinePlayers().size() * 0.7);
        votePlayer.clear();
        votePlayer.add(voter);
        approval++;
    }

    /**
     * 处理投票数据
     * @param voter 投票者
     * @param voteContent 投出的票的类型
     */
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

    /**
     * 处理投票数据
     * @param voter 投票者
     * @param voteContent 投出的票的类型
     */
    public void processVotingContent(Player voter, String voteContent) {
        String playerName = voter.getName();
        if (!voter.hasPermission("autorestart.user.vote")) {
            voter.sendMessage(main.msgPrefix+main.getLang(voter).translateString("command_not_permission"));
        }else if (votePlayer.contains(playerName) && !voteContent.equals("veto")) {
            voter.sendMessage(main.msgPrefix + main.getLang(voter).translateString("vote_msg_failed_repeat"));
        }else if (voter.hasPermission("autorestart.user.vote")) {
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
package cn.stevei5mc.autorestart.utils;

import cn.lanink.gamecore.utils.Language;
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
    private final AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    private LinkedList<String> votePlayer = new LinkedList<>();

    public static VoteUtils getInstance() {
        if (instance == null) {
            instance = new VoteUtils();
        }
        return instance;
    }

    /**
     * 清理无用数据
     */
    public void clearData() {
        approval = 0;
        oppose = 0;
        abstention = 0;
        votePlayer.clear();
    }

    /**
     * 初始化投票数据
     * <br><br>注意：
     * <br>不可以直接使用此方法，而是使用TasksUtils的runVoteTask方法。
     * <br>如果直接使用此方法后果自负!!!!!!
     * @param initiated 投票发起者
     */
    public void initializedData(Player initiated) {
        clearData(); //这里先清理一遍旧数据以防出现问题
        String voter = initiated.getName();
        approvalVotes = (int) Math.ceil(main.getServer().getOnlinePlayers().size() * 0.7);
        votePlayer.add(voter);
        approval++;
    }

    /**
     * 处理投票数据
     * <br><br>票类型：
     * <br>approval = 赞成票<br>abstention = 弃权票<br>oppose = 反对票<br>veto = 一票否决
     * <br><br>注意：
     * <br>如果拥有一票否决权的话将无法投出反对票
     * @param voter 投票者
     * @param voteType 投出的票的类型
     */
    public void processVotingContent(CommandSender voter, String voteType) {
        if (TasksUtils.getVoteTaskState()) {
            if (voter.isPlayer()) {
                Player player = (Player) voter;
                processVotingContent(player,voteType);
            } else if (!voter.isPlayer() && voteType.equals("veto")) {
                TasksUtils.cancelVoteTask();
                main.getLogger().info(main.getMessagePrefix() + main.getLang().translateString("vote_restart_msg_failed_veto"));
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    player.sendMessage(main.getMessagePrefix() + main.getLang(player).translateString("vote_restart_msg_failed_veto"));
                }
            } else {
                voter.sendMessage(main.getMessagePrefix() +main.getLang(voter).translateString("command_in_game_run"));
            }
        }else {
            voter.sendMessage(main.getMessagePrefix() + main.getLang(voter).translateString("vote_restart_msg_vote_failed"));
        }
    }

    /**
     * 处理投票数据
     * <br><br>票类型：
     * <br>approval = 赞成票<br>abstention = 弃权票<br>oppose = 反对票<br>veto = 一票否决
     * <br><br>注意：
     * <br>如果拥有一票否决权的话将无法投出反对票
     * @param voter 投票者
     * @param voteType 投出的票的类型
     */
    public void processVotingContent(Player voter, String voteType) {
        String playerName = voter.getName();
        if (TasksUtils.getVoteTaskState()) {
            if (!voter.hasPermission("autorestart.user.vote")) {
                voter.sendMessage(main.getMessagePrefix()+main.getLang(voter).translateString("command_not_permission"));
            }else if (votePlayer.contains(playerName) && !voteType.equals("veto")) {
                voter.sendMessage(main.getMessagePrefix() + main.getLang(voter).translateString("vote_msg_failed_repeat"));
            }else if (voter.hasPermission("autorestart.user.vote")) {
                switch (voteType) {
                    case "approval":
                        approval++;
                        votePlayer.add(playerName);
                        voter.sendMessage(main.getLang(voter).translateString("vote_msg_vote", main.getLang(voter).translateString("vote_type_approval")));
                        break;
                    case "abstention":
                        abstention++;
                        votePlayer.add(playerName);
                        voter.sendMessage(main.getLang(voter).translateString("vote_msg_vote", main.getLang(voter).translateString("vote_type_abstention")));
                        break;
                    case "oppose":
                        if (!voter.hasPermission("autorestart.admin.vote.veto")) {
                            votePlayer.add(playerName);
                            oppose++;
                            voter.sendMessage(main.getLang(voter).translateString("vote_msg_vote", main.getLang(voter).translateString("vote_type_oppose")));
                        }
                        break;
                    case "veto":
                        if (voter.hasPermission("autorestart.admin.vote.veto")) {
                            TasksUtils.cancelVoteTask();
                            for (Player player : main.getServer().getOnlinePlayers().values()) {
                                player.sendMessage(main.getMessagePrefix() + main.getLang(player).translateString("vote_restart_msg_failed_veto"));
                            }
                        }
                        break;
                    default:
                        voter.sendMessage(main.getMessagePrefix() + main.getLang(voter).translateString("command_unknown"));
                }
            }
        }else {
            voter.sendMessage(main.getMessagePrefix() + main.getLang(voter).translateString("vote_restart_msg_vote_failed"));
        }
    }

    /**
     * 获取赞成票数
     * @return 赞成票数
     */
    public int getApproval() {
        return approval;
    }

    /**
     * 获取反对票数
     * @return 反对票数
     */
    public int getOppose() {
        return oppose;
    }

    /**
     * 获取弃权票数
     * @return 弃权票数
     */
    public int getAbstention() {
        return abstention;
    }

    /**
     * 获取需要的赞成票数
     * @return 需要的赞成票数
     */
    public int getApprovalVotes() {
        return approvalVotes;
    }

    /**
     * 获取投票的综合数据
     * @param player 传入player参数以实现多语言
     * @return 返回综合数据
     */
    public String getVoteData(Player player) {
        Language lang = main.getLang(player);
        String data;
        if (TasksUtils.getVoteTaskState()) {
            data = lang.translateString("variable_vote_data_in_run",getApproval(),getApprovalVotes(),getOppose(),getAbstention());
        }else {
            data = lang.translateString("variable_none_data");
        }
        return data;
    }
}
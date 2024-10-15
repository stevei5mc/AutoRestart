package cn.stevei5mc.autorestart.command.vote;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.base.BaseSubCommand;
import cn.stevei5mc.autorestart.tasks.VoteTask;
import cn.stevei5mc.autorestart.utils.TasksUtils;
import cn.nukkit.Player;
import java.util.*;

public class VoteCmd extends BaseSubCommand {

    public VoteCmd(String name) {
        super(name);
    }

    @Override
    public boolean canUser(CommandSender sender) {
        return sender.hasPermission("autorestart.user.vote");
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 2) {
            String s = args[1];
            String vote = "";
            if (sender.isPlayer()) {
                Player player = (Player) sender;
                vote = player.getName();
                if (VoteTask.votePlayer.contains(vote) && !args[1].equals("veto")) {
                    sender.sendMessage(main.getLang(sender).translateString("vote_msg_failed_repeat"));
                    return false;
                }
            }
            if (sender.isPlayer() && args[1].equals("approval")) {
                VoteTask.approval++;
                sender.sendMessage(main.getLang(sender).translateString("vote_msg_vote",main.getLang(sender).translateString("vote_type_approval")));
            } else if (sender.isPlayer() && args[1].equals("oppose")) {
                VoteTask.oppose++;
                sender.sendMessage(main.getLang(sender).translateString("vote_msg_vote",main.getLang(sender).translateString("vote_type_oppose")));
            } else if (sender.isPlayer() && args[1].equals("abstention")) {
                VoteTask.abstention++;
                sender.sendMessage(main.getLang(sender).translateString("vote_msg_vote",main.getLang(sender).translateString("vote_type_abstention")));   
            } else if (args[1].equals("veto") && sender.hasPermission("autorestart.admin.vote.veto")) {
                TasksUtils.cancelVoteTask();
                for (Player player : main.getServer().getOnlinePlayers().values()) {
                    player.sendMessage(main.getLang(player).translateString("vote_restart_msg_failed_veto"));
                }
            } else if (sender.isPlayer()) {
                sender.sendMessage(main.getLang(sender).translateString("command_unknown"));
            }
            if (!VoteTask.votePlayer.contains(vote) && sender.isPlayer()) {
                VoteTask.votePlayer.add(vote);
            }
            return true;
        }else{
            sender.sendMessage(main.getLang(sender).translateString("command_unknown"));
            return false;
        }
    }

    @Override
    public CommandParameter[] getParameters() {
        LinkedList<String> vote = new LinkedList<String>();
        vote.add("approval");
        vote.add("oppose");
        vote.add("abstention");
        vote.add("veto");
        String[] vote2 = vote.toArray(new String[0]);
        return new CommandParameter[]{
            new CommandParameter("vote type", vote2),
        };
    }
}
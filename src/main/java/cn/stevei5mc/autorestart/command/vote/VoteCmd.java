package cn.stevei5mc.autorestart.command.vote;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.base.BaseSubCommand;
import cn.stevei5mc.autorestart.tasks.VoteTask;
import cn.stevei5mc.autorestart.Utils;
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
            switch (s) {
                case "approval":
                    VoteTask.approval = VoteTask.approval + 1;
                    sender.sendMessage(main.getLang(sender).translateString("vote_msg_vote",main.getLang(sender).translateString("vote_type_approval")));
                    break;
                case "oppose":
                    VoteTask.oppose = VoteTask.oppose + 1;
                    sender.sendMessage(main.getLang(sender).translateString("vote_msg_vote",main.getLang(sender).translateString("vote_type_oppose")));
                    break;
                case "abstention":
                    VoteTask.abstention = VoteTask.abstention + 1;
                    sender.sendMessage(main.getLang(sender).translateString("vote_msg_vote",main.getLang(sender).translateString("vote_type_abstention")));
                    break;
                case "veto":
                    if (sender.hasPermission("autorestart.admin.vote.veto")) {
                        Utils.cancelVoteTask();
                    }
                    break;
                default:
                    sender.sendMessage(main.getLang(sender).translateString("command_unknown"));
                    break;
            }
        }else{
            sender.sendMessage(main.getLang(sender).translateString("command_unknown"));
        }
        return true;
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
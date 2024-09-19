package cn.stevei5mc.autorestart.command.vote;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.base.BaseSubCommand;
import cn.nukkit.Player;
import cn.stevei5mc.autorestart.Utils;
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
                    sender.sendMessage(main.getLang(sender).translateString("vote_msg_vote",main.getLang(sender).translateString("vote_type_approval")));
                    return true;
                case "oppose":  
                    sender.sendMessage(main.getLang(sender).translateString("vote_msg_vote",main.getLang(sender).translateString("vote_type_oppose")));
                    return true;
                case "veto":
                    if (sender.hasPermission("autorestart.admin.vote.veto")) {
                        sender.sendMessage(main.getLang(sender).translateString("vote_msg_vote",main.getLang(sender).translateString("vote_type_veto")));
                    }
                    return true;
                default:
                    sender.sendMessage(main.getLang(sender).translateString("command_unknown"));
                    return false;
            }
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
        vote.add("veto");
        String[] vote2 = vote.toArray(new String[0]);
        return new CommandParameter[]{
            new CommandParameter("type", vote2),
        };
    }
}
package cn.stevei5mc.autorestart.command.vote.sub;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.base.BaseSubCommand;
import cn.stevei5mc.autorestart.utils.VoteUtils;

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
            String vote = args[1];
            VoteUtils.getInstance().processVotingContent(sender,vote);
            return true;
        }else{
            sender.sendMessage(main.getMessagePrefix() +main.getLang(sender).translateString("command_unknown"));
            return false;
        }
    }

    @Override
    public CommandParameter[] getParameters() {
        String[] vote = {"approval", "oppose", "abstention", "veto"};
        return new CommandParameter[]{
            CommandParameter.newEnum("vote type", vote)
        };
    }
}
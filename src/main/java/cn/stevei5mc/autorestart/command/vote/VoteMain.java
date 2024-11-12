package cn.stevei5mc.autorestart.command.vote;

import cn.lanink.gamecore.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.command.base.BaseCommand;
import cn.stevei5mc.autorestart.command.vote.sub.Initiate;
import cn.stevei5mc.autorestart.command.vote.sub.VoteCmd;
import cn.stevei5mc.autorestart.gui.Vote;
import cn.stevei5mc.autorestart.utils.TasksUtils;

public class VoteMain extends BaseCommand {
    protected AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    public VoteMain() {
        super("voterestart", "AutoRestart Command");
        this.setPermission("autorestart.user.vote");
        this.addSubCommand(new Initiate("initiate"));
        this.addSubCommand(new VoteCmd("vote"));
    }

    @Override
    public void sendHelp(CommandSender sender) {
        Language lang = main.getLang(sender);
        String cmdname = "§a/voterestart ";
        sender.sendMessage("§b=== AutoRestart vote command list ===");
        sender.sendMessage(cmdname+"initiate "+lang.translateString("command_help_vote_initiate"));
        sender.sendMessage(cmdname+"vote <approval|oppose|abstention|veto> "+lang.translateString("command_help_vote_vote"));
    }

    @Override
    public void sendUI(Player player) {
        if (TasksUtils.getVoteTaskState()) {
            Vote.voteGui(player);
        } else {
            Vote.initiateVote(player);  
        }
    }
}

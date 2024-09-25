package cn.stevei5mc.autorestart.command.vote;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.stevei5mc.autorestart.command.base.BaseCommand;
import cn.stevei5mc.autorestart.command.vote.Initiate;
import cn.stevei5mc.autorestart.command.vote.VoteCmd;
import cn.lanink.gamecore.utils.Language;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.Utils;
import cn.stevei5mc.autorestart.gui.Vote;

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
        sender.sendMessage(cmdname+"initiate "+main.getLang(sender).translateString("command_help_vote_initiate"));
        sender.sendMessage(cmdname+"vote <approval|oppose|abstention|veto> "+main.getLang(sender).translateString("command_help_vote_vote"));
    }

    @Override
    public void sendUI(Player player) {
        if (Utils.voteTaskState) {
            Vote.voteGui(player);
        } else {
            Vote.initiateVote(player);  
        }
    }
}

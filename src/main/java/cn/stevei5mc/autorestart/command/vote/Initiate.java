package cn.stevei5mc.autorestart.command.vote;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.base.BaseSubCommand;
import cn.nukkit.Player;
import cn.nukkit.Server; 
import cn.stevei5mc.autorestart.utils.TasksUtils;
import cn.stevei5mc.autorestart.tasks.RestartTask;
import java.util.*;

public class Initiate extends BaseSubCommand {

    public Initiate(String name) {
        super(name);
    }

    @Override
    public boolean canUser(CommandSender sender) {
        return sender.hasPermission("autorestart.user.vote") && sender.isPlayer();
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        String vote = "";
        if (TasksUtils.voteTaskState) {
            sender.sendMessage(main.getLang(sender).translateString("vote_restart_msg_is_initiate"));
        } else {
            Player player = (Player) sender;
            TasksUtils.runVoteTask(player);
        }
        return true; 
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
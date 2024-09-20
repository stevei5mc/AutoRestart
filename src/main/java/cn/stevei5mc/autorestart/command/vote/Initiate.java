package cn.stevei5mc.autorestart.command.vote;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.base.BaseSubCommand;
import cn.nukkit.Player;
import cn.stevei5mc.autorestart.Utils;
import java.util.*;

public class Initiate extends BaseSubCommand {

    public Initiate(String name) {
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
        String vote = "";
        if (Utils.voteTaskState) {
            sender.sendMessage(main.getLang(sender).translateString("vote_restart_msg_is_initiate"));
            return true;
        } else {
            if (sender.isPlayer()) {
                Player player = (Player) sender;
                vote = player.getName();
            } else {
                vote = "§d[§cServer§d]";
            }
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                player.sendMessage(main.getLang(player).translateString("vote_restart_msg_in_initiate", vote, "/voterestart"));
            }
            main.getLogger().info(main.getLang().translateString("vote_restart_msg_in_initiate", vote, "/voterestart"));
            return true; 
        }
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
package cn.stevei5mc.autorestart.command.vote;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.base.BaseSubCommand;
import cn.nukkit.Player;
import cn.nukkit.Server; 
import cn.stevei5mc.autorestart.Utils;
import cn.stevei5mc.autorestart.tasks.RestartTask;
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
        } else {
            if (sender.isPlayer()) {
                Player player = (Player) sender;
                vote = player.getName();
            } else {
                vote = "§d[§cServer§d]";
            }
            int startPlayer = main.getConfig().getInt("vote_start_player",3);
            int voteTime = main.getConfig().getInt("vote_time",5);
            //这里为了防止有人把时间设置为0或>5
            if (voteTime == 0 || voteTime > 5) {
                voteTime = 5;
            }
            //这里写死最低发起投票人数为3，在配置文件中定义低于3也会按照3来判断
            if (startPlayer < 3) {
                startPlayer = 3;
            }
            int time = voteTime * 60;
            if (/* Server.getInstance().getOnlinePlayers().size() >= startPlayer && */ time < RestartTask.time2) {
                Utils.runVoteTask(time,vote);
            } else {
                sender.sendMessage(main.getLang(sender).translateString("vote_restart_msg_not_initiate"));
            }
        }
        return true; 
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
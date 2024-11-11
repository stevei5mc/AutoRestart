package cn.stevei5mc.autorestart.command.admin.sub;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.base.BaseSubCommand;
import cn.nukkit.Player;
import cn.stevei5mc.autorestart.utils.TasksUtils;

/**
 * @author LT_Name
 */
public class Pause extends BaseSubCommand {

    public Pause(String name) {
        super(name);
    }

    @Override
    public boolean canUser(CommandSender sender) {
        return sender.hasPermission("autorestart.admin.pause");
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (TasksUtils.getRestartTaskState() == 1) {
            TasksUtils.pauseRestartTask();
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                player.sendMessage(main.msgPrefix + main.getLang(player).translateString("restart_msg_task_pause"));
            }
        } else if (TasksUtils.getRestartTaskState() == 2) {
            TasksUtils.continueRunRestartTask();
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                player.sendMessage(main.msgPrefix + main.getLang(player).translateString("restart_msg_task_continue"));
            }
        }
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}

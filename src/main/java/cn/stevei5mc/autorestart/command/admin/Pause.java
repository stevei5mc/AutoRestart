package cn.stevei5mc.autorestart.command.admin;

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
        if (TasksUtils.restartTaskState == 1) {
            TasksUtils.pauseRestartTask();
            main.getLogger().info((main.getLang().translateString("restart_task_pause")));
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                player.sendMessage(main.getLang(player).translateString("restart_task_pause"));
            }
        } else if (TasksUtils.restartTaskState == 2) {
            TasksUtils.continueRunRestartTask();
            main.getLogger().info((main.getLang().translateString("form_button_continue")));
            for (Player player : main.getServer().getOnlinePlayers().values()) {
                player.sendMessage(main.getLang(player).translateString("restart_task_continue"));
            }
        }
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}

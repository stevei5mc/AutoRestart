package cn.stevei5mc.autorestart.command.sub;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.base.BaseSubCommand;
import cn.nukkit.Player;
import cn.stevei5mc.autorestart.Utils;

/**
 * @author LT_Name
 */
public class Cancel extends BaseSubCommand {

    public Cancel(String name) {
        super(name);
    }

    @Override
    public boolean canUser(CommandSender sender) {
        return sender.hasPermission("autorestart.admin.cancel");
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Utils.cancelTask();
        main.getLogger().info((main.getLang().translateString("restart_task_cancel")));
        for (Player player : main.getServer().getOnlinePlayers().values()) {
            player.sendMessage(main.getLang(player).translateString("restart_task_cancel"));
        }
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}

package cn.stevei5mc.autorestart.command.sub;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.BaseSubCommand;
import cn.nukkit.Player;
import cn.stevei5mc.autorestart.Utils;

public class DispatchRestart extends BaseSubCommand {

    public DispatchRestart(String name) {
        super(name);
    }

    @Override
    public boolean canUser(CommandSender sender) {
        return sender.hasPermission("autorestart.admin.restart");
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        int i = Utils.getRestartTipTime();
        this.main.dispatchRestart(i);
        main.getLogger().info((main.getLang().translateString("restart_task_restart", i, main.getLang().translateString("time_unit_seconds"))));
        for (Player player : main.getServer().getOnlinePlayers().values()) {
            player.sendMessage(main.getLang(player).translateString("restart_task_restart", i, main.getLang(player).translateString("time_unit_seconds")));
        }
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}

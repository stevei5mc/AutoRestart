package cn.stevei5mc.autorestart.command.sub;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.BaseSubCommand;
import cn.nukkit.Player;

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
        int i = main.getConfig().getInt("tips_time", 30);
        this.main.dispatchRestart();
        main.getLogger().info((main.getLang(sender).translateString("restart_task_dispatch_restart", i)));
        for (Player player : main.getServer().getOnlinePlayers().values()) {
            player.sendMessage(main.getLang(player).translateString("restart_task_dispatch_restart", i));
        }
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}

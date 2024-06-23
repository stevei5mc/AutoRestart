package cn.stevei5mc.autorestart.command.sub;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.BaseSubCommand;

public class DispatchRestart extends BaseSubCommand {

    public DispatchRestart(String name) {
        super(name);
    }

    @Override
    public boolean canUser(CommandSender sender) {
        return sender.hasPermission("autorestart.admin.dispatchrestart");
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        this.main.dispatchRestart();
        sender.sendMessage(main.getLang(sender).translateString("restart_task_dispatch_restart", main.getConfig().getInt("tips_time", 30)));
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}

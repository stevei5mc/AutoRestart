package cn.stevei5mc.autorestart.command.admin;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.stevei5mc.autorestart.command.base.BaseCommand;
import cn.stevei5mc.autorestart.command.admin.Reload;
import cn.stevei5mc.autorestart.command.admin.Cancel;
import cn.stevei5mc.autorestart.command.admin.RunRestartTask;
import cn.lanink.gamecore.utils.Language;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.gui.Admin;

public class AdminMain extends BaseCommand {
    protected AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    public AdminMain() {
        super("autorestart", "AutoRestart Command");
        this.setPermission("autorestart.admin");
        this.addSubCommand(new Reload("reload"));
        this.addSubCommand(new Cancel("cancel"));
        this.addSubCommand(new RunRestartTask("restart"));
    }

    @Override
    public void sendHelp(CommandSender sender) {
        Language lang = main.getLang(sender);
        String cmdname = "§a/autorestart ";
        sender.sendMessage("§b=== AutoRestart admin command list ===");
        sender.sendMessage(cmdname+"reload "+lang.translateString("command_help_reload"));
        sender.sendMessage(cmdname+"cancel "+lang.translateString("command_help_cancel"));
        sender.sendMessage(cmdname+"restart <manual|no-player> "+lang.translateString("command_help_restart"));
    }

    @Override
    public void sendUI(Player player) {
        Admin.sendMain(player);
    }
}
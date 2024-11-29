package cn.stevei5mc.autorestart.command.admin;

import cn.lanink.gamecore.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.command.admin.sub.Cancel;
import cn.stevei5mc.autorestart.command.admin.sub.Pause;
import cn.stevei5mc.autorestart.command.admin.sub.Reload;
import cn.stevei5mc.autorestart.command.admin.sub.RunRestartTask;
import cn.stevei5mc.autorestart.command.base.BaseCommand;
import cn.stevei5mc.autorestart.gui.Admin;

public class AdminMain extends BaseCommand {
    protected AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    public AdminMain() {
        super("autorestart", "AutoRestart Command");
        this.setPermission("autorestart.admin");
        this.addSubCommand(new Reload("reload"));
        this.addSubCommand(new Cancel("cancel"));
        this.addSubCommand(new RunRestartTask("restart"));
        this.addSubCommand(new Pause("pause"));
    }

    @Override
    public void sendHelp(CommandSender sender) {
        Language lang = main.getLang(sender);
        String cmdname = "§a/autorestart ";
        sender.sendMessage("§b=== AutoRestart admin command list ===");
        sender.sendMessage(cmdname+"reload "+lang.translateString("command_help_reload"));
        sender.sendMessage(cmdname+"cancel "+lang.translateString("command_help_cancel"));
        sender.sendMessage(cmdname+"restart [task type] (time unit) (time) "+lang.translateString("command_help_restart"));
        sender.sendMessage(cmdname+"pause"+lang.translateString("command_help_pause"));
    }

    @Override
    public void sendUI(Player player) {
        Admin.sendMain(player);
    }
}
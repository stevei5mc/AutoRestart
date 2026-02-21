package cn.stevei5mc.autorestart.command.admin;

import cn.lanink.gamecore.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.stevei5mc.autorestart.command.admin.sub.Cancel;
import cn.stevei5mc.autorestart.command.admin.sub.Pause;
import cn.stevei5mc.autorestart.command.admin.sub.Reload;
import cn.stevei5mc.autorestart.command.admin.sub.RunRestartTask;
import cn.stevei5mc.autorestart.command.base.BaseCommand;
import cn.stevei5mc.autorestart.gui.Admin;

public class AdminMain extends BaseCommand {

    public AdminMain(String name, String description) {
        super(name, description);
        this.setPermission("autorestart.admin");
        this.addSubCommand(new Reload("reload"));
        this.addSubCommand(new Cancel("cancel"));
        this.addSubCommand(new RunRestartTask("restart"));
        this.addSubCommand(new Pause("pause"));
    }

    @Override
    public void sendHelp(CommandSender sender) {
        Language lang = main.getLang(sender);
        String cmdName = "§a/" + getName() + "";
        sender.sendMessage("§b=== AutoRestart admin command list ===");
        sender.sendMessage(cmdName + "reload " + lang.translateString("command_help_reload"));
        sender.sendMessage(cmdName + "cancel " + lang.translateString("command_help_cancel"));
        sender.sendMessage(cmdName + "restart [task type] (time unit) (time) " + lang.translateString("command_help_restart"));
        sender.sendMessage(cmdName + "pause" + lang.translateString("command_help_pause"));
    }

    @Override
    public void sendUI(Player player) {
        Admin.sendMain(player);
    }
}
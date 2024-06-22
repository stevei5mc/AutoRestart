package cn.stevei5mc.autorestart.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.stevei5mc.autorestart.command.BaseCommand;
import cn.stevei5mc.autorestart.command.sub.Reload;
import cn.lanink.gamecore.utils.Language;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
//import cn.stevei5mc.autorestart.utils.FormHelper;

/**
 * @author LT_Name
 */
public class AutoRestartCommand extends BaseCommand {
    protected AutoRestartPlugin main = AutoRestartPlugin.getInstance();
    public AutoRestartCommand() {
        super("autorestart", "AutoRestart Command");
        this.setPermission("autorestart.admin");

        this.addSubCommand(new Reload("reload"));
    }

    @Override
    public void sendHelp(CommandSender sender) {
        Language lang = main.getLang(sender);
        sender.sendMessage("§b=== AutoRestart Command List ===");
        sender.sendMessage("§a/autorestart reload "+lang.translateString("command_help_reload"));
    }

    /* @Override
    public void sendUI(Player player) {
        FormHelper.sendMain(player);
    } */

}

package cn.stevei5mc.autorestart.command.sub;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.BaseSubCommand;

/**
 * @author LT_Name
 */
public class Reload extends BaseSubCommand {

    public Reload(String name) {
        super(name);
    }

    @Override
    public boolean canUser(CommandSender sender) {
        return sender.hasPermission("autorestart.admin.reload");
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        this.main.reload();
        sender.sendMessage(main.getLang(sender).translateString("msg_command_reload_success"));
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}

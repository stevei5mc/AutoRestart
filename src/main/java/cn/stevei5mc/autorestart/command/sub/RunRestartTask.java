package cn.stevei5mc.autorestart.command.sub;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.data.CommandParamType;
import cn.stevei5mc.autorestart.command.BaseSubCommand;
import cn.nukkit.Player;
import cn.stevei5mc.autorestart.Utils;

public class RunRestartTask extends BaseSubCommand {

    public RunRestartTask(String name) {
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
        if (args.length == 2) {
            String s = args[1];
            switch (s) {
                case "manual":
                    int i = Utils.getRestartTipTime();
                    main.runRestartTask("seconds",i,2,20);
                    main.getLogger().info((main.getLang().translateString("restart_task_restart", i, main.getLang().translateString("time_unit_seconds"))));
                    for (Player player : main.getServer().getOnlinePlayers().values()) {
                        player.sendMessage(main.getLang(player).translateString("restart_task_restart", i, main.getLang(player).translateString("time_unit_seconds")));
                    }
                    return true;
                case "no-player":                
                    return true;
                default:
                    return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[]{
            new CommandParameter("manual|no-player", "task type"),
        };
    }
}
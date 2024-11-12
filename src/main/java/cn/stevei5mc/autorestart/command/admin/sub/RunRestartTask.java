package cn.stevei5mc.autorestart.command.admin.sub;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.stevei5mc.autorestart.command.base.BaseSubCommand;
import cn.stevei5mc.autorestart.utils.BaseUtils;
import cn.stevei5mc.autorestart.utils.TasksUtils;

import java.util.LinkedList;

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
                    int i = BaseUtils.getRestartTipTime();
                    TasksUtils.cancelRestartTask(); //这里取消掉现有的重启任务，再运行新的重启任务以免出现奇怪的问题
                    TasksUtils.runRestartTask(i,2,2);
                    return true;
                case "no-players":  
                    TasksUtils.cancelRestartTask();
                    TasksUtils.runRestartTask(3);
                    main.getLogger().info(main.msgPrefix +(main.getLang().translateString("restart_task_run",main.getLang().translateString("restart_task_type_no_player"))));
                    for (Player player : main.getServer().getOnlinePlayers().values()) {
                        player.sendMessage(main.msgPrefix +main.getLang(player).translateString("restart_task_run",main.getLang(player).translateString("restart_task_type_no_player")));
                    }
                    return true;
                default:
                    sender.sendMessage(main.msgPrefix +main.getLang(sender).translateString("command_unknown"));
                    return false;
            }
        }else{
            sender.sendMessage(main.msgPrefix +main.getLang(sender).translateString("command_unknown"));
            return false;
        }
    }

    @Override
    public CommandParameter[] getParameters() {
        LinkedList<String> list = new LinkedList<String>();
        list.add("manual");
        list.add("no-players");
        String[] list2 = list.toArray(new String[0]);
        return new CommandParameter[]{
            new CommandParameter("task type", list2),
        };
    }
}
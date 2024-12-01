package cn.stevei5mc.autorestart.command.admin.sub;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
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
        String commandUnknown = main.msgPrefix + main.getLang(sender).translateString("command_unknown");
        if (args.length == 2) {
            String s = args[1];
            switch (s) {
                case "manual":
                    int i = BaseUtils.getRestartTipTime();
                    TasksUtils.runRestartTask(i,2,2);
                    return true;
                case "no-players":
                    TasksUtils.runRestartTask(3);
                    main.getLogger().info(main.msgPrefix +(main.getLang().translateString("restart_task_run",main.getLang().translateString("restart_task_type_no_player"))));
                    for (Player player : main.getServer().getOnlinePlayers().values()) {
                        player.sendMessage(main.msgPrefix +main.getLang(player).translateString("restart_task_run",main.getLang(player).translateString("restart_task_type_no_player")));
                    }
                    return true;
                default:
                    sender.sendMessage(commandUnknown);
                    return false;
            }
        }else if(args.length == 4) {
            String type  = args[1];
            String timeUnit = args[2];
            int time = 0;
            try {
                time = Integer.parseInt(args[3]);
                if (type.equals("scheduled")) {
                    switch (timeUnit) {
                        case "hour":
                            TasksUtils.runRestartTask(time,5,3);
                            return true;
                        case "minutes":
                            TasksUtils.runRestartTask(time,5,1);
                            return true;
                        case "seconds":
                            TasksUtils.runRestartTask(time,5,2);
                            return true;
                        default:
                            sender.sendMessage(commandUnknown);
                            return false;
                    }
                }
            }catch (Exception ignored) {
                sender.sendMessage(main.msgPrefix + main.getLang(sender).translateString("command_unknown"));
                return false;
            }
            sender.sendMessage(commandUnknown);
            return false;
        } else{
            sender.sendMessage(commandUnknown);
            return false;
        }
    }

    @Override
    public CommandParameter[] getParameters() {
        LinkedList<String> list = new LinkedList<>();
        list.add("manual");
        list.add("no-players");
        list.add("scheduled");
        LinkedList<String> timeUnit =  new LinkedList<>();
        timeUnit.add("hour");
        timeUnit.add("minutes");
        timeUnit.add("seconds");
        String[] list2 = list.toArray(new String[0]);
        String[] timeUnit2 = timeUnit.toArray(new String[0]);
        return new CommandParameter[]{
            CommandParameter.newEnum("task type",list2),
            CommandParameter.newEnum("time unit",true,timeUnit2),
            CommandParameter.newType("time",true,CommandParamType.INT)
        };
    }
}
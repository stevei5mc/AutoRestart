package cn.stevei5mc.autorestart.gui;

import cn.lanink.gamecore.form.element.ResponseElementButton;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowCustom;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowModal;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowSimple;
import cn.lanink.gamecore.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementStepSlider;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.stevei5mc.autorestart.tasks.RestartTask;
import cn.stevei5mc.autorestart.utils.BaseUtils;
import cn.stevei5mc.autorestart.utils.TasksUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Admin {

    private Admin() {
        throw new RuntimeException("Error");
    }

    public static void sendMain(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(lang.translateString("form_title"));
        if (TasksUtils.getRestartTaskType() != 3 && TasksUtils.getRestartTaskState() >= 1) {
            simple.setContent(lang.translateString("form_confirm_cancel_description_time", RestartTask.getRestartRemainder(player)) + "\n\n");
        }
        String trueButton = lang.translateString("form_button_confirm");
        String falseButton = lang.translateString("form_button_back");
        String unitSeconds = lang.translateString("time_unit_seconds");
        if (TasksUtils.getRestartTaskState() >= 1 && player.hasPermission("autorestart.admin.cancel")) {
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_restart_cancel")).onClicked(cp -> {
                AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
                    lang.translateString("form_confirm_cancel_title"),
                    lang.translateString("form_confirm_cancel_description_task", TasksUtils.getRestartTaskName(player)),
                    trueButton,falseButton);
                    modal.onClickedTrue(cp2 -> Server.getInstance().dispatchCommand(cp2, "autorestart cancel"));
                    modal.onClickedFalse(Admin::sendMain);
                    cp.showFormWindow(modal);
                })
            );
        }
        if (TasksUtils.getRestartTaskState() == 1 && player.hasPermission("autorestart.admin.pause")) {
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_pause"))
                .onClicked(cp -> Server.getInstance().dispatchCommand(cp, "autorestart pause"))
            ); 
        }
        if (TasksUtils.getRestartTaskState() == 2 && player.hasPermission("autorestart.admin.pause")) {
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_continue"))
                .onClicked(cp -> Server.getInstance().dispatchCommand(cp, "autorestart pause"))
            ); 
        }
        if (TasksUtils.getRestartTaskState() == 0 && player.hasPermission("autorestart.admin.restart")) {
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_manual_restart")).onClicked(cp -> {
                int i = BaseUtils.getRestartTipTime();
                AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
                    lang.translateString("form_confirm_restart_title"),
                        lang.translateString("form_confirm_restart_description_task", lang.translateString("restart_task_type_manual_restart"))+"\n"+
                    lang.translateString("form_confirm_restart_description_time", i,unitSeconds),
                    trueButton,falseButton);
                    modal.onClickedTrue(cp2 -> Server.getInstance().dispatchCommand(cp2, "autorestart restart manual"));
                    modal.onClickedFalse(Admin::sendMain);
                    cp.showFormWindow(modal);
                }));
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_on_player")).onClicked(cp -> {
                AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
                    lang.translateString("form_confirm_restart_title"),
                    lang.translateString("form_confirm_restart_description_task", lang.translateString("restart_task_type_no_player")),
                    trueButton,falseButton);
                    modal.onClickedTrue(cp2 -> Server.getInstance().dispatchCommand(cp2, "autorestart restart no-players"));
                    modal.onClickedFalse(Admin::sendMain);
                    cp.showFormWindow(modal);
                }));
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_scheduled_time")).onClicked(Admin::sendSetRestartTime));
        }
        if (player.hasPermission("autorestart.admin.reload")) {
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_config_set")).onClicked(SettingsConfig::configSettings));
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_reload"))
                .onClicked(cp -> Server.getInstance().dispatchCommand(cp, "autorestart reload"))
            );
        }
        player.showFormWindow(simple);
    }

    public static void sendSetRestartTime(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        AdvancedFormWindowCustom custom = new AdvancedFormWindowCustom(lang.translateString("form_title_scheduled"));
        custom.addElement(new ElementLabel(lang.translateString("form_description_scheduled")+"\n\n"));
        custom.addElement(new ElementInput(lang.translateString("form_scheduled_input_set_time")));
        custom.addElement(new ElementStepSlider(lang.translateString("time_unit"), Arrays.asList(
            lang.translateString("time_unit_hour"),lang.translateString("time_unit_minutes"),lang.translateString("time_unit_seconds")
        )));
        custom.onResponded((formResponseCustom, cp) -> {
            int id = formResponseCustom.getStepSliderResponse(2).getElementID();
            String time = formResponseCustom.getInputResponse(1);
            if (!time.equals("")) {
                int time2;
                try {
                    time2 = Integer.parseInt(formResponseCustom.getInputResponse(1));
                    if (time2 < 1) {
                        time2 = 1;
                    }
                    String timeUnit = "";
                    int timeUnit2 = 0;
                    if (id == 0) {
                        timeUnit = lang.translateString("time_unit_hour");
                        timeUnit2 = 3;
                    }
                    if (id == 1) {
                        timeUnit = lang.translateString("time_unit_minutes");
                        timeUnit2 = 1;
                    }
                    if (id == 2) {
                        timeUnit = lang.translateString("time_unit_seconds");
                        timeUnit2 = 2;
                    }
                    AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
                        lang.translateString("form_title_scheduled"),
                        lang.translateString("restart_task_restart",time2,timeUnit),
                        lang.translateString("form_button_confirm"),
                        lang.translateString("form_button_back")
                    );
                    int finalTime = time2;
                    int finalTimeUnit = timeUnit2;
                    modal.onClickedTrue(cp2 -> TasksUtils.runRestartTask(finalTime, 5, finalTimeUnit));
                    modal.onClickedFalse(Admin::sendMain);
                    cp.showFormWindow(modal);
                }catch (Exception ignored) {
                    player.sendMessage(AutoRestartPlugin.getInstance().getMessagePrefix() +lang.translateString("command_unknown"));
                }
            }
        });
        custom.onClosed(Admin::sendMain);
        player.showFormWindow(custom);
    }
}
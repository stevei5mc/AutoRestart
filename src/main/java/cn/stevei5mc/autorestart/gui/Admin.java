package cn.stevei5mc.autorestart.gui;

import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.lanink.gamecore.form.element.ResponseElementButton;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowSimple;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowModal;
import cn.lanink.gamecore.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.Server;
import org.jetbrains.annotations.NotNull;
import cn.stevei5mc.autorestart.utils.*;

public class Admin {

    private Admin() {
        throw new RuntimeException("Error");
    }

    public static void sendMain(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(lang.translateString("form_title"));
        if (TasksUtils.restartTaskType < 2) {
            simple.setContent(lang.translateString("form_confirm_cancel_description_time",BaseUtils.getRemainder(player)) + "\n\n");
        }
        String trueButton = lang.translateString("form_button_confirm");
        String falseButton = lang.translateString("form_button_back");
        String unitSeconds = lang.translateString("time_unit_seconds");
        if (TasksUtils.restartTaskState >= 1 && player.hasPermission("autorestart.admin.cancel")) {
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_restart_cancel")).onClicked(cp -> {
                String postscript = "";
                String type = "";
                AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
                    lang.translateString("form_confirm_cancel_title"),
                    lang.translateString("form_confirm_cancel_description_task", BaseUtils.getRestartTaskName(player)),
                    trueButton,falseButton);
                    modal.onClickedTrue(cp2 -> Server.getInstance().dispatchCommand(cp2, "autorestart cancel"));
                    modal.onClickedFalse(cp2 -> sendMain(cp2));
                    cp.showFormWindow(modal);
                })
            );
        }
        if (TasksUtils.restartTaskState == 1 && player.hasPermission("autorestart.admin.pause")) {
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_pause"))
                .onClicked(cp -> Server.getInstance().dispatchCommand(cp, "autorestart pause"))
            ); 
        }
        if (TasksUtils.restartTaskState == 2 && player.hasPermission("autorestart.admin.pause")) {
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_continue"))
                .onClicked(cp -> Server.getInstance().dispatchCommand(cp, "autorestart pause"))
            ); 
        }
        if (TasksUtils.restartTaskState == 0 && player.hasPermission("autorestart.admin.restart")) {
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_manual_restart")).onClicked(cp -> {
                int i = BaseUtils.getRestartTipTime();
                AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
                    lang.translateString("form_confirm_restart_title"),
                    lang.translateString("form_confirm_restart_description_task", lang.translateString("restart_task_type_manual_restart"))+"\n"+
                    lang.translateString("form_confirm_restart_description_time", i,unitSeconds),
                    trueButton,falseButton);
                    modal.onClickedTrue(cp2 -> Server.getInstance().dispatchCommand(cp2, "autorestart restart manual"));
                    modal.onClickedFalse(cp2 -> sendMain(cp2));
                    cp.showFormWindow(modal);
                }));
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_on_player")).onClicked(cp -> {
                AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
                    lang.translateString("form_confirm_restart_title"),
                    lang.translateString("form_confirm_restart_description_task", lang.translateString("restart_task_type_no_player")),
                    trueButton,falseButton);
                    modal.onClickedTrue(cp2 -> Server.getInstance().dispatchCommand(cp2, "autorestart restart no-players"));
                    modal.onClickedFalse(cp2 -> sendMain(cp2));
                    cp.showFormWindow(modal);
                }));
        }
        if (player.hasPermission("autorestart.admin.reload")) {
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_reload"))
                .onClicked(cp -> Server.getInstance().dispatchCommand(cp, "autorestart reload"))
            );
        }
        player.showFormWindow(simple);
    }
}
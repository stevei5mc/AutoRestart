package cn.stevei5mc.autorestart;

import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.lanink.gamecore.form.element.ResponseElementButton;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowSimple;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowModal;
import cn.lanink.gamecore.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.math.Vector3;
import org.jetbrains.annotations.NotNull;
import cn.stevei5mc.autorestart.Utils;
import cn.stevei5mc.autorestart.tasks.RestartTask;

/**
 * 菜单（这个参考了rsnpc的写法）
 */
public class GUI {

    private GUI() {
        throw new RuntimeException("Error");
    }

    //主菜单
    public static void sendMain(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(lang.translateString("form_title"));
        String trueButton = lang.translateString("form_button_confirm_true");
        String falseButton = lang.translateString("form_button_confirm_false");
        if (Utils.taskState) {
            if (player.hasPermission("autorestart.admin.cancel")) {
                simple.addButton(new ResponseElementButton(lang.translateString("form_button_restart_cancel")).onClicked(cp -> {
                    String postscript = "";
                    String type = "";
                    if (Utils.taskType == 1) {
                        type = lang.translateString("restart_task_type_time");
                        postscript = "\n" + lang.translateString("form_confirm_cancel_description_time",RestartTask.time2,lang.translateString("time_unit_seconds"));
                    }
                    AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
                        lang.translateString("form_confirm_cancel_title"),
                        lang.translateString("form_confirm_cancel_description_task", type)+postscript,
                        trueButton,falseButton);
                        modal.onClickedTrue(cp2 -> Server.getInstance().dispatchCommand(cp2, "autorestart cancel"));
                        modal.onClickedFalse(cp2 -> sendMain(cp2));
                        cp.showFormWindow(modal);
                    })
                );
            }
        } else {
            if (player.hasPermission("autorestart.admin.restart")) {
                simple.addButton(new ResponseElementButton(lang.translateString("form_button_dispatch_restart")).onClicked(cp -> {
                    int i = Utils.getRestartTipTime();
                    AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
                        lang.translateString("form_confirm_restart_title"),
                        lang.translateString("form_confirm_restart_description_task", lang.translateString("restart_task_type_manual_restart"))+"\n"+
                        lang.translateString("form_confirm_restart_description_time", i,lang.translateString("time_unit_seconds")),
                        trueButton,falseButton);
                        modal.onClickedTrue(cp2 -> Server.getInstance().dispatchCommand(cp2, "autorestart restart"));
                        modal.onClickedFalse(cp2 -> sendMain(cp2));
                        cp.showFormWindow(modal);
                }));
            }
        }
        if (player.hasPermission("autorestart.admin.reload")) {
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_reload"))
                .onClicked(cp -> Server.getInstance().dispatchCommand(cp, "autorestart reload"))
            );
        }
        player.showFormWindow(simple);
    }
}

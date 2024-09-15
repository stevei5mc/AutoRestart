package cn.stevei5mc.autorestart.gui;

import cn.stevei5mc.autorestart.AutoRestartPlugin;
import cn.lanink.gamecore.form.element.ResponseElementButton;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowSimple;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowModal;
import cn.lanink.gamecore.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.Server;
import org.jetbrains.annotations.NotNull;
import cn.stevei5mc.autorestart.Utils;

public class Vote {

    private Vote() {
        throw new RuntimeException("Error");
    }

    public static void initiateVote(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
            lang.translateString("form_confirm_restart_title"),
            lang.translateString("form_confirm_restart_description_task", lang.translateString("restart_task_type_no_player")),
            lang.translateString("form_button_confirm_true"),
            lang.translateString("form_button_confirm_false")
        );
        modal.onClickedTrue(cp2 -> Server.getInstance().dispatchCommand(cp2, "autorestart restart no-players"));
        modal.onClickedFalse(cp2 -> Server.getInstance().dispatchCommand(cp2, "autorestart restart no-players"));
        player.showFormWindow(modal);
    }

    public static void voteGui(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(lang.translateString("vote_restart_form_title"));
        simple.addButton(new ResponseElementButton(lang.translateString("vote_restart_form_button_approval"))
            .onClicked(cp -> Server.getInstance().dispatchCommand(cp, "autorestart reload"))
        );
        simple.addButton(new ResponseElementButton(lang.translateString("vote_restart_form_button_oppose"))
            .onClicked(cp -> Server.getInstance().dispatchCommand(cp, "autorestart reload"))
        );
        if (player.hasPermission("autorestart.admin.vote.veto")) {
            simple.addButton(new ResponseElementButton(lang.translateString("vote_restart_form_button_veto"))
                .onClicked(cp -> Server.getInstance().dispatchCommand(cp, "autorestart reload"))
            ); 
        }
        player.showFormWindow(simple);
    }
}
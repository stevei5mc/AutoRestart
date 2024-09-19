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
            lang.translateString("vote_restart_form_title"),
            lang.translateString("vote_restart_form_description_confirm"),
            lang.translateString("form_button_confirm"),
            lang.translateString("form_button_close")
        );
        modal.onClickedTrue(cp2 -> Server.getInstance().dispatchCommand(cp2, "voterestart initiate"));//确认按钮，关闭按钮无需如何代码
        player.showFormWindow(modal);
    }

    public static void voteGui(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(lang.translateString("vote_restart_form_title"));
        simple.addButton(new ResponseElementButton(lang.translateString("form_button_approval"))
            .onClicked(cp -> Server.getInstance().dispatchCommand(cp, "voterestart vote approval"))
        );
        simple.addButton(new ResponseElementButton(lang.translateString("form_button_oppose"))
            .onClicked(cp -> Server.getInstance().dispatchCommand(cp, "voterestart vote oppose"))
        );
        if (player.hasPermission("autorestart.admin.vote.veto")) {
            simple.addButton(new ResponseElementButton(lang.translateString("form_button_veto"))
                .onClicked(cp -> Server.getInstance().dispatchCommand(cp, "voterestart vote veto"))
            ); 
        }
        player.showFormWindow(simple);
    }
}
package cn.stevei5mc.autorestart;

import cn.nukkit.Player;
import tip.utils.variables.BaseVariable;
import cn.stevei5mc.autorestart.utils.BaseUtils;

public class TipsVar extends BaseVariable {
    public TipsVar(Player player) {
        super(player);
    }

    public void strReplace() {
        if (player != null && player.isOnline()) {
            var();
        }
    }

    public void var() {
        addStrReplaceString("{restart-remainder}", BaseUtils.getRemainder(player));
    }
}
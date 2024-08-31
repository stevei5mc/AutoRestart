package cn.stevei5mc.autorestart;

import cn.nukkit.Player;
import tip.utils.variables.BaseVariable;
import cn.stevei5mc.autorestart.Utils;

public class TipsVar extends BaseVariable {
    public TipsVar(Player player) {
        super(player);
    }

    public void strReplace() {
        var();
    }

    public void var() {
        addStrReplaceString("{restart-remainder}", Utils.getRemainder(player));
    }
}
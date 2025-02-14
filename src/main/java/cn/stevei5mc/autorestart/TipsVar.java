package cn.stevei5mc.autorestart;

import cn.nukkit.Player;
import cn.stevei5mc.autorestart.tasks.RestartTask;
import cn.stevei5mc.autorestart.utils.VoteUtils;
import tip.utils.variables.BaseVariable;

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
        addStrReplaceString("{restart-remainder}", RestartTask.getRestartRemainder(player));
        addStrReplaceString("{vote-data}", VoteUtils.getInstance().getVoteData(player));
    }
}
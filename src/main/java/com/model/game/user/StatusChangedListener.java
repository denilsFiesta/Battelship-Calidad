package com.model.game.user;

import com.model.game.AttackReport;

public interface StatusChangedListener {
    /**
     * method to notify listener that status has been changed
     *
     * @param report attacking report
     */
    void onStatusChanged(AttackReport report);
}

package com.flash3388.flashview.gui;

import com.flash3388.flashview.actions.ActionType;

public class ActionSelectionCell {

    private final String mName;
    private final ActionType mActionType;

    public ActionSelectionCell(String name, ActionType actionType) {
        mName = name;
        mActionType = actionType;
    }

    public String getName() {
        return mName;
    }

    public ActionType getActionType() {
        return mActionType;
    }

    @Override
    public String toString() {
        return mName;
    }
}

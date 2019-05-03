package com.flash3388.flashview.gui.drag;

import java.io.Serializable;

public class LinkDragContainer implements Serializable {

    private final String mId;
    private String mTarget;

    public LinkDragContainer(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public String getTarget() {
        return mTarget;
    }

    public void setTarget(String target) {
        mTarget = target;
    }
}

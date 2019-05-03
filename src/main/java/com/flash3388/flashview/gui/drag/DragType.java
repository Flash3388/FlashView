package com.flash3388.flashview.gui.drag;

import javafx.scene.input.DataFormat;

public class DragType {

    private DragType() {}

    public static final DataFormat ADD_NODE =
            new DataFormat("application.DragIcon.add");

    public static final DataFormat DRAG_NODE =
            new DataFormat("application.DraggableNode.drag");

    public static final DataFormat ADD_LINK =
            new DataFormat("application.NodeLink.add");
}

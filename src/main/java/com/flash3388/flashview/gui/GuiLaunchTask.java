package com.flash3388.flashview.gui;

import javafx.application.Application;

public class GuiLaunchTask implements Runnable {

    @Override
    public void run() {
        Application.launch(FlashViewGui.class);
    }
}

package com.flash3388.flashview.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class FlashViewGui extends Application {

    private static FlashViewGui sInstance;
    private static CyclicBarrier sLaunchBarrier = new CyclicBarrier(2);

    private Stage mPrimaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        sInstance = this;
        sLaunchBarrier.await();

        mPrimaryStage = primaryStage;
    }

    public static Stage startGui(ExecutorService executorService) {
        Future<?> guiRunFuture = executorService.submit(new GuiLaunchTask());

        try {
            sLaunchBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {

        }

        return sInstance.mPrimaryStage;
    }
}

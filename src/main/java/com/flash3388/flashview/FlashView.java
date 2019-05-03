package com.flash3388.flashview;

import com.flash3388.flashview.actions.ActionTypeFactory;
import com.flash3388.flashview.actions.ActionTypeInitializationException;
import com.flash3388.flashview.gui.FlashViewGui;
import com.flash3388.flashview.image.ImageLoader;
import com.flash3388.flashview.gui.MainWindow;
import com.flash3388.flashview.gui.WindowCloseRequest;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class FlashView {

    private static final double WINDOW_WIDTH = 600;
    private static final double WINDOW_HEIGHT = 400;

    private final ExecutorService mExecutorService;
    private final Logger mLogger;

    public FlashView(ExecutorService executorService, Logger logger) {
        mExecutorService = executorService;
        mLogger = logger;
    }

    public void start() throws InitializationException {
        mLogger.info("Starting GUI");
        Stage primaryStage = FlashViewGui.startGui(mExecutorService);
        mLogger.info("GUI launched");

        showMainWindow(primaryStage);
    }

    private void showMainWindow(Stage primaryStage) throws InitializationException {
        try {
            CountDownLatch runLatch = new CountDownLatch(1);

            final MainWindow mainWindow = new MainWindow(WINDOW_WIDTH, WINDOW_HEIGHT, ActionTypeFactory.createAll(new ImageLoader()));

            Platform.runLater(()-> {
                primaryStage.setScene(mainWindow.createScene());
                primaryStage.setOnCloseRequest(new WindowCloseRequest());
                primaryStage.show();

                runLatch.countDown();
            });

            runLatch.await();
        } catch (ActionTypeInitializationException | InterruptedException e) {
            Platform.exit();
            throw new InitializationException(e);
        }
    }
}

package com.flash3388.flashview;

import com.flash3388.flashview.commands.CommandTypeFactory;
import com.flash3388.flashview.commands.CommandTypeInitializationException;
import com.flash3388.flashview.gui.FlashViewGui;
import com.flash3388.flashview.image.ImageLoader;
import com.flash3388.flashview.gui.MainWindow;
import com.flash3388.flashview.gui.WindowCloseRequest;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class FlashView {

    private static final double WINDOW_WIDTH = 1000;
    private static final double WINDOW_HEIGHT = 800;

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

            final MainWindow mainWindow = new MainWindow(WINDOW_WIDTH, WINDOW_HEIGHT, CommandTypeFactory.createAll(new ImageLoader()), new ImageLoader());

            Platform.runLater(()-> {
                try {
                    primaryStage.setScene(mainWindow.createScene());
                } catch (IOException e) {
                    Platform.exit();
                }
                primaryStage.setOnCloseRequest(new WindowCloseRequest());
                primaryStage.show();

                runLatch.countDown();
            });

            runLatch.await();
        } catch (CommandTypeInitializationException | InterruptedException e) {
            Platform.exit();
            throw new InitializationException(e);
        }
    }
}

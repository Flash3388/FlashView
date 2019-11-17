package com.flash3388.flashview;

import com.flash3388.flashview.commands.CommandType;
import com.flash3388.flashview.commands.CommandTypeFactory;
import com.flash3388.flashview.commands.CommandTypeInitializationException;
import com.flash3388.flashview.deploy.Deployer;
import com.flash3388.flashview.gui.FlashViewGui;
import com.flash3388.flashview.image.ImageLoader;
import com.flash3388.flashview.gui.MainWindow;
import com.flash3388.flashview.gui.WindowCloseRequest;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class FlashView {

    private static final boolean FORCE_FULL_SCREEN = true;

    private static final double WINDOW_WIDTH = 1000;
    private static final double WINDOW_HEIGHT = 800;

    private final ExecutorService mExecutorService;
    private final Deployer mDeployer;
    private final ImageLoader mImageLoader;
    private final Logger mLogger;

    public FlashView(ExecutorService executorService, Deployer deployer, ImageLoader imageLoader, Logger logger) {
        mExecutorService = executorService;
        mDeployer = deployer;
        mImageLoader = imageLoader;
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

            List<CommandType> commandTypes = CommandTypeFactory.createAll(mImageLoader);
            final MainWindow mainWindow = new MainWindow(primaryStage, WINDOW_WIDTH, WINDOW_HEIGHT,
                    commandTypes, mDeployer, mImageLoader);

            Platform.runLater(()-> {
                try {
                    primaryStage.setScene(mainWindow.createScene());
                } catch (IOException e) {
                    Platform.exit();
                }

                if (FORCE_FULL_SCREEN) {
                    primaryStage.setFullScreen(true);
                    primaryStage.setMaximized(true);
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

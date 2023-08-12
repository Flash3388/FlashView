package com.flash3388.flashview;

import com.flash3388.flashview.commands.CommandType;
import com.flash3388.flashview.commands.ViewableCommandType;
import com.flash3388.flashview.deploy.Deployer;
import com.flash3388.flashview.gui.FlashViewGui;
import com.flash3388.flashview.gui.MainWindow;
import com.flash3388.flashview.gui.WindowCloseRequest;
import com.flash3388.flashview.image.ImageLoader;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
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
    private final List<CommandType> mCommandTypes;
    private final Logger mLogger;

    public FlashView(ExecutorService executorService, Deployer deployer, ImageLoader imageLoader,
                     List<CommandType> commandTypes, Logger logger) {
        mExecutorService = executorService;
        mDeployer = deployer;
        mImageLoader = imageLoader;
        mCommandTypes = commandTypes;
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

            List<ViewableCommandType> commandTypes = loadCommandTypes();
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
        } catch (Exception e) {
            Platform.exit();
            throw new InitializationException(e);
        }
    }

    private List<ViewableCommandType> loadCommandTypes() throws IOException {
        List<ViewableCommandType> commandTypes = new ArrayList<>();
        for (CommandType commandType : mCommandTypes) {
            commandTypes.add(new ViewableCommandType(commandType, mImageLoader));
        }

        return commandTypes;
    }
}

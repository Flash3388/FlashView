package com.flash3388.flashview;

import com.castle.util.closeables.Closer;
import com.castle.util.logging.LoggerBuilder;
import com.flash3388.flashview.commands.CommandType;
import com.flash3388.flashview.deploy.Deployer;
import com.flash3388.flashview.deploy.Destination;
import com.flash3388.flashview.deploy.Remote;
import com.flash3388.flashview.deploy.SshjDeployer;
import com.flash3388.flashview.image.ImageLoader;
import com.flash3388.flashview.io.CommandTypesLoader;
import com.flash3388.flashview.io.JsonCommandTypesLoader;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final File COMMAND_TYPES_FILE = new File("command-types.json");

    public static void main(String[] args) throws Exception {
        Logger logger = new LoggerBuilder("flashview")
                .enableConsoleLogging(true)
                .build();

        CommandTypesLoader commandTypesLoader = new JsonCommandTypesLoader();
        List<CommandType> commandTypes = commandTypesLoader.load(COMMAND_TYPES_FILE.toPath());

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Closer closer = Closer.empty();
        closer.add(executorService::shutdownNow);

        Deployer deployer = new SshjDeployer(
                new Remote("admin", "", "roborio-3388-frc.local"),
                new Destination("/home/lvuser/flashviewProgram.json"),
                COMMAND_TYPES_FILE.toPath(),
                logger
        );
        ImageLoader imageLoader = new ImageLoader();

        try {
            FlashView flashView = new FlashView(executorService, deployer, imageLoader, commandTypes, logger);
            flashView.start();
        } finally {
            closer.close();
        }
    }
}

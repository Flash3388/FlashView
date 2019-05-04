package com.flash3388.flashview;

import com.flash3388.flashlib.io.Closer;
import com.flash3388.flashlib.util.concurrent.ExecutorCloser;
import com.flash3388.flashlib.util.logging.LoggerBuilder;
import com.flash3388.flashview.deploy.Deployer;
import com.flash3388.flashview.deploy.Remote;
import com.flash3388.flashview.deploy.SshjDeployer;
import com.flash3388.flashview.image.ImageLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException, InitializationException {
        Logger logger = new LoggerBuilder("flashview")
                .enableFileLogging(true)
                .build();

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Closer closer = Closer.empty();
        closer.add(new ExecutorCloser(executorService));

        Deployer deployer = new SshjDeployer(
                new Remote("admin", "", "roborio-3388-frc.local"),
                new File("/home/lvuser/SweetJesus.fv"));

        ImageLoader imageLoader = new ImageLoader();

        try {
            FlashView flashView = new FlashView(executorService, deployer, imageLoader, logger);
            flashView.start();
        } finally {
            closer.close();
        }
    }
}

package com.flash3388.flashview;

import com.flash3388.flashlib.io.Closer;
import com.flash3388.flashlib.util.concurrent.ExecutorCloser;
import com.flash3388.flashlib.util.logging.LoggerBuilder;
import com.flash3388.flashview.configuration.Configuration;
import com.flash3388.flashview.configuration.loader.ConfigurationLoadException;
import com.flash3388.flashview.configuration.loader.JsonConfigurationLoader;
import com.flash3388.flashview.deploy.Deployer;
import com.flash3388.flashview.deploy.SshjDeployer;
import com.flash3388.flashview.image.ImageLoader;
import com.google.gson.Gson;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final File CONFIGURATION_FILE = new File("config.json");

    public static void main(String[] args) throws IOException, InitializationException, ConfigurationLoadException {
        Logger logger = new LoggerBuilder("flashview")
                .enableFileLogging(true)
                .build();

        System.out.println(CONFIGURATION_FILE.getAbsolutePath());
        Configuration configuration;
        try (InputStream inputStream = new FileInputStream(CONFIGURATION_FILE)) {
            configuration = new JsonConfigurationLoader(new Gson()).load(inputStream);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Closer closer = Closer.empty();
        closer.add(new ExecutorCloser(executorService));

        Deployer deployer = new SshjDeployer(
                //new Remote("admin", "", "roborio-3388-frc.local"),
                //new File("/home/lvuser/SweetJesus.fv"),
                configuration.getRemote(),
                configuration.getDeploymentDestination(),
                logger);

        ImageLoader imageLoader = new ImageLoader();

        try {
            FlashView flashView = new FlashView(executorService, deployer, imageLoader, logger);
            flashView.start();
        } finally {
            closer.close();
        }
    }
}

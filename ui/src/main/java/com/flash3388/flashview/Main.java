package com.flash3388.flashview;

import com.castle.util.closeables.Closer;
import com.castle.util.logging.LoggerBuilder;
import com.flash3388.flashview.commands.CommandType;
import com.flash3388.flashview.config.Config;
import com.flash3388.flashview.config.JsonConfigLoader;
import com.flash3388.flashview.deploy.Deployer;
import com.flash3388.flashview.deploy.Destination;
import com.flash3388.flashview.deploy.Remote;
import com.flash3388.flashview.deploy.SshjDeployer;
import com.flash3388.flashview.image.ImageLoader;
import com.flash3388.flashview.io.CommandTypesLoader;
import com.flash3388.flashview.io.JsonCommandTypesLoader;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static net.sourceforge.argparse4j.impl.Arguments.*;

public class Main {

    private static final String DEFAULT_CONFIG_FILE_NAME = "config.json";
    private static final String DEFAULT_COMMAND_TYPES_FILE_NAME = "command-types.json";

    public static void main(String[] args) throws Exception {
        Logger logger = new LoggerBuilder("flashview")
                .enableConsoleLogging(true)
                .build();

        ProgramOptions programOptions = handleArguments(args);

        Path configFile = programOptions.getConfigFile().toPath();
        Config config = new JsonConfigLoader().load(configFile);

        Path commandTypesFile = programOptions.getCommandTypesFile().toPath();
        CommandTypesLoader commandTypesLoader = new JsonCommandTypesLoader();
        List<CommandType> commandTypes = commandTypesLoader.load(commandTypesFile);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Closer closer = Closer.empty();
        closer.add(executorService::shutdownNow);

        Deployer deployer = new SshjDeployer(
                config.getRemote(),//new Remote("admin", "", "roborio-3388-frc.local"),
                config.getDestination(),//new Destination("/home/lvuser/deploy/flashviewProgram.json"),
                commandTypesFile,
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

    private static ProgramOptions handleArguments(String[] args) throws ArgumentParserException {
        ArgumentParser parser = ArgumentParsers.newFor("FlashView")
                .build()
                .defaultHelp(true)
                .description("Automation Planner");

        String userDir = System.getProperty("user.dir");

        // config
        parser.addArgument("--config-file")
                .dest("config-file")
                .required(false)
                .type(String.class)
                .action(store())
                .setDefault(userDir.concat("/").concat(DEFAULT_CONFIG_FILE_NAME))
                .help("Path to the configuration file of the program");

        // command types
        parser.addArgument("--command-types-file")
                .dest("command-types-file")
                .required(false)
                .type(String.class)
                .action(store())
                .setDefault(userDir.concat("/").concat(DEFAULT_COMMAND_TYPES_FILE_NAME))
                .help("Path to the command types file of the program");


        Namespace namespace = parser.parseArgs(args);
        return new ProgramOptions(
                new File(namespace.getString("config-file")),
                new File(namespace.getString("command-types-file"))
        );
    }
}

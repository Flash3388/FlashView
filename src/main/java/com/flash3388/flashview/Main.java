package com.flash3388.flashview;

import com.flash3388.flashlib.io.Closer;
import com.flash3388.flashlib.util.concurrent.ExecutorCloser;
import com.flash3388.flashlib.util.logging.LoggerBuilder;
import org.slf4j.Logger;

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

        try {
            FlashView flashView = new FlashView(executorService, logger);
            flashView.start();
        } finally {
            closer.close();
        }
    }
}

package com.flash3388.flashview;

import java.io.File;

public class ProgramOptions {

    private final File mConfigFile;
    private final File mCommandTypesFile;

    public ProgramOptions(File configFile, File commandTypesFile) {
        mConfigFile = configFile;
        mCommandTypesFile = commandTypesFile;
    }

    public File getConfigFile() {
        return mConfigFile;
    }

    public File getCommandTypesFile() {
        return mCommandTypesFile;
    }
}

package com.flash3388.flashview.configuration;

import com.flash3388.flashview.deploy.Remote;

import java.io.File;

public class Configuration {

    private final Remote mRemote;
    private final File mDeploymentDestination;

    public Configuration(Remote remote, File deploymentDestination) {
        mRemote = remote;
        mDeploymentDestination = deploymentDestination;
    }

    public Remote getRemote() {
        return mRemote;
    }

    public File getDeploymentDestination() {
        return mDeploymentDestination;
    }
}

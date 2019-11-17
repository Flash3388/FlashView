package com.flash3388.flashview.configuration;

import com.flash3388.flashview.deploy.Destination;
import com.flash3388.flashview.deploy.Remote;

import java.io.File;

public class Configuration {

    private final Remote mRemote;
    private final Destination mDeploymentDestination;

    public Configuration(Remote remote, Destination deploymentDestination) {
        mRemote = remote;
        mDeploymentDestination = deploymentDestination;
    }

    public Remote getRemote() {
        return mRemote;
    }

    public Destination getDeploymentDestination() {
        return mDeploymentDestination;
    }
}

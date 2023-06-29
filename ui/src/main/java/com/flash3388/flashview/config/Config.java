package com.flash3388.flashview.config;

import com.flash3388.flashview.deploy.Destination;
import com.flash3388.flashview.deploy.Remote;

public class Config {

    private final Destination mDestination;
    private final Remote mRemote;

    public Config(Destination destination, Remote remote) {
        mDestination = destination;
        mRemote = remote;
    }

    public Destination getDestination() {
        return mDestination;
    }

    public Remote getRemote() {
        return mRemote;
    }
}

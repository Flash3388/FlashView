package com.flash3388.flashview.deploy;

import java.nio.file.Path;

public interface Deployer {

    void deploy(Path path) throws DeploymentException;

    class Stub implements Deployer {

        @Override
        public void deploy(Path path) throws DeploymentException {
            throw new DeploymentException(new UnsupportedOperationException("not supported"));
        }
    }
}

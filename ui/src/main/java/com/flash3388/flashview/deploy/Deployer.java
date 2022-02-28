package com.flash3388.flashview.deploy;

import com.google.gson.JsonElement;

public interface Deployer {

    void deploy(JsonElement data) throws DeploymentException;

    class Stub implements Deployer {

        @Override
        public void deploy(JsonElement data) throws DeploymentException {
            throw new DeploymentException(new UnsupportedOperationException("not supported"));
        }
    }
}

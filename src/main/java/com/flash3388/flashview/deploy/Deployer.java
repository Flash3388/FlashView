package com.flash3388.flashview.deploy;

import com.google.gson.JsonElement;

public interface Deployer {

    void deploy(JsonElement data) throws DeploymentException;
}

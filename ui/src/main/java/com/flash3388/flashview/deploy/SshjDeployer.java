package com.flash3388.flashview.deploy;

import com.google.gson.JsonElement;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class SshjDeployer implements Deployer {

    private final Remote mRemote;
    private final Destination mDestination;
    private final Logger mLogger;

    public SshjDeployer(Remote remote, Destination destination, Logger logger) {
        mRemote = remote;
        mDestination = destination;
        mLogger = logger;
    }

    @Override
    public void deploy(JsonElement data) throws DeploymentException {
        try {
            SSHClient client = new SSHClient();
            tryLoadKnownHosts(client);
            client.addHostKeyVerifier(new PromiscuousVerifier());

            client.connect(mRemote.getHostname());
            try {
                client.authPassword(mRemote.getUsername(), mRemote.getPassword());

                client.useCompression();

                File temp = File.createTempFile("deploy", "flashview");
                Files.write(temp.toPath(), Collections.singleton(data.toString()), new StandardOpenOption[]{StandardOpenOption.WRITE});

                client.newSCPFileTransfer().upload(
                        new FileSystemFile(temp.getAbsolutePath()),
                        mDestination.getPath());
            } finally {
                client.disconnect();
            }
        } catch (IOException e) {
            throw new DeploymentException(e);
        }
    }

    private void tryLoadKnownHosts(SSHClient client) {
        try {
            client.loadKnownHosts();
        } catch (IOException e) {
            e.printStackTrace();
            mLogger.warn("Unable to load known_hosts", e);
        }
    }
}

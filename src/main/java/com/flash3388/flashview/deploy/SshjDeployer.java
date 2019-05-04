package com.flash3388.flashview.deploy;

import com.google.gson.JsonElement;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class SshjDeployer implements Deployer {

    private final Remote mRemote;
    private final File mDestinationFile;

    public SshjDeployer(Remote remote, File destinationFile) {
        mRemote = remote;
        mDestinationFile = destinationFile;
    }

    @Override
    public void deploy(JsonElement data) throws DeploymentException {
        try {
            SSHClient client = new SSHClient();
            client.loadKnownHosts();
            client.addHostKeyVerifier(new PromiscuousVerifier());

            client.connect(mRemote.getHostname());
            try {
                client.authPassword(mRemote.getUsername(), mRemote.getPassword());

                client.useCompression();

                File temp = File.createTempFile("deploy", "flashview");
                Files.write(temp.toPath(), Collections.singleton(data.toString()), new StandardOpenOption[]{StandardOpenOption.WRITE});

                client.newSCPFileTransfer().upload(new FileSystemFile(temp.getAbsolutePath()), mDestinationFile.getAbsolutePath());
            } finally {
                client.disconnect();
            }
        } catch (IOException e) {
            throw new DeploymentException(e);
        }
    }
}

package com.flash3388.flashview.deploy;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import net.schmizz.sshj.xfer.scp.SCPFileTransfer;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class SshjDeployer implements Deployer {

    private static final String COMMAND_TYPES_FILE_NAME = "commandTypes.json";
    private static final String PROGRAM_FILE_NAME = "flashviewProgram.json";

    private final Remote mRemote;
    private final Destination mDestination;
    private final Path mCommandTypesFile;
    private final Logger mLogger;

    public SshjDeployer(Remote remote, Destination destination, Path commandTypesFile, Logger logger) {
        mRemote = remote;
        mDestination = destination;
        mCommandTypesFile = commandTypesFile;
        mLogger = logger;
    }

    @Override
    public void deploy(Path path) throws DeploymentException {
        try {
            SSHClient client = new SSHClient();
            tryLoadKnownHosts(client);
            client.addHostKeyVerifier(new PromiscuousVerifier());

            client.connect(mRemote.getHostname());
            try {
                client.authPassword(mRemote.getUsername(), mRemote.getPassword());
                client.useCompression();

                SCPFileTransfer transfer = client.newSCPFileTransfer();

                String uploadPath = mDestination.getPath();
                String programUploadPath = uploadPath.concat("/").concat(PROGRAM_FILE_NAME);
                String commandTypesUploadPath = uploadPath.concat("/").concat(COMMAND_TYPES_FILE_NAME);

                transfer.upload(new FileSystemFile(path.toFile().getAbsolutePath()), programUploadPath);
                transfer.upload(new FileSystemFile(mCommandTypesFile.toFile().getAbsolutePath()), commandTypesUploadPath);
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

package frc.team3388.flashview;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProgramReader {
    private Path path;

    public ProgramReader(String path) {
        this.path = Paths.get(path);
    }

    public BufferedReader tryReading() throws IOException{
        return Files.newBufferedReader(path);
    }
}

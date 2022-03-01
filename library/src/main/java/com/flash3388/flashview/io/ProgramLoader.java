package com.flash3388.flashview.io;

import com.flash3388.flashview.Program;

import java.io.IOException;
import java.nio.file.Path;

public interface ProgramLoader {

    Program load(Path path) throws IOException;
}

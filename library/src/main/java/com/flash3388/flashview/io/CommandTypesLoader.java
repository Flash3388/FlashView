package com.flash3388.flashview.io;

import com.flash3388.flashview.commands.CommandType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface CommandTypesLoader {

    List<CommandType> load(Path path) throws IOException;
}

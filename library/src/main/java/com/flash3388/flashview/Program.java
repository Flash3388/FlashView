package com.flash3388.flashview;

import com.flash3388.flashview.commands.Command;

import java.util.Collections;
import java.util.List;

public class Program {

    private final List<Command> mCommands;

    public Program(List<Command> commands) {
        mCommands = Collections.unmodifiableList(commands);
    }

    public List<Command> getCommands() {
        return mCommands;
    }
}

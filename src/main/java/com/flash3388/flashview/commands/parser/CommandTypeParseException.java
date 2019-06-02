package com.flash3388.flashview.commands.parser;

public class CommandTypeParseException extends Exception {

    public CommandTypeParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandTypeParseException(Throwable cause) {
        super(cause);
    }

    public CommandTypeParseException(String message) {
        super(message);
    }
}

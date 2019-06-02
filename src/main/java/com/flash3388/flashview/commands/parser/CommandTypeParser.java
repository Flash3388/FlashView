package com.flash3388.flashview.commands.parser;

import com.flash3388.flashview.commands.CommandType;

import java.io.InputStream;
import java.util.List;

public interface CommandTypeParser {

    List<CommandType> parse(InputStream inputStream) throws CommandTypeParseException;
}

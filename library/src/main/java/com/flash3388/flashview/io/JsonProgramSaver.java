package com.flash3388.flashview.io;

import com.flash3388.flashview.Program;
import com.flash3388.flashview.commands.Command;
import com.flash3388.flashview.commands.parameters.CommandParameterValue;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonProgramSaver implements ProgramSaver {

    private final Gson mGson;

    public JsonProgramSaver(Gson gson) {
        mGson = gson;
    }

    public JsonProgramSaver() {
        this(new Gson());
    }

    @Override
    public void save(Program program, Path path) throws IOException {
        try (BufferedWriter fileWriter = Files.newBufferedWriter(path);
             JsonWriter writer = mGson.newJsonWriter(fileWriter)) {
            writer.beginArray();
            for (Command command : program.getCommands()) {
                saveCommand(command, writer);
            }
            writer.endArray();
        }
    }

    private void saveCommand(Command command, JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name("type").value(command.getCommandType().getId());

        writer.name("params").beginArray();
        for (CommandParameterValue<?> parameter : command.getParameters()) {
            writer.beginObject();
            writer.name("name").value(parameter.getParameterType().getName());
            writer.name("value").value(parameter.getValue().toString());
            writer.endObject();
        }
        writer.endArray();

        writer.endObject();
    }
}

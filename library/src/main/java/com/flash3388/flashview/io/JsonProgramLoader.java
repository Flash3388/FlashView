package com.flash3388.flashview.io;

import com.flash3388.flashview.Program;
import com.flash3388.flashview.commands.Command;
import com.flash3388.flashview.commands.CommandType;
import com.flash3388.flashview.commands.parameters.CommandParameter;
import com.flash3388.flashview.commands.parameters.CommandParameterValue;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JsonProgramLoader implements ProgramLoader {

    private final Gson mGson;
    private final List<CommandType> mKnownCommandTypes;

    public JsonProgramLoader(Gson gson, List<CommandType> knownCommandTypes) {
        mGson = gson;
        mKnownCommandTypes = knownCommandTypes;
    }

    public JsonProgramLoader(List<CommandType> knownCommandTypes) {
        this(new Gson(), knownCommandTypes);
    }

    @Override
    public Program load(Path path) throws IOException {
        try (BufferedReader fileReader = Files.newBufferedReader(path);
             JsonReader reader = mGson.newJsonReader(fileReader)) {

            reader.beginArray();
            List<Command> commands = readCommands(reader);
            reader.endArray();

            return new Program(commands);
        }
    }

    private List<Command> readCommands(JsonReader reader) throws IOException {
        List<Command> commands = new ArrayList<>();

        while (reader.hasNext()) {
            commands.add(readCommand(reader));
        }

        return commands;
    }

    private Command readCommand(JsonReader reader) throws IOException {
        CommandType type = null;
        Map<String, String> paramsData = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equalsIgnoreCase("type")) {
                String typeString = reader.nextString();
                type = findCommandType(typeString);
            } else if (name.equalsIgnoreCase("params")) {
                paramsData = readCommandParams(reader);
            } else {
                throw new IOException("Unknown object entry: " + name);
            }
        }
        reader.endObject();

        return new Command(type, parseCommandParams(paramsData, type));
    }

    private Map<String, String> readCommandParams(JsonReader reader) throws IOException {
        Map<String, String> data = new HashMap<>();

        reader.beginArray();
        while (reader.hasNext()) {
            readCommandParamInto(reader, data);
        }
        reader.endArray();

        return data;
    }

    private void readCommandParamInto(JsonReader reader, Map<String, String> data) throws IOException {
        String paramName = null;
        String paramValue = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equalsIgnoreCase("name")) {
                paramName = reader.nextString();
            } else if (name.equalsIgnoreCase("value")) {
                paramValue = reader.nextString();
            } else {
                throw new IOException("Bad entry for param object " + name);
            }
        }
        reader.endObject();

        data.put(paramName, paramValue);
    }

    private List<CommandParameterValue<?>> parseCommandParams(Map<String, String> data, CommandType type) throws IOException {
        List<CommandParameterValue<?>> params = new ArrayList<>();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            CommandParameter<?> parameter = findCommandParameter(type, entry.getKey());
            String valueString = entry.getValue();

            Optional<?> result = parameter.getValueType().tryConvert(valueString);
            if (result.isPresent()) {
                params.add(new CommandParameterValue(parameter, result.get()));
            } else {
                throw new IOException("Bad value for param: " + parameter.getName());
            }
        }

        return params;
    }

    private CommandType findCommandType(String typeString) throws IOException {
        for (CommandType type : mKnownCommandTypes) {
            if (type.getId().equals(typeString)) {
                return type;
            }
        }

        throw new IOException("command type " + typeString + " unknown");
    }

    private CommandParameter<?> findCommandParameter(CommandType type, String name) throws IOException {
        for (CommandParameter<?> param : type.getParameters()) {
            if (param.getName().equalsIgnoreCase(name)) {
                return param;
            }
        }

        throw new IOException("command parameter " + name + " unknown");
    }
}

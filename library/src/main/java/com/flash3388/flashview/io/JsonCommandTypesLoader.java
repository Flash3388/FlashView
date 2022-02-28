package com.flash3388.flashview.io;

import com.flash3388.flashview.commands.CommandType;
import com.flash3388.flashview.commands.parameters.CommandParameter;
import com.flash3388.flashview.commands.parameters.CommandTypeImpl;
import com.flash3388.flashview.commands.parameters.KnownParameterTypes;
import com.flash3388.flashview.commands.parameters.range.Ranges;
import com.flash3388.flashview.commands.parameters.range.ValueRange;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonCommandTypesLoader implements CommandTypesLoader {

    private final Gson mGson;

    public JsonCommandTypesLoader(Gson gson) {
        mGson = gson;
    }

    public JsonCommandTypesLoader() {
        this(new Gson());
    }

    @Override
    public List<CommandType> load(Path path) throws IOException {
        List<CommandType> commandTypes = new ArrayList<>();

        try (BufferedReader fileReader = Files.newBufferedReader(path);
             JsonReader reader = mGson.newJsonReader(fileReader)) {
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                CommandType type = parseCommandType(reader);
                commandTypes.add(type);
                reader.endObject();
            }
            reader.endArray();
        }

        return commandTypes;
    }

    private CommandType parseCommandType(JsonReader reader) throws IOException {
        String id = null;
        String commandName = null;
        List<CommandParameter<?>> params = null;
        Map<String, String> additionalProperties = null;

        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equalsIgnoreCase("id")) {
                id = reader.nextString();
            } else if (name.equalsIgnoreCase("name")) {
                commandName = reader.nextString();
            } else if (name.equalsIgnoreCase("params")) {
                params = parseCommandTypeParams(reader);
            } else if (name.equalsIgnoreCase("additionalProperties")) {
                additionalProperties = parseCommandTypeProps(reader);
            } else {
                throw new IOException("Unknown value name in commandtype: " + name);
            }
        }

        return new CommandTypeImpl(id, commandName, params, additionalProperties);
    }

    private List<CommandParameter<?>> parseCommandTypeParams(JsonReader reader) throws IOException {
        List<CommandParameter<?>> params = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            params.add(parseCommandTypeParam(reader));
        }
        reader.endArray();

        return params;
    }

    private CommandParameter<?> parseCommandTypeParam(JsonReader reader) throws IOException {
        String paramName = null;
        String measurementUnit = null;
        KnownParameterTypes type = null;
        Map<String, Object> rangeData = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equalsIgnoreCase("name")) {
                paramName = reader.nextString();
            } else if (name.equalsIgnoreCase("measurementUnit")) {
                measurementUnit = reader.nextString();
            } else if (name.equalsIgnoreCase("type")) {
                String typeName = reader.nextString();
                type = KnownParameterTypes.valueOf(typeName);
            } else if (name.equalsIgnoreCase("range")) {
                rangeData = readParamValueRange(reader);
            } else {
                throw new IOException("Unknown value name in param: " + name);
            }
        }
        reader.endObject();

        ValueRange<?> range = parseParamRange(rangeData, type);
        return type.newParameter(paramName, measurementUnit, range);
    }

    private Map<String, Object> readParamValueRange(JsonReader reader) throws IOException {
        Map<String, Object> data = new HashMap<>();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            JsonToken token = reader.peek();

            switch (token) {
                case NUMBER:
                    data.put(name, reader.nextDouble());
                    break;
                default:
                    throw new IOException("Bad param value type: " + name);
            }
        }
        reader.endObject();

        return data;
    }

    private ValueRange<?> parseParamRange(Map<String, Object> data, KnownParameterTypes type) throws IOException {
        switch (type) {
            case DOUBLE: {
                double min = (double) data.get("min");
                double max = (double) data.get("max");

                return Ranges.doubleRange(min, max);
            }
            case INTEGER: {
                double min = (double) data.get("min");
                double max = (double) data.get("max");

                return Ranges.intRange((int) min, (int) max);
            }
            default:
                throw new IOException("Bad param type: " + type);
        }
    }

    private Map<String, String> parseCommandTypeProps(JsonReader reader) throws IOException {
        Map<String, String> props = new HashMap<>();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            String value = reader.nextString();

            props.put(name, value);
        }
        reader.endObject();

        return props;
    }
}

package com.flash3388.flashview.commands.parser;

import com.flash3388.flashview.commands.CommandType;
import com.flash3388.flashview.commands.CommandTypeBuilder;
import com.flash3388.flashview.commands.data.DataType;
import com.flash3388.flashview.commands.data.DataTypes;
import com.flash3388.flashview.commands.parameters.CommandParameterType;
import com.flash3388.flashview.image.ImageLoader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonCommandTypeParser implements CommandTypeParser {

    private final JsonParser mParser;
    private final ImageLoader mImageLoader;

    public JsonCommandTypeParser(JsonParser parser, ImageLoader imageLoader) {
        mParser = parser;
        mImageLoader = imageLoader;
    }

    @Override
    public List<CommandType> parse(InputStream inputStream) throws CommandTypeParseException {
        try {
            JsonElement root = mParser.parse(new InputStreamReader(inputStream));
            if (root.isJsonObject()) {
                return Collections.singletonList(parseSingle(root.getAsJsonObject()));
            }
            if (root.isJsonArray()) {
                return parseMultiple(root.getAsJsonArray());
            }

            throw new CommandTypeParseException("Unexpected root element type: " + root.toString());
        } catch (IOException e) {
            throw new CommandTypeParseException(e);
        }
    }

    private List<CommandType> parseMultiple(JsonArray array) throws CommandTypeParseException, IOException {
        List<CommandType> commandTypes = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonElement element = array.get(i);
            if (!element.isJsonObject()) {
                throw new CommandTypeParseException("CommandType element should be a jsonobject");
            }

            CommandType commandType = parseSingle(element.getAsJsonObject());
            commandTypes.add(commandType);
        }

        return commandTypes;
    }

    private CommandType parseSingle(JsonObject object) throws CommandTypeParseException, IOException {
        if (!object.has("name")) {
            throw new CommandTypeParseException("CommandType object missing `name`");
        }
        if (!object.has("iconPath")) {
            throw new CommandTypeParseException("CommandType object missing `iconPath`");
        }

        String name = object.get("name").getAsString();
        String displayName = object.has("displayName") ? object.get("displayName").getAsString() : name;
        String iconPath = object.get("iconPath").getAsString();

        CommandTypeBuilder builder = new CommandTypeBuilder(name, displayName)
                .setIconPath(iconPath);

        JsonElement parameters = object.get("parameters");
        if (parameters != null) {
            List<CommandParameterType> parameterTypes = parseParameters(parameters);
            for (CommandParameterType parameterType : parameterTypes) {
                builder.addParameter(parameterType);
            }
        }


        return builder.build(mImageLoader);
    }

    private List<CommandParameterType> parseParameters(JsonElement element) throws CommandTypeParseException {
        if (element.isJsonObject()) {
            return Collections.singletonList(parseParameter(element.getAsJsonObject()));
        }
        if (element.isJsonArray()) {
            return parseParameters(element.getAsJsonArray());
        }

        throw new CommandTypeParseException("Unexpected parameters element type: " + element.toString());
    }

    private List<CommandParameterType> parseParameters(JsonArray array) throws CommandTypeParseException {
        List<CommandParameterType> parameterTypes = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonElement element = array.get(i);
            if (!element.isJsonObject()) {
                throw new CommandTypeParseException("CommandParameterType element should be a jsonobject");
            }

            CommandParameterType parameterType = parseParameter(element.getAsJsonObject());
            parameterTypes.add(parameterType);
        }

        return parameterTypes;
    }

    private CommandParameterType parseParameter(JsonObject object) throws CommandTypeParseException {
        try {
            String name = object.get("name").getAsString();
            String measurementUnit = object.get("measurementUnit").getAsString();
            String dataTypeName = object.get("dataType").getAsString();

            DataType<?> dataType = DataTypes.forName(dataTypeName);

            return DataTypes.parameterType(dataType, name, measurementUnit);
        } catch (EnumConstantNotPresentException e) {
            throw new CommandTypeParseException("DataType invalid", e);
        }
    }
}

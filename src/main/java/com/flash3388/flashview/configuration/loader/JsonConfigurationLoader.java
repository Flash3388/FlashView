package com.flash3388.flashview.configuration.loader;

import com.flash3388.flashview.configuration.Configuration;
import com.flash3388.flashview.deploy.Remote;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class JsonConfigurationLoader implements ConfigurationLoader {

    private final Gson mGson;

    public JsonConfigurationLoader(Gson gson) {
        mGson = gson;
    }

    @Override
    public Configuration load(InputStream inputStream) throws ConfigurationLoadException {
        try {
            JsonReader reader = mGson.newJsonReader(new InputStreamReader(inputStream));
            reader.beginObject();
            try {
                Remote remote = null;
                File destination = null;

                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equalsIgnoreCase("remote")) {
                        remote = parseRemote(reader);
                    } else if (name.equalsIgnoreCase("destination")) {
                        destination = parseDestinationFile(reader);
                    } else {
                        continue;
                    }
                }

                if (remote == null || destination == null) {
                    throw new IOException("missing configuration data");
                }

                return new Configuration(remote, destination);
            } finally {
                reader.endObject();
            }
        } catch (IOException e) {
            throw new ConfigurationLoadException(e);
        }
    }

    private Remote parseRemote(JsonReader reader) throws IOException {
        reader.beginObject();
        try {
            Map<String, String> map = readerToMap(reader);
            return pareRemoteFromMap(map);
        } finally {
            reader.endObject();
        }
    }

    private Remote pareRemoteFromMap(Map<String, String> map) throws IOException {
        String username = map.get("username");
        String password = map.get("password");
        String hostname = map.get("hostname");

        if (username == null || password == null || hostname == null) {
            throw new IOException("missing attribute for remote");
        }

        return new Remote(
                username,
                password,
                hostname);
    }

    private File parseDestinationFile(JsonReader reader) throws IOException {
        reader.beginObject();
        try {
            Map<String, String> map = readerToMap(reader);
            return parseDestinationFromPath(map);
        } finally {
            reader.endObject();
        }
    }

    private File parseDestinationFromPath(Map<String, String> map) throws IOException {
        String path = map.get("path");

        if (path == null) {
            throw new IOException("path attributes missing");
        }

        return new File(path);
    }

    private Map<String, String> readerToMap(JsonReader reader) throws IOException {
        Map<String, String> map = new HashMap<>();

        while (reader.hasNext()) {
            String name = reader.nextName();
            String value = reader.nextString();

            map.put(name, value);
        }

        return map;
    }
}

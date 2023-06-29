package com.flash3388.flashview.config;

import com.flash3388.flashview.deploy.Destination;
import com.flash3388.flashview.deploy.Remote;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonConfigLoader {

    private final Gson mGson;

    public JsonConfigLoader(Gson gson) {
        mGson = gson;
    }

    public JsonConfigLoader() {
        this(new Gson());
    }

    public Config load(Path path) throws IOException {
        Destination destination = null;
        Remote remote = null;

        try (BufferedReader fileReader = Files.newBufferedReader(path);
             JsonReader reader = mGson.newJsonReader(fileReader)) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equalsIgnoreCase("destination")) {
                    reader.beginObject();
                    reader.nextName();
                    String destinationPath = reader.nextString();
                    destination = new Destination(destinationPath);
                    reader.endObject();
                } else if (name.equalsIgnoreCase("remote")) {
                    remote = parseRemote(reader);
                }
            }
            reader.endObject();
        }

        return new Config(destination, remote);
    }

    private Remote parseRemote(JsonReader reader) throws IOException {
        String username = null;
        String password = null;
        String hostname = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equalsIgnoreCase("username")) {
                username = reader.nextString();
            } else if (name.equalsIgnoreCase("password")) {
                password = reader.nextString();
            } else if (name.equalsIgnoreCase("hostname")) {
                hostname = reader.nextString();
            }
        }
        reader.endObject();

        assert username != null;
        assert password != null;
        assert hostname != null;

        return new Remote(username, password, hostname);
    }
}

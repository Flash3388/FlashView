package com.flash3388.flashview.configuration.loader;

import com.flash3388.flashview.configuration.Configuration;

import java.io.InputStream;

public interface ConfigurationLoader {

    Configuration load(InputStream inputStream) throws ConfigurationLoadException;
}

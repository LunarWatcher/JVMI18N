package io.github.lunarwatcher.jvmi18n.android;

import android.content.Context;
import io.github.lunarwatcher.jvmi18n.Loader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AndroidLoader implements Loader{
    private Map<String, Properties> cachedProperties;
    LoaderConfig config;

    public AndroidLoader(LoaderConfig config){
        if(config == null)
            throw new NullPointerException("Context cannot be null");
        this.config = config;
        cachedProperties = new HashMap<>();
    }

    @Override
    public Properties loadFile(String fileName) throws IOException {
        throw new RuntimeException("Stub!");
    }


}

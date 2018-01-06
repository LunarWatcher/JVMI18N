package io.github.lunarwatcher.jvmi18n.desktop;

import io.github.lunarwatcher.jvmi18n.Loader;
import io.github.lunarwatcher.jvmi18n.Translation;
import io.github.lunarwatcher.jvmi18n.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class DesktopLoader implements Loader {
    private HashMap<String, Properties> storedProps;

    public DesktopLoader() {
        storedProps = new HashMap<>();
    }

    public Properties loadFile(String fileName) throws IOException {
        if(storedProps.get(fileName) != null && !Translation.FORCE_FROM_FILE){
            //The property has already been loaded. Use cached file instead
            //of wasting memory and time re-loading the file
            System.out.println("Translation already loaded. Using cached version");
            return storedProps.get(fileName);
        }
        Properties properties = new Properties();
        InputStream stream = this.getClass().getResourceAsStream("/" + fileName);
        //Assert that the stream isn't null
        Utils.assertion(stream != null, "File not found: " + fileName);
        properties.load(stream);
        if(!Translation.FORCE_FROM_FILE)
            storedProps.put(fileName, properties);
        return properties;
    }
}

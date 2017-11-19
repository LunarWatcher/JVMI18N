package io.github.lunarwatcher.jvmi18n;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The core of the translator. It's package-private by default to prevent access to it from external sources.
 *
 * @since 1.0
 * @author LunarWatcher
 */
public class TranslationCore {
    Map<String, Properties> storedProps;

    public TranslationCore() {
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
        InputStream stream = new FileInputStream(new File(fileName));
        //Assert that the stream isn't null
        Utils.assertion(stream != null, "File not found: " + fileName);
        properties.load(stream);
        if(!Translation.FORCE_FROM_FILE)
            storedProps.put(fileName, properties);
        return properties;
    }
}

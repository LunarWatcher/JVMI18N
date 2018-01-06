package io.github.lunarwatcher.jvmi18n.android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import io.github.lunarwatcher.jvmi18n.Loader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AndroidLoader implements Loader{
    private Map<String, Properties> cachedProperties;
    LoaderConfig config;

    public AndroidLoader(LoaderConfig config){
        if(config == null)
            throw new NullPointerException("Config cannot be null");
        this.config = config;
        cachedProperties = new HashMap<>();
    }

    /**
     * Loads a file
     * @param fileName the filename to load
     * @return A properties file
     * @throws IOException For I/O issues
     * @throws SecurityException If using {@link LoadingMode#EXTERNAL_STORAGE} and the permissions aren't granted
     */
    @Override
    public Properties loadFile(String fileName) throws IOException {
        Properties properties = new Properties();

        switch(config.getMode()){
            case ASSETS:
                InputStreamReader in;
                try{
                    in = new InputStreamReader(config.getContext().getAssets().open(fileName));
                }catch(Exception e){
                    throw new RuntimeException(e);
                }
                properties.load(in);
                in.close();
                break;
            case EXTERNAL_STORAGE:
                PackageManager pm = config.getContext().getPackageManager();
                int hasWPermission = pm.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, config.getContext().getPackageName());
                int hasRPermission = pm.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, config.getContext().getPackageName());
                if(hasWPermission != PackageManager.PERMISSION_GRANTED || hasRPermission != PackageManager.PERMISSION_GRANTED){
                    Log.e("Permission", "Missing permissions!");
                    Log.e("Permission", "WRITE_EXTERNAL_STORAGE and/or READ_EXTERNAL_STORAGE is missing.");
                    Log.e("Permission", "If you have declared them, remember to request the permissions at runtime if you target API 23 or newer");
                    throw new SecurityException("Cannot read/write external storage");
                }

                File file = new File(Environment.getExternalStorageDirectory(), fileName);
                FileInputStream fis = new FileInputStream(file);
                properties.load(fis);
                break;
            case INTERNAL_STORAGE:
                FileInputStream input = config.getContext().openFileInput(fileName);
                properties.load(input);
                break;
        }

        return properties;
    }


}

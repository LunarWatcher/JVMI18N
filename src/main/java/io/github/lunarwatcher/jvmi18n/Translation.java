package io.github.lunarwatcher.jvmi18n;


import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The core of the program! This class contains the actual translation interface, and interfaces
 * with classes such as the TranslationCore and Bundle.
 * @author LunarWatcher
 */
public class Translation {
    /**
     * if the locale isn't supplied, use this locale. This is final as it can be overridden
     * by passing a non-null argument to the constructor.
     */
    public static final String DEFAULT_LOCALE = "en_US";
    /**
     * The extension to use. Defaults to .i18n
     * Can be changed through static access before loading anything
     */
    public static String EXTENSION = ".i18n";
    /**
     * Whether or not to throw an exception if the key isn't found in the translation.
     * The program throws an exception if the key isn't in the translation or the base
     * bundle independently of this setting.
     */
    public static boolean CRASH_IF_NOT_FOUND = false;

    /**
     * Whether or not to force the loader to load from the file whenever a file that's already
     * been loaded is loaded again. Defaults to false
     */
    public static boolean FORCE_FROM_FILE = false;
    /**
     * The current locale
     */
    public String locale;

    /**
     * The stored bundles
     */
    public List<Bundle> bundles;
    /**
     * Instance of the translation core
     */
    TranslationCore core;

    public Formatter formatter;

    /**
     * Creates a new translation object
     * @param locale The locale to use (in String format, equivalent to the name of the translation files)
     */
    public Translation(@Nullable String locale) {
        if(locale == null)
            locale = DEFAULT_LOCALE;
        this.locale = locale;
        bundles = new ArrayList<>();
        core = new TranslationCore();
    }

    /**
     * Add a new base bundle - a bundle without translations - by name
     * @param path The path of the bundle
     * @param name the bundle name
     * @return whether or not adding was successful
     * @throws IOException if the file is not found
     */
    public boolean addBaseBundle(String path, String name)throws IOException{
        Bundle bundle = new Bundle(path, name, this);
        for(Bundle b : bundles){
            if(b.getBasename().equals(name)){
                return false;
            }
        }
        bundles.add(bundle);
        return true;
    }

    /**
     * Add a new translation for a base bundle
     * @param base The name of the base bundle
     * @param locale the locale to add
     * @throws IllegalArgumentException When the base translation isn't found
     */
    public void addTranslation(String base, String locale) throws IllegalArgumentException{
        for(Bundle b : bundles){
            if(b.getBasename().equals(base)){
                b.configTranslation(locale);
                return;
            }
        }
        throw new IllegalArgumentException("Base translation not found: " + base);
    }

    /**
     * Get a String by accessing the 0th bundle in the list of Bundles
     * @param key The key to find
     * @return the String if found
     */
    public String get(String key){
        return this.get(key, bundles.get(0));
    }

    /**
     * Get a String by accessing the bundle by String
     * @param key the key to find
     * @param bundle the bundle to find in
     * @return the String if found
     */
    public String get(String key, String bundle){
        Bundle b = null;
        for(Bundle bund : bundles){
            if(bund.getBasename().equals(bundle)){
                b = bund;
                break;
            }
        }
        return this.get(key, b);
    }

    /**
     * Get a key by index in the ArrayList
     * @param key The key to find
     * @param bundleIndex The index for the bundle to find in
     * @return The String if found
     */
    public String get(String key, int bundleIndex){
        try{
            return get(key, bundles.get(bundleIndex));
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
            if(bundleIndex == 0)
                return null;
            return this.get(key, bundles.get(0));
        }
    }

    /**
     * Get a String from a specific bundle. Mostly used by the API to access the Strings,
     * the other get methods call this method with a bundle.
     * @param key The key to find
     * @param bundle the Bundle to pick from
     * @return the String if found, or RuntimeException if the key isn't found
     */
    public String get(String key, Bundle bundle){
        return bundle.get(key, locale);
    }

    /**
     * Add a single bundle to the List of available Bundles
     * @param b the bundle to add
     */
    public void addBundle(Bundle b){
        if(bundles.size() > 0){
            for(Bundle bundle : bundles){
                if(bundle.getBasename().equals(b.getBasename()))
                    return;
            }
        }
        bundles.add(b);
    }

    /**
     * Manually bulk-add bundles to the List of Bundles
     * @param b a List of bundles
     */
    public void addBundles(Bundle... b){
        for(Bundle bun : b){
            addBundle(bun);
        }
    }

    public void setFormatter(Formatter formatter){
        this.formatter = formatter;
    }

    public Formatter getFormatter(){
        return formatter;
    }

    /**
     *
     * @return the current locale
     */
    public String getLocale(){
        return locale;
    }

    /**
     * Set whether or not to crash if a key isn't found
     * @param notFound The new value
     */
    public static void setCrashIfNotFound(boolean notFound){
        CRASH_IF_NOT_FOUND = notFound;
    }

    public static void setForceFromFile(boolean nv){
        FORCE_FROM_FILE = nv;
    }

    /**
     * Set the locale. Affects what file the Strings are grabbed from
     * @param locale The new locale
     */
    public void setLocale(String locale){
        this.locale = locale;
    }

    public Bundle getBundle(String name){
        for(Bundle b : bundles){
            if(b.getBasename().equals(name))
                return b;
        }
        return null;
    }

    public List<Bundle> getBundles(){
        return bundles;
    }
}

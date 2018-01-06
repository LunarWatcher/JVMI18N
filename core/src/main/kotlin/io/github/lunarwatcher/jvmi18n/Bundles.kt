package io.github.lunarwatcher.jvmi18n

import java.io.IOException
import java.util.*

/**
 * The bundle class. Contains all added occurances of a bundle, and contains
 * the path, base name and more.
 *
 * @author LunarWatcher
 */
class Bundle{
    /**
     * The path to the bundle set
     */
    var path: String;
    /**
     * The name for the base bundle without extension
     */
    var basename: String;
    /**
     * The properties defined in the base file. Held externally from the
     * other translations
     */
    var base: Properties
    private set get

    /**
     * Map of the translations. The key is the locale and the value is the connected
     * properties.
     */
    var translations: MutableMap<String, Properties>

    /**
     * Holds the locales themselves, in String form. Used for checking whether or not
     * a given locale has been added, but also used to manage bulk adding.
     */
    var locales: MutableList<String>

    /**
     * Instance of the [Translation] class
     */
    var translation: Translation;

    constructor(path: String, basename: String, translation: Translation){
        translations = mutableMapOf<String, Properties>()
        locales = mutableListOf<String>()
        this.path = path;
        this.basename = basename;
        this.translation = translation;

        try{
            base = translation.core.loadFile(path + basename + Translation.EXTENSION)
            if(base == null)
                throw RuntimeException("Base translation cannot be null!");
        }catch(e: IOException){
            throw RuntimeException(e);
        }
    }

    /**
     * Add a translation using the String representing locale and an already loaded
     * properties file
     */
    fun addTranslation(locale: String, property: Properties){
        locales.add(locale);
        translations.put(locale, property);
    }

    /**
     * Add a single locale. Used for bulk adding without instantly
     * loading the files.
     */
    fun addLocale(locale: String){
        locales.add(locale);
    }

    fun addLocales(locales: Array<String>){
        for(locale in locales)
            addLocale(locale);
    }

    /**
     * Loads the locales that only have been added in String form as instances of Properties.
     * Only adds those that aren't already added
     */
    fun loadAll() {

        for(loc in locales){
            if(loc !in translations.keys){
                println("Locale not in loaded translations");
                try{
                    translations.put(loc,
                            translation.core
                                    .loadFile(path + basename
                                            + "-" + loc
                                            + Translation.EXTENSION));
                }catch(e: IOException){
                    println("Failed to load the translation for locale " + loc + " for bundle " + basename);
                }
            }
        }
        println("Loaded all remaining files");

    }

    fun configTranslation(locale: String){
        for(l in locales){
            if(l == locale){
                return;
            }
        }
        locales.add(locale);
        translations.put(locale, translation.core
                .loadFile(path + basename
                        + "-" + locale
                        + Translation.EXTENSION));
    }

    fun getBaseBundle() : Properties{
        return base;
    }

    fun get(key: String, locale: String): String {
        val prop: String? = translations.get(locale)?.getProperty(key);
        if (prop == null && Translation.CRASH_IF_NOT_FOUND && translations.isNotEmpty()){
            /**
             * Throws an exception if the property loaded is null, it should crash when a key isn't found (see [Translation.CRASH_IF_NOT_FOUND])
             * and the translation list doesn't contain anything. If the translation list is empty, assume the usage is for retrieving Strings
             * from a file and not translations. Because yes, that happens too
             */
            throw RuntimeException("Property not found: " + key);
        }else if(prop == null){
            val bProp: String = base.getProperty(key) ?: throw RuntimeException("Property (" + key + ") not found in translation or base bundle (" + basename + ")");
            return bProp;
        }
        return prop;
    }

    /**
     * Method mostly used for debug. Prints all the translations available for this bundle.
     */
    fun printLocales(){
        val set = translations.entries;
        System.out.println();
        System.out.println("The following translations have been added for the bundle " + basename + ":");

        for(s in set){
            System.out.println(s.key);
        }
        System.out.println("#########################################")
        System.out.println();
    }
}
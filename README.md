<h1><b style="color:red">NOTE!</b></h1>

You're currently on the nightly branch. This contains (most likely) untested new features. 

The current new feature being added is cross-platform support.

# About the project

This project was made in an attempt to improve the internationalization support already offered by Java. Since JVM is supported by multiple programming languages, this project can be used with Java, Kotlin, Scala or [any of the other JVM languages](http://www.oracle.com/technetwork/articles/java/architect-languages-2266279.html). 

The default `.properties` files is used, though the default file extension is `.i18n`. The syntax itself is still the same as the `.properties` file type. The file extension can be edited through the static variable in the Translation class.

In addition, the annoying `Locale` class is completely redundant in this setup. You can name the files whatever you want and use any locale setting you'd like. There's currently no support for default locale setting (meaning take the Strings from the file that corresponds with the locale). 

The code is designed for memory caching, so if a file is already loaded there's no point in re-loading it. The `TranslationCore` class is behind the core file loading, and contains the stored properties that have been loaded before. The caching can be disabled by setting the boolean `FORCE_FROM_FILE` in the Translation class to true. All of this is documented in the wiki.*



## File structure

The actual files themselves rely on a given structure. There are two types of files: the base file and the translation files.
The base file is (well, the base file) the file with the base translations. It **should** contain all the keys that show up in other translations. 

And yes, there is a naming convension of the files that must be followed to avoid crashes. (Everything not surrounded in `[]` is a fixed character. The name doesn't necessarily have to have brackets in them unless you need them for some reason).

Base file: `[name].[extension]`.
Translation file: `[name]-[locale].[extension]`

The name is dynamic, the locale as well. The extension is a pitfall, as it is set to default to `.i18n`. This means you have to either use that extension or change the extension defined in the code. This can be done by calling `Translation.EXTENSION = ".yourExtension"`.

If a base file isn't found when adding a translation, or the base file can't be loaded, you will get an exception thrown. This is worth noting as a possible cause for problems in production if the file isn't found for some reason. It will throw a RuntimeException or IOException, depending on where, so this is worth paying attention to.

And yes, the library has support for multiple bundles. For an instance:

    baseBundle.i18n
    gameStory.i18n
    characterClasses.i18n
    
Or whatever names and extensions you need. The translations for a given bundle has the same base name, but also contains `-[locale]`. Because the Locale class isn't involved, there's no need to follow a given naming convension. You can say that the american translation is `-american` and the russian one is `-russian`, but it can also be `-en_US` or `-ru_RU`. Whatever you pick has to be loaded through the code

# Setup

Refer the wiki for more details. 

# Current stable version: 1.0.1

Remember to update the dependency to the newest version

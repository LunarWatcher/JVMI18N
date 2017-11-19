package io.github.lunarwatcher.dummyapp;

import io.github.lunarwatcher.jvmi18n.DefaultFormatter;
import io.github.lunarwatcher.jvmi18n.Formatter;
import io.github.lunarwatcher.jvmi18n.Translation;

import java.io.IOException;
import java.util.Random;

class Test {
    public Test() {

    }

    public static void main(String[] args) throws IOException {
        Translation translation = new Translation("en_US");
        translation.addBaseBundle("res/", "base");
        translation.addTranslation("base", "en_US");
        translation.addTranslation("base", "nb_NO");
        translation.setLocale("ru_RU");
        DefaultFormatter formatter = new DefaultFormatter(translation);
        String unformatted = translation.get("formattable");
        System.out.println("Line needs " + formatter.scanLine(unformatted) + " references");
        for(int i = -1000; i <= 1000; i++) {
            // Don't do this in production.
            String formatted = formatter.formatInput(unformatted, new Object[]{translation.get("something"), i});
            System.out.println(formatted);
        }

    }
}

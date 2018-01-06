package io.github.lunarwatcher.jvmi18n;

import java.io.IOException;
import java.util.Properties;

public interface Loader {
    Properties loadFile(String fileName) throws IOException;
}

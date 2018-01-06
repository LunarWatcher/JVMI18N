import io.github.lunarwatcher.jvmi18n.Translation;
import io.github.lunarwatcher.jvmi18n.desktop.DesktopLoader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.fail;

public class DesktopTest {
    public DesktopTest() {

    }

    @Test
    public void resources() {
        Translation translation = new Translation("en_US", new DesktopLoader());
        try {
            translation.addBaseBundle("", "base.i18n");
        }catch(IOException e){
            e.printStackTrace();
            fail("File not found");
        }
        System.out.println(translation.get("appName"));
        System.out.println(translation.get("defaultConfig"));
        translation.addTranslation("base", "en_US");
        System.out.println(translation.get("defaultConfig"));
    }
}

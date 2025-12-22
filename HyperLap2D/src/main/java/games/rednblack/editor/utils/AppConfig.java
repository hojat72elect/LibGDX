package games.rednblack.editor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import games.rednblack.editor.Main;
import games.rednblack.editor.renderer.utils.Version;

public class AppConfig {

    public static AppConfig instance;

    public String versionString;
    public Version version;
    public String build;

    public Properties properties;

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
            instance.loadProperties();
        }

        return instance;
    }

    private void loadProperties() {
        File root = new File(new File(".").getAbsolutePath()).getParentFile();
        File configDir = new File(root.getAbsolutePath() + File.separator + "configs");

        properties = new Properties();
        InputStream propertiesInput = null;

        File file = new File(configDir.getAbsolutePath() + File.separator + "app.properties");
        if (file.exists()) {
            try {
                propertiesInput = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            propertiesInput = Main.class.getClassLoader().getResourceAsStream("configs/app.properties");
        }

        if (propertiesInput != null) {
            try {
                properties.load(propertiesInput);
                versionString = properties.getProperty("version");
                version = new Version(versionString.replaceAll("[^0-9\\\\.]", ""));
                build = properties.getProperty("build");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

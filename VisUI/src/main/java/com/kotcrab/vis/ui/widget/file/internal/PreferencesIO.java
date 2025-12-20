package com.kotcrab.vis.ui.widget.file.internal;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class PreferencesIO {
    private static final String VIS_DEFAULT_PREFS_NAME = "com.kotcrab.vis.ui.widget.file.filechooser_favorites";
    private static String defaultPrefsName = VIS_DEFAULT_PREFS_NAME;

    private final String favoritesKeyName = "favorites";
    private final String recentDirKeyName = "recentDirectories";
    private final String lastDirKeyName = "lastDirectory";

    private final Preferences prefs;
    private final Json json = new Json();

    public PreferencesIO() {
        this(defaultPrefsName);
    }

    public PreferencesIO(String prefsName) {
        prefs = Gdx.app.getPreferences(prefsName);
        checkIfUsingDefaultName();
    }

    public static void setDefaultPrefsName(String prefsName) {
        if (prefsName == null) throw new IllegalStateException("prefsName can't be null");
        PreferencesIO.defaultPrefsName = prefsName;
    }

    public void checkIfUsingDefaultName() {
        if (defaultPrefsName.equals(VIS_DEFAULT_PREFS_NAME)) {
            Gdx.app.log("VisUI", "Warning, using default preferences file name for file chooser! (see FileChooser.setDefaultPrefsName(String))");
        }
    }

    public Array<FileHandle> loadFavorites() {
        String data = prefs.getString(favoritesKeyName, null);
        if (data == null)
            return new Array<FileHandle>();
        else
            return json.fromJson(FileArrayData.class, data).toFileHandleArray();
    }

    public void saveFavorites(Array<FileHandle> favorites) {
        prefs.putString(favoritesKeyName, json.toJson(new FileArrayData(favorites)));
        prefs.flush();
    }

    public Array<FileHandle> loadRecentDirectories() {
        String data = prefs.getString(recentDirKeyName, null);
        if (data == null)
            return new Array<FileHandle>();
        else
            return json.fromJson(FileArrayData.class, data).toFileHandleArray();
    }

    public void saveRecentDirectories(Array<FileHandle> recentDirs) {
        prefs.putString(recentDirKeyName, json.toJson(new FileArrayData(recentDirs)));
        prefs.flush();
    }

    public FileHandle loadLastDirectory() {
        String data = prefs.getString(lastDirKeyName, null);
        if (data == null) return null;
        return json.fromJson(FileHandleData.class, data).toFileHandle();
    }

    public void saveLastDirectory(FileHandle file) {
        prefs.putString(lastDirKeyName, json.toJson(new FileHandleData(file)));
        prefs.flush();
    }

    private static class FileArrayData {
        public Array<FileHandleData> data;

        public FileArrayData() {

        }

        public FileArrayData(Array<FileHandle> favourites) {
            data = new Array<FileHandleData>();
            for (FileHandle file : favourites)
                data.add(new FileHandleData(file));
        }

        public Array<FileHandle> toFileHandleArray() {
            Array<FileHandle> files = new Array<FileHandle>();

            for (FileHandleData fileData : data) {
                files.add(fileData.toFileHandle());
            }

            return files;
        }
    }

    private static class FileHandleData {
        public FileType type;
        public String path;

        public FileHandleData() {
        }

        public FileHandleData(FileHandle file) {
            type = file.type();
            path = file.path();
        }

        public FileHandle toFileHandle() {
            switch (type) {
                case Absolute:
                    return Gdx.files.absolute(path);
                case Classpath:
                    return Gdx.files.classpath(path);
                case External:
                    return Gdx.files.external(path);
                case Internal:
                    return Gdx.files.internal(path);
                case Local:
                    return Gdx.files.local(path);
                default:
                    throw new IllegalStateException("Unknown file type!");
            }
        }
    }
}

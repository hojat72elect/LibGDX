package games.rednblack.editor.data.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.Map;

public class PreferencesManager {
    private static final String TAG = PreferencesManager.class.getCanonicalName();
    private static PreferencesManager instance = null;
    private final Preferences prefs;
    // Recent Project Manager
    // Count: recentHistory
    // Array: recent.0, recent.1, recent.2, etc, etc.
    private ArrayList<String> recentHistory;

    public PreferencesManager() {
        prefs = Gdx.app.getPreferences("HyperLap2DPrefs");
        initPrefs();
    }

    public static PreferencesManager getInstance() {
        if (instance == null)
            instance = new PreferencesManager();

        return instance;
    }

    private void initPrefs() {
        if (!contains("recentHistory")) {
            putInteger("recentHistory", 10);
            flush();
        }
    }

    // Map to Prefs
    public boolean contains(String key) {
        return prefs.contains(key);
    }

    public void flush() {
        prefs.flush();
    }

    public Map<String, ?> get() {
        return prefs.get();
    }

    public boolean getBoolean(String key) {
        return prefs.getBoolean(key);
    }

    public boolean getBoolean(String key, boolean defVal) {
        return prefs.getBoolean(key, defVal);
    }

    public float getFloat(String key) {
        return prefs.getFloat(key);
    }

    public float getFloat(String key, float defVal) {
        return prefs.getFloat(key, defVal);
    }

    public int getInteger(String key) {
        return prefs.getInteger(key);
    }

    public int getInteger(String key, int defVal) {
        return prefs.getInteger(key, defVal);
    }

    public long getLong(String key) {
        return prefs.getLong(key);
    }

    public long getLong(String key, long defVal) {
        return prefs.getLong(key, defVal);
    }

    public String getString(String key) {
        return prefs.getString(key);
    }

    public String getString(String key, String defVal) {
        return prefs.getString(key, defVal);
    }

    public Preferences put(Map<String, ?> vals) {
        return prefs.put(vals);
    }

    public Preferences putBoolean(String key, boolean val) {
        return prefs.putBoolean(key, val);
    }

    public Preferences putFloat(String key, float val) {
        return prefs.putFloat(key, val);
    }

    public Preferences putInteger(String key, int val) {
        return prefs.putInteger(key, val);
    }

    public Preferences putLong(String key, int val) {
        return prefs.putLong(key, val);
    }

    public Preferences putString(String key, String val) {
        return prefs.putString(key, val);
    }

    // Custom Functions

    public void remove(String key) {
        prefs.remove(key);
    }

    public void buildRecentHistory() {
        recentHistory = new ArrayList<String>();
        for (int i = 0; i < getInteger("recentHistory"); i++) {
            if (!contains(String.format("recent.%d", i))) {
                break;
            }
            String project = getString(String.format("recent.%d", i));
            java.io.File file = new java.io.File(project);
            if (file.exists() && file.isFile() && file.canRead()) {
                recentHistory.add(project);
            }
        }
    }

    public ArrayList<String> getRecentHistory() {
        return recentHistory;
    }

    public void storeRecentHistory() {
        cleanDuplicates(recentHistory);
        for (int i = 0; i < recentHistory.size(); i++) {
            if (i > getInteger("recentHistory"))
                break;
            prefs.remove(String.format("recent.%d", i));
            prefs.putString(String.format("recent.%d", i), recentHistory.get(i));
        }
        flush();
    }

    private void cleanDuplicates(ArrayList<String> paths) {
        Array<Integer> duplicates = new Array<>();
        for (int i = 0; i < paths.size() - 1; i++) {
            if (duplicates.contains(i, false)) continue;
            for (int j = i + 1; j < paths.size(); j++) {
                if (FilenameUtils.equalsNormalized(paths.get(i), paths.get(j))) {
                    duplicates.add(j);
                }
            }
        }
        duplicates.sort();
        duplicates.reverse();
        for (int i = 0; i < duplicates.size; i++) {
            paths.remove((int) duplicates.get(i));
        }
    }

    public void pushHistory(String file) {
        if (recentHistory.contains(file))
            popHistory(file);
        recentHistory.add(0, file);
        storeRecentHistory();
    }

    public void popHistory(String file) {
        if (recentHistory.contains(file)) {
            recentHistory.remove(file);
            storeRecentHistory();
        }
    }

    public void popHistory() {
        recentHistory.remove(-1);
        storeRecentHistory();
    }

    public void clearHistory() {
        for (int i = 0; i < recentHistory.size(); i++) {
            if (i > getInteger("recentHistory"))
                break;
            prefs.remove(String.format("recent.%d", i));
        }
        flush();
        recentHistory.clear();
    }
}

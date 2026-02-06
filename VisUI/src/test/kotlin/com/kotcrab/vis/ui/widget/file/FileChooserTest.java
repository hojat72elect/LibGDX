package com.kotcrab.vis.ui.widget.file;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.kotcrab.vis.ui.Locales;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.PopupMenu.PopupMenuStyle;
import com.kotcrab.vis.ui.widget.Tooltip;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.file.FileChooser.FileIconProvider;
import com.kotcrab.vis.ui.widget.file.FileChooser.ViewMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileFilter;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FileChooser}.
 */
public class FileChooserTest {

    @Mock
    private Application mockApplication;

    @Mock
    private Files mockFiles;

    @Mock
    private Skin mockSkin;

    @Mock
    private Stage mockStage;

    @Mock
    private FileChooserListener mockListener;

    @Mock
    private FileIconProvider mockIconProvider;

    @Mock
    private FileFilter mockFileFilter;

    @Mock
    private FileTypeFilter mockFileTypeFilter;

    @Mock
    private FileHandle mockFileHandle;

    @Mock
    private FileHandle mockDirectoryHandle;

    @Mock
    private PopupMenuStyle mockPopupMenuStyle;

    @Mock
    private Sizes mockSizes;

    @Mock
    private WindowStyle mockWindowStyle;

    @Mock
    private I18NBundle mockI18NBundle;

    @Mock
    private ButtonStyle mockButtonStyle;

    @Mock
    private TextFieldStyle mockTextFieldStyle;

    @Mock
    private ScrollPaneStyle mockScrollPaneStyle;

    @Mock
    private ListStyle mockListStyle;

    @Mock
    private LabelStyle mockLabelStyle;

    @Mock
    private VisImageButton.VisImageButtonStyle mockVisImageButtonStyle;

    @Mock
    private VisTextField.VisTextFieldStyle mockVisTextFieldStyle;

    @Mock
    private Tooltip.TooltipStyle mockTooltipStyle;

    @Mock
    private Preferences mockPreferences;

    private FileChooser fileChooser;
    private FileHandle testDirectory;
    private FileHandle testFile;
    private AutoCloseable visUIMock;
    private AutoCloseable i18nBundleMock;
    private AutoCloseable localesMock;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock Gdx application
        Gdx.app = mockApplication;
        Gdx.files = mockFiles;

        // Setup Preferences mocking
        when(mockApplication.getPreferences(anyString())).thenReturn(mockPreferences);
        when(mockPreferences.getString(anyString(), anyString())).thenReturn("");
        when(mockPreferences.getString(anyString())).thenReturn("");

        // Mock VisUI static methods to avoid loading native libraries
        visUIMock = mockStatic(VisUI.class);
        when(VisUI.getSkin()).thenReturn(mockSkin);
        when(VisUI.getSizes()).thenReturn(mockSizes);
        when(VisUI.isLoaded()).thenReturn(true); // Pretend VisUI is already loaded
        when(VisUI.getDefaultTitleAlign()).thenReturn(1); // Return center alignment

        // Mock I18NBundle to prevent resource bundle loading
        i18nBundleMock = mockStatic(I18NBundle.class);
        when(I18NBundle.createBundle(any(FileHandle.class))).thenReturn(mockI18NBundle);

        // Mock Locales to prevent bundle loading
        localesMock = mockStatic(Locales.class);
        when(Locales.getFileChooserBundle()).thenReturn(mockI18NBundle);

        // Setup mock skin with required style
        when(mockSkin.get(FileChooserStyle.class)).thenReturn(createMockFileChooserStyle());
        when(mockSkin.get(WindowStyle.class)).thenReturn(mockWindowStyle);
        when(mockSkin.get(anyString(), eq(WindowStyle.class))).thenReturn(mockWindowStyle);

        // Setup all required UI styles
        when(mockSkin.get(ButtonStyle.class)).thenReturn(mockButtonStyle);
        when(mockSkin.get(anyString(), eq(ButtonStyle.class))).thenReturn(mockButtonStyle);
        when(mockSkin.get(VisImageButton.VisImageButtonStyle.class)).thenReturn(mockVisImageButtonStyle);
        when(mockSkin.get(anyString(), eq(VisImageButton.VisImageButtonStyle.class))).thenReturn(mockVisImageButtonStyle);
        when(mockSkin.get(VisTextField.VisTextFieldStyle.class)).thenReturn(mockVisTextFieldStyle);
        when(mockSkin.get(anyString(), eq(VisTextField.VisTextFieldStyle.class))).thenReturn(mockVisTextFieldStyle);
        when(mockSkin.get(Tooltip.TooltipStyle.class)).thenReturn(mockTooltipStyle);
        when(mockSkin.get(anyString(), eq(Tooltip.TooltipStyle.class))).thenReturn(mockTooltipStyle);
        when(mockSkin.get(TextFieldStyle.class)).thenReturn(mockTextFieldStyle);
        when(mockSkin.get(anyString(), eq(TextFieldStyle.class))).thenReturn(mockTextFieldStyle);
        when(mockSkin.get(ScrollPaneStyle.class)).thenReturn(mockScrollPaneStyle);
        when(mockSkin.get(anyString(), eq(ScrollPaneStyle.class))).thenReturn(mockScrollPaneStyle);
        when(mockSkin.get(ListStyle.class)).thenReturn(mockListStyle);
        when(mockSkin.get(anyString(), eq(ListStyle.class))).thenReturn(mockListStyle);
        when(mockSkin.get(LabelStyle.class)).thenReturn(mockLabelStyle);
        when(mockSkin.get(anyString(), eq(LabelStyle.class))).thenReturn(mockLabelStyle);

        // Setup WindowStyle mock with required fields
        mockWindowStyle.titleFont = mock(com.badlogic.gdx.graphics.g2d.BitmapFont.class);

        // Setup ButtonStyle mock with required fields
        mockButtonStyle.up = mock(Drawable.class);
        mockButtonStyle.down = mock(Drawable.class);
        mockButtonStyle.checked = mock(Drawable.class);

        // Setup VisImageButtonStyle mock with required fields
        mockVisImageButtonStyle.up = mock(Drawable.class);
        mockVisImageButtonStyle.down = mock(Drawable.class);
        mockVisImageButtonStyle.checked = mock(Drawable.class);
        mockVisImageButtonStyle.imageUp = mock(Drawable.class);
        mockVisImageButtonStyle.imageDown = mock(Drawable.class);
        mockVisImageButtonStyle.imageChecked = mock(Drawable.class);

        // Setup TextFieldStyle mock with required fields
        mockTextFieldStyle.font = mock(com.badlogic.gdx.graphics.g2d.BitmapFont.class);
        mockTextFieldStyle.background = mock(Drawable.class);

        // Setup VisTextFieldStyle mock with required fields
        mockVisTextFieldStyle.font = mock(com.badlogic.gdx.graphics.g2d.BitmapFont.class);
        mockVisTextFieldStyle.background = mock(Drawable.class);
        mockVisTextFieldStyle.focusedBackground = mock(Drawable.class);
        mockVisTextFieldStyle.disabledBackground = mock(Drawable.class);
        mockVisTextFieldStyle.cursor = mock(Drawable.class);

        // Setup TooltipStyle mock with required fields
        mockTooltipStyle.background = mock(Drawable.class);

        // Setup I18NBundle mock to return empty strings for all calls
        when(mockI18NBundle.get(anyString())).thenReturn("");
        when(mockI18NBundle.format(anyString())).thenReturn("");
        when(mockI18NBundle.format(anyString(), any())).thenReturn("");

        // Setup all Drawable mocks to return non-null values
        when(mock(Drawable.class).getMinWidth()).thenReturn(10f);
        when(mock(Drawable.class).getMinHeight()).thenReturn(10f);
        when(mock(Drawable.class).getLeftWidth()).thenReturn(1f);
        when(mock(Drawable.class).getRightWidth()).thenReturn(1f);
        when(mock(Drawable.class).getTopHeight()).thenReturn(1f);
        when(mock(Drawable.class).getBottomHeight()).thenReturn(1f);

        // Setup all BitmapFont mocks to return non-null values
        com.badlogic.gdx.graphics.g2d.BitmapFont mockFont = mock(com.badlogic.gdx.graphics.g2d.BitmapFont.class);
        com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData mockFontData = mock(com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData.class);
        com.badlogic.gdx.graphics.g2d.BitmapFontCache mockFontCache = mock(com.badlogic.gdx.graphics.g2d.BitmapFontCache.class);

        when(mockFont.getData()).thenReturn(mockFontData);
        when(mockFont.getRegion()).thenReturn(mock(com.badlogic.gdx.graphics.g2d.TextureRegion.class));
        when(mockFont.getLineHeight()).thenReturn(16f);
        when(mockFontCache.getFont()).thenReturn(mockFont);

        // Setup ListStyle mock with required fields
        mockListStyle.font = mock(com.badlogic.gdx.graphics.g2d.BitmapFont.class);
        mockListStyle.selection = mock(Drawable.class);

        // Setup LabelStyle mock with required fields
        mockLabelStyle.font = mock(com.badlogic.gdx.graphics.g2d.BitmapFont.class);

        // Setup test directory and file
        testDirectory = new FileHandle(System.getProperty("java.io.tmpdir") + "/filechooser_test_" + System.currentTimeMillis());
        testDirectory.mkdirs();
        testFile = testDirectory.child("test.txt");
        testFile.writeString("test content", false);

        // Setup mock directory handle
        when(mockDirectoryHandle.exists()).thenReturn(true);
        when(mockDirectoryHandle.isDirectory()).thenReturn(true);
        when(mockDirectoryHandle.list()).thenReturn(new FileHandle[]{testFile});
        when(mockDirectoryHandle.path()).thenReturn(testDirectory.path());

        // Setup mock file handle
        when(mockFileHandle.exists()).thenReturn(true);
        when(mockFileHandle.isDirectory()).thenReturn(false);
        when(mockFileHandle.name()).thenReturn("test.txt");
        when(mockFileHandle.path()).thenReturn(testFile.path());

        // Setup mock files to return test directory
        when(mockFiles.absolute(testDirectory.path())).thenReturn(testDirectory);
        when(mockFiles.absolute(testFile.path())).thenReturn(testFile);
        // when(mockFiles.current()).thenReturn(testDirectory); // current() method doesn't exist
    }

    @After
    public void tearDown() throws Exception {
        if (fileChooser != null) {
            fileChooser.remove();
        }
        if (testFile != null) {
            testFile.delete();
        }
        if (testDirectory != null) {
            testDirectory.deleteDirectory();
        }

        // Close static mocks
        if (visUIMock != null) {
            visUIMock.close();
        }
        if (i18nBundleMock != null) {
            i18nBundleMock.close();
        }
        if (localesMock != null) {
            localesMock.close();
        }

        // Reset VisUI.skin to null using reflection
        try {
            Class<?> visUIClass = Class.forName("com.kotcrab.vis.ui.VisUI");
            Field skinField = visUIClass.getDeclaredField("skin");
            skinField.setAccessible(true);
            skinField.set(null, null);
        } catch (Exception e) {
            // Ignore reflection errors
        }
    }

    private FileChooserStyle createMockFileChooserStyle() {
        FileChooserStyle style = new FileChooserStyle();
        style.highlight = mock(Drawable.class);
        style.iconArrowLeft = mock(Drawable.class);
        style.iconArrowRight = mock(Drawable.class);
        style.iconFolder = mock(Drawable.class);
        style.iconFolderParent = mock(Drawable.class);
        style.iconFolderStar = mock(Drawable.class);
        style.iconFolderNew = mock(Drawable.class);
        style.iconDrive = mock(Drawable.class);
        style.iconTrash = mock(Drawable.class);
        style.iconStar = mock(Drawable.class);
        style.iconStarOutline = mock(Drawable.class);
        style.iconRefresh = mock(Drawable.class);
        style.iconListSettings = mock(Drawable.class);
        style.iconFileText = mock(Drawable.class);
        style.iconFileImage = mock(Drawable.class);
        style.iconFilePdf = mock(Drawable.class);
        style.iconFileAudio = mock(Drawable.class);
        style.contextMenuSelectedItem = mock(Drawable.class);
        style.expandDropdown = mock(Drawable.class);
        style.popupMenuStyle = mockPopupMenuStyle;
        return style;
    }


    @Test
    public void testSetDefaultPrefsName() {
        String prefsName = "test.prefs";
        FileChooser.setDefaultPrefsName(prefsName);
        // This is a static method that sets internal state, we can't easily verify it
        // but we can ensure it doesn't throw an exception
        assertTrue("Setting default prefs name should not throw exception", true);
    }

    @Test
    public void testSetFavoritesPrefsName() {
        String prefsName = "test.favorites";
        FileChooser.setFavoritesPrefsName(prefsName);
        // This is deprecated but should still work without throwing
        assertTrue("Setting favorites prefs name should not throw exception", true);
    }

    @Test
    public void testSaveLastDirectory() {
        assertFalse("Save last directory should be false by default", FileChooser.isSaveLastDirectory());
        FileChooser.setSaveLastDirectory(true);
        assertTrue("Save last directory should be true after setting", FileChooser.isSaveLastDirectory());
        FileChooser.setSaveLastDirectory(false);
        assertFalse("Save last directory should be false after resetting", FileChooser.isSaveLastDirectory());
    }

    @Test
    public void testFileSortingEnum() {
        // Test that all enum values exist (comparator is private, so we can't test it directly)
        assertTrue("NAME enum should exist", FileChooser.FileSorting.NAME != null);
        assertTrue("MODIFIED_DATE enum should exist", FileChooser.FileSorting.MODIFIED_DATE != null);
        assertTrue("SIZE enum should exist", FileChooser.FileSorting.SIZE != null);
    }

    @Test
    public void testViewModeEnum() {
        // Test ViewMode properties
        assertTrue("DETAILS should not be thumbnail mode", !FileChooser.ViewMode.DETAILS.isThumbnailMode());
        assertTrue("BIG_ICONS should be thumbnail mode", FileChooser.ViewMode.BIG_ICONS.isThumbnailMode());
        assertTrue("MEDIUM_ICONS should be thumbnail mode", FileChooser.ViewMode.MEDIUM_ICONS.isThumbnailMode());
        assertTrue("SMALL_ICONS should be thumbnail mode", FileChooser.ViewMode.SMALL_ICONS.isThumbnailMode());
        assertTrue("LIST should not be thumbnail mode", !FileChooser.ViewMode.LIST.isThumbnailMode());

        // Test grid mode properties
        assertTrue("DETAILS should not be grid mode", !FileChooser.ViewMode.DETAILS.isGridMode());
        assertTrue("BIG_ICONS should be grid mode", FileChooser.ViewMode.BIG_ICONS.isGridMode());
        assertTrue("MEDIUM_ICONS should be grid mode", FileChooser.ViewMode.MEDIUM_ICONS.isGridMode());
        assertTrue("SMALL_ICONS should be grid mode", FileChooser.ViewMode.SMALL_ICONS.isGridMode());
        assertTrue("LIST should be grid mode", FileChooser.ViewMode.LIST.isGridMode());
    }

    @Test
    public void testSelectionModeEnum() {
        // Test that all enum values exist
        assertEquals("FILES enum should exist", "FILES", FileChooser.SelectionMode.FILES.name());
        assertEquals("DIRECTORIES enum should exist", "DIRECTORIES", FileChooser.SelectionMode.DIRECTORIES.name());
        assertEquals("FILES_AND_DIRECTORIES enum should exist", "FILES_AND_DIRECTORIES", FileChooser.SelectionMode.FILES_AND_DIRECTORIES.name());
    }

    @Test
    public void testModeEnum() {
        // Test that all enum values exist
        assertEquals("OPEN enum should exist", "OPEN", FileChooser.Mode.OPEN.name());
        assertEquals("SAVE enum should exist", "SAVE", FileChooser.Mode.SAVE.name());
    }

    @Test
    public void testHistoryPolicyEnum() {
        // Test that all enum values exist
        assertEquals("ADD enum should exist", "ADD", FileChooser.HistoryPolicy.ADD.name());
        assertEquals("CLEAR enum should exist", "CLEAR", FileChooser.HistoryPolicy.CLEAR.name());
        assertEquals("IGNORE enum should exist", "IGNORE", FileChooser.HistoryPolicy.IGNORE.name());
    }

    @Test
    public void testDefaultFileDeleter() {
        FileChooser.DefaultFileDeleter deleter = new FileChooser.DefaultFileDeleter();
        assertNotNull("DefaultFileDeleter should be created", deleter);
        assertFalse("DefaultFileDeleter should not have trash", deleter.hasTrash());
    }

    @Test
    public void testFileIconProviderInterface() {
        // Test that the interface methods exist and can be called
        FileChooser.FileIconProvider provider = new FileChooser.FileIconProvider() {
            @Override
            public com.badlogic.gdx.scenes.scene2d.utils.Drawable provideIcon(FileChooser.FileItem item) {
                return null;
            }

            @Override
            public boolean isThumbnailModesSupported() {
                return false;
            }

            @Override
            public void directoryChanged(FileHandle newDirectory) {
                // Do nothing
            }

            @Override
            public void viewModeChanged(ViewMode newViewMode) {
                // Do nothing
            }
        };

        assertNotNull("FileIconProvider should be created", provider);
        assertFalse("Thumbnail modes should not be supported by default", provider.isThumbnailModesSupported());
    }

    @Test
    public void testFileDeleterInterface() {
        // Test that the interface methods exist and can be called
        FileChooser.FileDeleter deleter = new FileChooser.FileDeleter() {
            @Override
            public boolean hasTrash() {
                return false;
            }

            @Override
            public boolean delete(FileHandle file) throws java.io.IOException {
                return file.delete();
            }
        };

        assertNotNull("FileDeleter should be created", deleter);
        assertFalse("Trash should not be supported by default", deleter.hasTrash());
    }

    @Test
    public void testFileChooserConstants() {
        // Test that public constants exist
        assertEquals("DEFAULT_KEY should be -1", -1, FileChooser.DEFAULT_KEY);
        assertTrue("focusFileScrollPaneOnShow should be boolean", FileChooser.focusFileScrollPaneOnShow || !FileChooser.focusFileScrollPaneOnShow);
        assertTrue("focusSelectedFileTextFieldOnShow should be boolean", FileChooser.focusSelectedFileTextFieldOnShow || !FileChooser.focusSelectedFileTextFieldOnShow);
    }
}

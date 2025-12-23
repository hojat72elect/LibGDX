package com.badlogic.gdx.ai;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.File;

public class StandaloneFileSystem implements FileSystem {

    public StandaloneFileSystem() {
    }

    @Override
    public FileHandleResolver newResolver(final FileType fileType) {
        return new FileHandleResolver() {

            @Override
            public FileHandle resolve(String fileName) {
                return new DesktopFileHandle(fileName, fileType);
            }
        };
    }

    @Override
    public FileHandle newFileHandle(String fileName) {
        return new DesktopFileHandle(fileName, FileType.Absolute);
    }

    @Override
    public FileHandle newFileHandle(File file) {
        return new DesktopFileHandle(file, FileType.Absolute);
    }

    @Override
    public FileHandle newFileHandle(String fileName, FileType type) {
        return new DesktopFileHandle(fileName, type);
    }

    @Override
    public FileHandle newFileHandle(File file, FileType type) {
        return new DesktopFileHandle(file, type);
    }

    public static class DesktopFileHandle extends FileHandle {

        static public final String externalPath = System.getProperty("user.home") + File.separator;
        static public final String localPath = new File("").getAbsolutePath() + File.separator;

        public DesktopFileHandle(String fileName, FileType type) {
            super(fileName, type);
        }

        public DesktopFileHandle(File file, FileType type) {
            super(file, type);
        }

        public FileHandle child(String name) {
            if (file.getPath().length() == 0) return new DesktopFileHandle(new File(name), type);
            return new DesktopFileHandle(new File(file, name), type);
        }

        public FileHandle sibling(String name) {
            if (file.getPath().length() == 0) throw new GdxRuntimeException("Cannot get the sibling of the root.");
            return new DesktopFileHandle(new File(file.getParent(), name), type);
        }

        public FileHandle parent() {
            File parent = file.getParentFile();
            if (parent == null) {
                if (type == FileType.Absolute)
                    parent = new File("/");
                else
                    parent = new File("");
            }
            return new DesktopFileHandle(parent, type);
        }

        public File file() {
            if (type == FileType.External) return new File(DesktopFileHandle.externalPath, file.getPath());
            if (type == FileType.Local) return new File(DesktopFileHandle.localPath, file.getPath());
            return file;
        }
    }
}

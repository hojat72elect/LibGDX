package com.mbrlabs.mundus.commons.utils;

import com.badlogic.gdx.files.FileHandle;

public class FileFormatUtils {

    public static final String FORMAT_3D_G3DB = "g3db";
    public static final String FORMAT_3D_GLTF = "gltf";
    public static final String FORMAT_3D_GLB = "glb";

    public static boolean isG3DB(String filename) {
        return filename.toLowerCase().endsWith(FORMAT_3D_G3DB);
    }

    public static boolean isG3DB(FileHandle file) {
        return isG3DB(file.name());
    }

    public static boolean isGLTF(String filename) {
        return filename.toLowerCase().endsWith(FORMAT_3D_GLTF);
    }

    public static boolean isGLB(String filename) {
        return filename.toLowerCase().endsWith(FORMAT_3D_GLB);
    }

    public static boolean isGLTF(FileHandle file) {
        return isGLTF(file.name());
    }

    public static boolean isGLB(final FileHandle file) {
        return isGLB(file.name());
    }
}

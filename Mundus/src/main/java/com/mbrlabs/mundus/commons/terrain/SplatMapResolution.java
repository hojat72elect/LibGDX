package com.mbrlabs.mundus.commons.terrain;

public enum SplatMapResolution {
    _512("512x512"),
    _1024("1024x1024"),
    _2048("2048x2048");

    public static final SplatMapResolution DEFAULT_RESOLUTION = _512;
    private final String value;

    SplatMapResolution(String value) {
        this.value = value;
    }

    public static SplatMapResolution valueFromString(String string) {
        for (SplatMapResolution res : values()) {
            if (res.value.equals(string)) {
                return res;
            }
        }

        return DEFAULT_RESOLUTION;
    }

    public String getValue() {
        return value;
    }

    public int getResolutionValues() {
        if (this == _512) {
            return 512;
        }
        if (this == _1024) {
            return 1024;
        }
        if (this == _2048) {
            return 2048;
        }

        return 512;
    }
}

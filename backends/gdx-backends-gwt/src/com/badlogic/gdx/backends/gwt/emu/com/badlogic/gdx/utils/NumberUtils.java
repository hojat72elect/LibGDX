
package com.badlogic.gdx.utils;

import com.google.gwt.corp.compatibility.Numbers;

public final class NumberUtils {

	public static int floatToIntBits (float value) {
		return Numbers.floatToIntBits(value);
	}

	public static int floatToRawIntBits (float value) {
		return Numbers.floatToIntBits(value);
	}

	public static int floatToIntColor (float value) {
		// matches non-emulated behavior except for floats that are infinite or NaN
		int intBits = Numbers.floatToIntBits(value);
		intBits |= (int)((intBits >>> 24) * (255f / 254f)) << 24;
		return intBits;
	}

	public static float intToFloatColor (int value) {
		// This mask avoids using bits in the NaN range. See Float.intBitsToFloat javadocs.
		// This unfortunately means we don't get the full range of alpha.
		return Numbers.intBitsToFloat(value & 0xfeffffff);
	}

	public static float intBitsToFloat (int value) {
		return Numbers.intBitsToFloat(value);
	}

	public static long doubleToLongBits (double value) {
		return Numbers.doubleToLongBits(value);
	}

	public static double longBitsToDouble (long value) {
		return Numbers.longBitsToDouble(value);
	}
}

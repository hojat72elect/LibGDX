
package com.badlogic.gdx.backends.gwt;

import com.google.gwt.core.client.JsArrayString;

public class GwtUtils {
	/** converts a JsArrayString to String[]
	 *
	 * @param jsArrayString JavaScript String Array
	 * @return Java String Array */
	public static String[] toStringArray (JsArrayString jsArrayString) {
		String[] strings = new String[jsArrayString.length()];
		for (int i = 0; i < jsArrayString.length(); i++) {
			strings[i] = jsArrayString.get(i);
		}
		return strings;
	}
}


package com.badlogic.gdx.backends.gwt;

/** Implementation of the <a href="https://www.w3.org/TR/gyroscope/#gyroscope-interface">Gyroscope Interface</a>
 * <a href="https://developer.mozilla.org/en-US/docs/Web/API/Gyroscope#Browser_compatibility">Compatibility</a> */
public class GwtGyroscope extends GwtSensor {

	/** Permission String to query the permission
	 *
	 * @see com.badlogic.gdx.backends.gwt.GwtPermissions */
	public static final String PERMISSION = "gyroscope";

	protected GwtGyroscope () {
	}

	static native GwtGyroscope getInstance () /*-{
		return new $wnd.Gyroscope();
	}-*/;

	static native boolean isSupported () /*-{
		return "Gyroscope" in $wnd;
	}-*/;

	final native double x () /*-{
		return this.x ? this.x : 0;
	}-*/;

	final native double y () /*-{
		return this.y ? this.y : 0;
	}-*/;

	final native double z () /*-{
		return this.z ? this.z : 0;
	}-*/;
}

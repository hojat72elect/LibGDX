
package com.badlogic.gdx.backends.gwt;

/** Implementation of the <a href="https://www.w3.org/TR/accelerometer/#accelerometer-interface">Accelerometer Interface</a>
 * <a href="https://developer.mozilla.org/en-US/docs/Web/API/Accelerometer#Browser_compatibility">Compatibility</a> */
public class GwtAccelerometer extends GwtSensor {

	/** Permission String to query the permission
	 *
	 * @see com.badlogic.gdx.backends.gwt.GwtPermissions */
	public static final String PERMISSION = "accelerometer";

	protected GwtAccelerometer () {
	}

	static native GwtAccelerometer getInstance () /*-{
		return new $wnd.Accelerometer();
	}-*/;

	static native boolean isSupported () /*-{
		return "Accelerometer" in $wnd;
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

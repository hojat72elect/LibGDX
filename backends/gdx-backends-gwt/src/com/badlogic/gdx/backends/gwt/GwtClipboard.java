
package com.badlogic.gdx.backends.gwt;

import com.badlogic.gdx.utils.Clipboard;

/** Basic implementation of clipboard in GWT. Paste only works inside the libGDX application. */
public class GwtClipboard implements Clipboard {

	private boolean requestedWritePermissions = false;
	private boolean hasWritePermissions = true;

	private final ClipboardWriteHandler writeHandler = new ClipboardWriteHandler();

	private String content = "";

	@Override
	public boolean hasContents () {
		String contents = getContents();
		return contents != null && !contents.isEmpty();
	}

	@Override
	public String getContents () {
		return content;
	}

	@Override
	public void setContents (String content) {
		this.content = content;
		if (requestedWritePermissions || GwtApplication.agentInfo().isFirefox()) {
			if (hasWritePermissions) setContentJSNI(content);
		} else {
			GwtPermissions.queryPermission("clipboard-write", writeHandler);
			requestedWritePermissions = true;
		}
	}

	private native void setContentJSNI (String content) /*-{
		if ("clipboard" in $wnd.navigator) {
			$wnd.navigator.clipboard.writeText(content);
		}
	}-*/;

	private class ClipboardWriteHandler implements GwtPermissions.GwtPermissionResult {
		@Override
		public void granted () {
			hasWritePermissions = true;
			setContentJSNI(content);
		}

		@Override
		public void denied () {
			hasWritePermissions = false;
		}

		@Override
		public void prompt () {
			hasWritePermissions = true;
			setContentJSNI(content);
		}
	}
}

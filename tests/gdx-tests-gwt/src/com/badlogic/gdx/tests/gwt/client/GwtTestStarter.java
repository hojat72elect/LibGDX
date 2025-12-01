
package com.badlogic.gdx.tests.gwt.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.google.gwt.user.client.Window;

public class GwtTestStarter extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration config = new GwtApplicationConfiguration(true);
		config.useGyroscope = true;
		config.padVertical = 150;

		// Look for URL parameter '?useGL30=true' to enable WebGL2
		config.useGL30 = Boolean.parseBoolean(Window.Location.getParameter("useGL30"));

		if (config.useGL30) {
			ShaderProgram.prependVertexCode = "#version 300 es\n#define varying out\n#define attribute in\n";
			ShaderProgram.prependFragmentCode = "#version 300 es\n#define varying in\n#define texture2D texture\n#define gl_FragColor fragColor\nprecision mediump float;\nout vec4 fragColor;\n";
		}

		// config.openURLInNewWindow = true;
		return config;
	}

	@Override
	public ApplicationListener createApplicationListener () {
		return new GwtTestWrapper();
	}
}

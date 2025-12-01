
package com.badlogic.gdx.tests.gwt;

import java.io.DataInputStream;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.ScreenUtils;

public class GwtBinaryTest extends GdxTest {
	byte[] bytes;

	@Override
	public void create () {
		FileHandle handle = Gdx.files.internal("data/lsans.ttf");
		bytes = new byte[(int)handle.length()];
		DataInputStream in = new DataInputStream(handle.read());
		for (int i = 0; i < 100; i++) {
			try {
				bytes[i] = in.readByte();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
	}
}


package com.badlogic.gdx.backends.iosrobovm;

import com.badlogic.gdx.Audio;

public interface IOSAudio extends Audio {
	void didBecomeActive();

	void willEnterForeground();

	void willResignActive();

}

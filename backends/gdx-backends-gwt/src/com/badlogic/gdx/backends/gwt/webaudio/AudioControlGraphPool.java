
package com.badlogic.gdx.backends.gwt.webaudio;

import com.badlogic.gdx.utils.Pool;
import com.google.gwt.core.client.JavaScriptObject;

public class AudioControlGraphPool extends Pool<AudioControlGraph> {
	public JavaScriptObject audioContext;
	public JavaScriptObject destinationNode;

	public AudioControlGraphPool (JavaScriptObject audioContext, JavaScriptObject destinationNode) {
		this.audioContext = audioContext;
		this.destinationNode = destinationNode;
	}

	@Override
	protected AudioControlGraph newObject () {
		return new AudioControlGraph(audioContext, destinationNode);
	}
}

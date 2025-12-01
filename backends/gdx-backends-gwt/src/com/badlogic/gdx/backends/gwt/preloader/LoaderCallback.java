
package com.badlogic.gdx.backends.gwt.preloader;

public interface LoaderCallback<T> {
	void success(T result);

	void error();
}

package com.badlogic.gdx.backends.android;

import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import java.io.IOException;

public class AndroidMusic implements Music, MediaPlayer.OnCompletionListener {
    private final AndroidAudio audio;
    protected boolean wasPlaying = false;
    protected OnCompletionListener onCompletionListener;
    private MediaPlayer player;
    private boolean isPrepared = true;
    private float volume = 1f;

    AndroidMusic(AndroidAudio audio, MediaPlayer player) {
        this.audio = audio;
        this.player = player;
        this.onCompletionListener = null;
        this.player.setOnCompletionListener(this);
    }

    @Override
    public void dispose() {
        if (player == null) return;
        try {
            player.release();
        } catch (Throwable t) {
            Gdx.app.log("AndroidMusic", "Error while disposing AndroidMusic instance, non-fatal");
        } finally {
            player = null;
            onCompletionListener = null;
            audio.notifyMusicDisposed(this);
        }
    }

    @Override
    public boolean isLooping() {
        if (player == null) return false;
        try {
            return player.isLooping();
        } catch (IllegalStateException e) {
            Gdx.app.error("AndroidMusic", "Error while checking isLooping", e);
            return false;
        }
    }

    @Override
    public void setLooping(boolean isLooping) {
        if (player == null) return;
        player.setLooping(isLooping);
    }

    @Override
    public boolean isPlaying() {
        if (player == null) return false;
        try {
            return player.isPlaying();
        } catch (IllegalStateException e) {
            Gdx.app.error("AndroidMusic", "Error while checking isPlaying", e);
            return false;
        }
    }

    @Override
    public void pause() {
        if (player == null) return;
        try {
            if (player.isPlaying()) {
                player.pause();
            }
        } catch (IllegalStateException e) {
            Gdx.app.error("AndroidMusic", "Error trying to pause music", e);
        }
        wasPlaying = false;
    }

    @Override
    public void play() {
        if (player == null) return;
        try {
            if (!isPrepared) {
                player.prepare();
                isPrepared = true;
            }
            player.start();
        } catch (IllegalStateException | IOException e) {
            Gdx.app.error("AndroidMusic", "Error trying to play music", e);
        }
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public void setVolume(float volume) {
        if (player == null) return;
        player.setVolume(volume, volume);
        this.volume = volume;
    }

    @Override
    public void setPan(float pan, float volume) {
        if (player == null) return;
        float leftVolume = volume;
        float rightVolume = volume;

        if (pan < 0) {
            rightVolume *= (1 - Math.abs(pan));
        } else if (pan > 0) {
            leftVolume *= (1 - Math.abs(pan));
        }

        player.setVolume(leftVolume, rightVolume);
        this.volume = volume;
    }

    @Override
    public void stop() {
        if (player == null) return;
        player.stop();
        isPrepared = false;
    }

    @Override
    public float getPosition() {
        if (player == null) return 0.0f;
        return player.getCurrentPosition() / 1000f;
    }

    public void setPosition(float position) {
        if (player == null) return;
        try {
            if (!isPrepared) {
                player.prepare();
                isPrepared = true;
            }
            player.seekTo((int) (position * 1000));
        } catch (IllegalStateException | IOException e) {
            Gdx.app.error("AndroidMusic", "Error setting music position", e);
        }
    }

    public float getDuration() {
        if (player == null) return 0.0f;
        return player.getDuration() / 1000f;
    }

    @Override
    public void setOnCompletionListener(@NonNull OnCompletionListener listener) {
        onCompletionListener = listener;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (onCompletionListener != null) {
            Gdx.app.postRunnable(() -> {
                if (onCompletionListener != null) {
                    onCompletionListener.onCompletion(AndroidMusic.this);
                }
            });
        }
    }
}

package com.badlogic.gdx.ai.tests.msg.tests;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.tests.MessageTests;
import com.badlogic.gdx.ai.tests.msg.MessageTestBase;
import com.badlogic.gdx.ai.tests.utils.scene2d.FloatValueLabel;
import com.badlogic.gdx.ai.tests.utils.scene2d.IntValueLabel;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * A simple test to demonstrate timer messages.
 *
 *  */
public class MessageTimerTest extends MessageTestBase implements Telegraph {

    int msgCounter;
    float msgTimestamp;
    TextButton timerEnabledButton;

    public MessageTimerTest(MessageTests container) {
        super(container, "Timer");
    }

    @Override
    public String getDescription() {
        return "Creates a simple timer that increments a counter each second";
    }

    @Override
    public void create() {
        super.create();

        MessageManager.getInstance().clear();

        msgCounter = -1;

        testTable.add(new IntValueLabel("Message Counter: ", 0, container.skin) {
            @Override
            public int getValue() {
                return msgCounter;
            }
        }).left();

        testTable.row();
        testTable.add(new FloatValueLabel("Message Timestamp: ", 0, container.skin) {
            @Override
            public float getValue() {
                return msgTimestamp;
            }
        }).left();

        testTable.row();
        testTable.add(new FloatValueLabel("AI Time: ", 0, container.skin) {
            @Override
            public float getValue() {
                return GdxAI.getTimepiece().getTime();
            }
        }).left();

        testTable.row();
        timerEnabledButton = new TextButton("Stop timer", container.skin);
        timerEnabledButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (timerEnabledButton.isChecked()) {
                    timerEnabledButton.setText("Stop Timer");
                    sendMessage(0f, msgCounter + 1);
                } else {
                    timerEnabledButton.setText("Start Timer");
                }
            }
        });
        testTable.add(timerEnabledButton).left();

        // Triggers the listener above, so sending the 1st message with no delay and counter 0
        timerEnabledButton.setChecked(true);
    }

    @Override
    public void update() {
        // Dispatch any delayed messages
        MessageManager.getInstance().update();
    }

    @Override
    public void draw() {
    }

    @Override
    public void dispose() {
        MessageManager.getInstance().clear();
        super.dispose();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        this.msgCounter = (Integer) msg.extraInfo;
        this.msgTimestamp = msg.getTimestamp();
        if (timerEnabledButton.isChecked()) {
            float lag = GdxAI.getTimepiece().getTime() - msg.getTimestamp();
            lag -= (int) lag; // take the decimal part only (in case the lag is > 1)
            float delay = 1f - lag;
            sendMessage(delay, msgCounter + 1);
        }
        return true;
    }

    private void sendMessage(float delay, int counter) {
        MessageManager.getInstance().dispatchMessage(delay, this, this, 1, counter);
    }
}

package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;

class Coin extends GameObject {

    private static final String[] ANIMATION_FRAMES = {
            "textures/loot/coin/1.png",
            "textures/loot/coin/2.png",
            "textures/loot/coin/3.png",
            "textures/loot/coin/4.png"
    };

    private static final float ANIMATION_DELAY = 1f;

    private static Bitmap[] loadedFrames = null;

    private float delayCounter;
    private int frameCounter;

    Coin(Context context) {
        super(context);

        if(loadedFrames == null) {
            loadedFrames = new Bitmap[ANIMATION_FRAMES.length];
            loadAllFrames();
        }

        delayCounter = 0;
        frameCounter = 0;
    }

    private static void loadAllFrames() {
        int i = 0;
        for (String path: ANIMATION_FRAMES) {
            Bitmap frame = SHARED_CACHE.get(ANIMATION_FRAMES[i]);
            frame = Bitmap.createScaledBitmap(frame, 120, 120, false);
            loadedFrames[i] = frame;
            i++;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        delayCounter += deltaTime/100;

        if (delayCounter > ANIMATION_DELAY) {
            if (frameCounter == ANIMATION_FRAMES.length) {
                frameCounter = 0;
            }
            setSprite(loadedFrames[frameCounter]);
            delayCounter = 0;
            frameCounter++;
        }
    }
}

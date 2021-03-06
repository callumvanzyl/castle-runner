package com.callumvanzyl.castlerunner;

import android.content.res.AssetManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

class AnimatedObject extends GameObject {

    private static HashMap<String, Object> registeredAnimations = new HashMap<>();
    protected static ScaledBitmapCache sharedScaledCache = null;

    private String currentAnimationName;
    private ArrayList<String> currentAnimationPaths;
    private float currentAnimationDelay;
    private float delayCounter;
    private int frameCounter;

    private boolean loop;

    AnimatedObject(GameContext gameContext) {
        super(gameContext);

        if (sharedScaledCache == null) {
            AssetManager assetManager = gameContext.getContext().getAssets();
            sharedScaledCache = new ScaledBitmapCache(assetManager);
        }

        currentAnimationPaths = null;
        currentAnimationDelay = 0;
        delayCounter = 0;
        frameCounter = 0;

        loop = false;
    }

    public static void registerAnimation(String id, float delay, ArrayList<String> paths) {
        Object[] data = new Object[2];
        data[0] = delay;
        data[1] = paths;
        registeredAnimations.put(id, data);
    }

    public void setAnimation(String id, boolean loop) {
        if (registeredAnimations.containsKey(id)) {
            Object[] raw = (Object[]) registeredAnimations.get(id);
            currentAnimationPaths = (ArrayList<String>) raw[1];
            currentAnimationDelay = (float) raw[0];
            currentAnimationName = id;
            frameCounter = 0;

            this.loop = loop;
        }
    }

    public static void emptyCache() {
        sharedCache.empty();
    }

    public boolean isAnimationDone() {
        return frameCounter == currentAnimationPaths.size();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (currentAnimationPaths != null) {
            delayCounter += deltaTime / 100;

            if (delayCounter > currentAnimationDelay) {
                if (frameCounter == currentAnimationPaths.size()) {
                    if (loop) {
                        frameCounter = 0;
                    } else {
                        frameCounter = currentAnimationPaths.size()-1;
                    }
                }
                setSprite(sharedScaledCache.get(currentAnimationPaths.get(frameCounter), getSize()));
                delayCounter = 0;
                frameCounter++;
            }
        }
    }

    public String getCurrentAnimationName() {
        return currentAnimationName;
    }

}

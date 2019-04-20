package com.callumvanzyl.castlerunner;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

class Skeleton extends AnimatedObject {

    private final static int JUMP_VELOCITY = 30;

    private float timeGrounded = 0;

    private static boolean isInit = false;

    Skeleton(GameContext gameContext) {
        super(gameContext);

        if (!isInit) {
            AnimatedObject.registerAnimation(
                    "skeleton-idle",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/monsters/skeleton/idle/1.png",
                            "textures/monsters/skeleton/idle/2.png",
                            "textures/monsters/skeleton/idle/3.png"
                    ))
            );

            isInit = true;
        }

        setAnimation("skeleton-idle", true);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isGrounded()) {
            if (timeGrounded > 0.2) {
                setVelocity(JUMP_VELOCITY);
                timeGrounded = 0;
            }
            timeGrounded += (deltaTime/100);
        }
    }

}

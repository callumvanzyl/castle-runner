package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

class Player extends AnimatedObject {

    private static boolean isInit = false;

    private ArrayList<Chunk> activeChunks = null;

    private float jumpVelocity;

    Player(Context context) {
        super(context);

        if (!isInit) {
            AnimatedObject.registerAnimation(
                    "player-attack",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/player/attack/1.png",
                            "textures/player/attack/2.png",
                            "textures/player/attack/3.png"
                    ))
            );

            AnimatedObject.registerAnimation(
                    "player-die",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/player/die/1.png",
                            "textures/player/die/2.png",
                            "textures/player/die/3.png",
                            "textures/player/die/4.png",
                            "textures/player/die/5.png"
                    ))
            );

            AnimatedObject.registerAnimation(
                    "player-idle",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/player/idle/1.png"
                    ))
            );

            AnimatedObject.registerAnimation(
                    "player-jump",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/player/jump/1.png",
                            "textures/player/jump/2.png",
                            "textures/player/jump/3.png",
                            "textures/player/jump/4.png"
                    ))
            );

            AnimatedObject.registerAnimation(
                    "player-run",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/player/run/1.png",
                            "textures/player/run/2.png",
                            "textures/player/run/3.png",
                            "textures/player/run/4.png",
                            "textures/player/run/5.png",
                            "textures/player/run/6.png",
                            "textures/player/run/7.png"
                    ))
            );

            isInit = true;
        }

        jumpVelocity = 0f;

        setAnimation("player-run", true);
    }

    public boolean isGrounded() {
        Rect tester = new Rect();
        tester.set(getCollider());
        tester.offset(0, 10);

        if (activeChunks != null) {
            for (Chunk chunk: activeChunks) {
                for (GameObject object : chunk.getObjects()) {
                    if (object.isColliding(tester)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (!isGrounded()) {
            jumpVelocity -= 1f;
            int incrementY = (int) ((int) -jumpVelocity*(deltaTime/100));
            setPosition(getPosition().add(0, incrementY));
        } else {
            jumpVelocity = 0;
        }
    }

    public void setActiveChunks(ArrayList<Chunk> activeChunks) {
        this.activeChunks = activeChunks;
    }

}

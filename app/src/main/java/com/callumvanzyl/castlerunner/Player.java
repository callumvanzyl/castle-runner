package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.math.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;

class Player extends AnimatedObject {

    private final static int MAX_VELOCITY = 100;
    private final static int JUMP_VELOCITY = 90;

    private static boolean isInit = false;

    private ArrayList<Chunk> activeChunks = null;

    private boolean isCeiling;
    private boolean isGrounded;
    private float velocity;

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

        velocity = 0f;

        setAnimation("player-run", true);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        isCeiling = false;
        isGrounded = false;

        Rect ceilingTester = new Rect();
        ceilingTester.set(getCollider());
        ceilingTester.offset(0, -2);
        ceilingTester.bottom = ceilingTester.top + 20;

        Rect groundTester = new Rect();
        groundTester.set(getCollider());
        groundTester.offset(0, 2);
        groundTester.top = groundTester.bottom - 20;

        if (activeChunks != null) {
            for (Chunk chunk: activeChunks) {
                for (int i = 0; i < chunk.getObjects().size(); i++) {
                    GameObject object = chunk.getObjects().get(i);

                    if (object.hasTag("Loot")) {
                        if (object.isColliding(getCollider())) {
                            chunk.removeObject(i);
                            continue;
                        }
                    }

                    if (object.hasTag(("Ground"))) {
                        if (object.isColliding(ceilingTester)) {
                            isCeiling = true;
                        }
                        if (object.isColliding(groundTester)) {
                            isGrounded = true;
                        }
                    }

                }
            }
        }

        if (isCeiling) {
            velocity = -(velocity/2);
        }

        if (!isGrounded) {
            velocity -= 2f;
        } else {
            velocity = JUMP_VELOCITY;
        }

        velocity = MathUtils.clamp(velocity, -MAX_VELOCITY, MAX_VELOCITY);
        int incrementY = (int) ((int) -velocity*(deltaTime/100));
        setPosition(getPosition().add(0, incrementY));
    }

    public void setActiveChunks(ArrayList<Chunk> activeChunks) {
        this.activeChunks = activeChunks;
    }

}

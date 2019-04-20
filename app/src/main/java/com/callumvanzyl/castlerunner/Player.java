package com.callumvanzyl.castlerunner;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Arrays;

class Player extends AnimatedObject {

    private final static int MAX_VELOCITY = 200;
    private final static int JUMP_VELOCITY = 110;

    private static boolean isInit = false;

    private float timeGrounded;

    private boolean canJump;

    Player(GameContext gameContext) {
        super(gameContext);

        if (!isInit) {
            AnimatedObject.registerAnimation(
                    "player-attack",
                    0.1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/player/attack/v2/1.png",
                            "textures/player/attack/v2/2.png",
                            "textures/player/attack/v2/3.png",
                            "textures/player/attack/v2/4.png",
                            "textures/player/attack/v2/5.png",
                            "textures/player/attack/v2/6.png",
                            "textures/player/attack/v2/7.png",
                            "textures/player/attack/v2/8.png",
                            "textures/player/attack/v2/9.png",
                            "textures/player/attack/v2/10.png",
                            "textures/player/attack/v2/11.png",
                            "textures/player/attack/v2/12.png",
                            "textures/player/attack/v2/13.png",
                            "textures/player/attack/v2/14.png"
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
                    "player-fall",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/player/fall/v2/1.png",
                            "textures/player/fall/v2/2.png"
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
                            "textures/player/jump/v2/1.png",
                            "textures/player/jump/v2/2.png"
                    ))
            );

            AnimatedObject.registerAnimation(
                    "player-run",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/player/run/v2/1.png",
                            "textures/player/run/v2/2.png",
                            "textures/player/run/v2/3.png",
                            "textures/player/run/v2/4.png",
                            "textures/player/run/v2/5.png",
                            "textures/player/run/v2/6.png",
                            "textures/player/run/v2/7.png"
                    ))
            );

            isInit = true;
        }

        canJump = true;

        setAnimation("player-idle", true);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (getActiveChunks() != null) {
            for (Chunk chunk: getActiveChunks()) {
                for (int i = 0; i < chunk.getObjects().size(); i++) {
                    GameObject object = chunk.getObjects().get(i);
                    if (object.hasTag(("Loot"))) {
                        if (object.isColliding(getCollider())) {
                            chunk.getObjects().remove(i);
                        }
                    }
                }
            }
        }

        Button jumpButton = gameContext.getJumpButton();

        canJump = (timeGrounded > 0.2 && isGrounded());
        jumpButton.setActive(!canJump);

        if (isGrounded()) {
            timeGrounded += (deltaTime / 100);
            if (!getCurrentAnimationName().equals("player-run")) {
                setAnimation("player-run", true);
            }
            if (canJump && jumpButton.isTouched()) {
                setVelocity(JUMP_VELOCITY);
            }
        } else {
            timeGrounded = 0;
            if (getVelocity() > 0) {
                if (!getCurrentAnimationName().equals("player-jump")) {
                    setAnimation("player-jump", true);
                }
            } else if (getVelocity() < 0) {
                if (!getCurrentAnimationName().equals("player-fall")) {
                    setAnimation("player-fall", true);
                }
            }
            if (jumpButton.isTouched()) {
                setFallingSpeed(2f);
            } else {
                setFallingSpeed(3f);
            }
        }
    }

}

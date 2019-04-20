package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.math.MathUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

class Player extends AnimatedObject {

    private final static int MAX_VELOCITY = 200;
    private final static int JUMP_VELOCITY = 110;

    private static boolean isInit = false;

    private ArrayList<Chunk> activeChunks = null;

    private boolean isCeiling;
    private boolean isGrounded;
    private float velocity;
    private float timeGrounded;

    private boolean canJump;

    private Rect ceilingTester;
    private Rect groundTester;

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
                    "player-ouch",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/player/ouch/1.png"
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

        velocity = 0f;

        canJump = true;

        setAnimation("player-idle", true);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (DRAW_COLLIDERS) {
            if (ceilingTester != null && groundTester != null) {
                Paint paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setStyle(Paint.Style.FILL);

                canvas.drawRect(ceilingTester, paint);
                canvas.drawRect(groundTester, paint);
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Button jumpButton = gameContext.getJumpButton();

        isCeiling = false;
        isGrounded = false;

        ceilingTester = new Rect();
        ceilingTester.set(getCollider());
        ceilingTester.bottom = ceilingTester.top + 10;

        groundTester = new Rect();
        groundTester.set(getCollider());
        groundTester.top = groundTester.bottom - 10;

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
                            int correction = ceilingTester.top - object.getCollider().bottom;
                            if (correction < 0) {
                                setPosition(getPosition().add(0, -correction));
                            }
                        }
                        if (object.isColliding(groundTester)) {
                            isGrounded = true;
                            int correction = groundTester.bottom - object.getCollider().top;
                        }
                    }

                }
            }
        }

        if (isCeiling) {
            velocity = -(velocity/2);

            if (!getCurrentAnimationName().equals("player-ouch")) {
                setAnimation("player-ouch", true);
            }
        }

        canJump = (timeGrounded > 0.5 && isGrounded);
        jumpButton.setActive(!canJump);

        if (isGrounded) {
            if (!getCurrentAnimationName().equals("player-run")) {
                setAnimation("player-run", true);
            }
            timeGrounded += (deltaTime / 100);
            if (canJump && jumpButton.isTouched()) {
                velocity = JUMP_VELOCITY;
            } else {
                velocity = 0;
            }
        } else {
            if (velocity > 0) {
                if (!getCurrentAnimationName().equals("player-jump")) {
                    setAnimation("player-jump", true);
                }
            } else if (velocity < 0) {
                if (!getCurrentAnimationName().equals("player-fall")) {
                    setAnimation("player-fall", true);
                }
            }
            timeGrounded = 0;
            if (jumpButton.isTouched()) {
                velocity -= 2f;
            } else {
                velocity -= 3f;
            }
        }

        velocity = MathUtils.clamp(velocity, -MAX_VELOCITY, MAX_VELOCITY);
        int incrementY = (int) ((int) -velocity*(deltaTime/100));
        setPosition(getPosition().add(0, incrementY));
    }

    public void setActiveChunks(ArrayList<Chunk> activeChunks) {
        this.activeChunks = activeChunks;
    }

}

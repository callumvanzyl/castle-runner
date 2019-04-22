package com.callumvanzyl.castlerunner;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

class Player extends AnimatedObject {

    private final static int MAX_VELOCITY = 200;
    private final static int JUMP_VELOCITY = 110;

    private static boolean isInit = false;

    private int score;
    private boolean isDead;
    private int worldHeight;

    private float timeGrounded;

    private boolean canJump;
    private boolean isAttacking;

    Player(GameContext gameContext) {
        super(gameContext);

        if (!isInit) {
            AnimatedObject.registerAnimation(
                    "player-attack",
                    0.2f,
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
                            "textures/player/die/v2/1.png",
                            "textures/player/die/v2/2.png",
                            "textures/player/die/v2/3.png",
                            "textures/player/die/v2/4.png",
                            "textures/player/die/v2/5.png",
                            "textures/player/die/v2/6.png",
                            "textures/player/die/v2/7.png",
                            "textures/player/die/v2/8.png",
                            "textures/player/die/v2/9.png",
                            "textures/player/die/v2/10.png"
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

        score = 0;
        isDead = false;
        worldHeight = Integer.MAX_VALUE;

        canJump = true;

        setAnimation("player-fall", false);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        worldHeight = canvas.getHeight();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Rect attackHitbox = new Rect();
        attackHitbox.set(getCollider());
        attackHitbox.right = attackHitbox.left + 150;

        if (getActiveChunks() != null) {
            for (Chunk chunk: getActiveChunks()) {
                for (int i = 0; i < chunk.getObjects().size(); i++) {
                    GameObject object = chunk.getObjects().get(i);
                    if (object.hasTag(("Loot"))) {
                        if (object.isColliding(getCollider())) {
                            score += 1;
                            chunk.getObjects().remove(i);
                        }
                    }
                    if (object.hasTag("Monster")) {
                        if (isAttacking) {
                            if (object.isColliding(attackHitbox)) {
                                score += 5;
                                chunk.getObjects().remove(i);
                            }
                        }
                        if (object.isColliding(getCollider())) {
                            isDead = true;
                        }
                    }
                }
            }
        }

        if (getPosition().y > worldHeight) {
            isDead = true;
        }

        Button attackButton = gameContext.getAttackButton();

        if (!isDead && attackButton.isTouched()) {
            isAttacking = true;
            if (!getCurrentAnimationName().equals("player-attack")) {
                setAnimation("player-attack", false);
            }
        }

        if (isAttacking) {
            if (isAnimationDone()) {
                isAttacking = false;
            }
        }

        if (isDead) {
            if (!getCurrentAnimationName().equals("player-die")) {
                setAnimation("player-die", false);
            }
        }

        Button jumpButton = gameContext.getJumpButton();

        canJump = (timeGrounded > 0.2 && isGrounded());
        jumpButton.setActive(!canJump);

        if (isGrounded()) {
            timeGrounded += (deltaTime / 100);
            if (!isAttacking && !isDead) {
                if (!getCurrentAnimationName().equals("player-run")) {
                    setAnimation("player-run", true);
                }
            }
            if (canJump && jumpButton.isTouched() && !isDead) {
                setVelocity(JUMP_VELOCITY);
            }
        } else {
            timeGrounded = 0;
            if (getVelocity() > 0) {
                if (!isAttacking && !isDead) {
                    if (!getCurrentAnimationName().equals("player-jump")) {
                        setAnimation("player-jump", true);
                    }
                }
            } else if (getVelocity() < 0) {
                if (!isAttacking && !isDead) {
                    if (!getCurrentAnimationName().equals("player-fall")) {
                        setAnimation("player-fall", true);
                    }
                }
            }
            if (jumpButton.isTouched()) {
                setFallingSpeed(2f);
            } else {
                setFallingSpeed(3f);
            }
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isDead() {
        return isDead;
    }
}

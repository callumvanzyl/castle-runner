package com.callumvanzyl.castlerunner;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.math.MathUtils;
import android.util.Log;

import java.util.ArrayList;

class GameObject implements Drawable, Updateable {

    protected static final boolean DRAW_COLLIDERS = false;

    private static ArrayList<Chunk> activeChunks = null;

    protected static BitmapCache sharedCache = null;
    private static Paint debugPaint = null;
    private static Paint pixelPaint = null;

    protected GameContext gameContext;

    private Vector2 position;
    private Vector2 size;

    private Vector2 colliderOffset;

    private Rect collider;
    private Bitmap sprite;
    private Rect surface;

    private ArrayList<String> tags;

    private boolean isCollidable;

    private boolean velocityEnabled;
    private float velocity;

    private boolean isCeiling;
    private boolean isGrounded;

    private float fallingSpeed;

    GameObject(GameContext gameContext) {
        if (sharedCache == null) {
            AssetManager assetManager = gameContext.getContext().getAssets();
            sharedCache = new BitmapCache(assetManager);
        }

        if (debugPaint == null) {
            debugPaint = new Paint();
            debugPaint.setStyle(Paint.Style.FILL);
            debugPaint.setColor(Color.RED);
            debugPaint.setAlpha(120);
        }

        if (pixelPaint == null) {
            pixelPaint = new Paint();
            pixelPaint.setAntiAlias(false);
            pixelPaint.setDither(false);
            pixelPaint.setFilterBitmap(false);
        }

        this.gameContext = gameContext;

        position = Vector2.ZERO;
        size = Vector2.ONE;

        colliderOffset = Vector2.ZERO;

        collider = new Rect();
        setSprite("textures/placeholder.jpg");
        surface = new Rect();

        tags = new ArrayList<>();

        isCollidable = false;

        velocityEnabled = false;
        velocity = 0;

        fallingSpeed = 1f;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    public boolean isColliding(Rect rect) {
        if (!isCollidable) return false;
        return Rect.intersects(collider, rect);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, position.x, position.y, pixelPaint);

        if (DRAW_COLLIDERS && isCollidable) {
            canvas.drawRect(collider, debugPaint);
        }
    }

    @Override
    public void update(float deltaTime) {
        if (isCollidable && velocityEnabled) {
            isCeiling = false;
            isGrounded = false;

            Rect ceilingTester = new Rect();
            ceilingTester.set(getCollider());
            ceilingTester.bottom = ceilingTester.top + 10;

            Rect groundTester = new Rect();
            groundTester.set(getCollider());
            groundTester.top = groundTester.bottom - 10;

            if (GameObject.activeChunks != null) {
                for (Chunk chunk: GameObject.activeChunks) {
                    for (int i = 0; i < chunk.getObjects().size(); i++) {
                        GameObject object = chunk.getObjects().get(i);
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
                                if (correction > 10) {
                                    setPosition(getPosition().add(0, -correction));
                                }
                            }
                        }
                    }
                }
            }

            velocity = MathUtils.clamp(velocity, -200, 200);
            int incrementY = (int) ((int) -velocity*(deltaTime/100));
            setPosition(getPosition().add(0, incrementY));

            if (isCeiling) {
                velocity = -(velocity/2);
            }

            if (isGrounded) {
                velocity = fallingSpeed;
            } else {
                velocity -= fallingSpeed;
            }
        }
    }

    public static ArrayList<Chunk> getActiveChunks() {
        return activeChunks;
    }

    public static void setActiveChunks(ArrayList<Chunk> activeChunks) {
        GameObject.activeChunks = activeChunks;
    }

    public Rect getCollider() {
        return collider;
    }

    public void setCollidable(boolean collidable) {
        isCollidable = collidable;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        collider.offsetTo(position.x + colliderOffset.x, position.y + colliderOffset.y);
        surface.offsetTo(position.x, position.y);

        this.position = position;
    }


    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        sprite = Bitmap.createScaledBitmap(sprite, size.x, size.y, false);

        surface.right = surface.left + size.x;
        surface.bottom = surface.top + size.y;

        this.size = size;
    }

    public void setColliderSizeAndOffset(Vector2 colliderSize, Vector2 colliderOffset) {
        collider.right = collider.left + colliderSize.x;
        collider.bottom = collider.top + colliderSize.y;

        this.colliderOffset = colliderOffset;
    }

    public void setSprite(String path) {
        this.sprite = sharedCache.get(path);
        this.sprite = Bitmap.createScaledBitmap(sprite, size.x, size.y, false);
    }

    public void setSprite(Bitmap sprite) {
        this.sprite = sprite;
    }

    public boolean isVelocityEnabled() {
        return velocityEnabled;
    }

    public void setVelocityEnabled(boolean velocityEnabled) {
        this.velocityEnabled = velocityEnabled;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public boolean isCeiling() {
        return isCeiling;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public void setFallingSpeed(float fallingSpeed) {
        this.fallingSpeed = fallingSpeed;
    }
}

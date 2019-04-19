package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

class GameObject implements Drawable, Updateable {

    private static final boolean DRAW_COLLIDERS = true;

    protected static BitmapCache sharedCache = null;
    private static Paint debugPaint = null;
    private static Paint pixelPaint = null;

    private Vector2 position;
    private Vector2 size;

    private Vector2 colliderOffset;

    private Rect collider;
    private Bitmap sprite;
    private Rect surface;

    private boolean isCollidable;

    GameObject(Context context) {
        if (sharedCache == null) {
            AssetManager assetManager = context.getAssets();
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

        position = Vector2.ZERO;
        size = Vector2.ONE;

        colliderOffset = Vector2.ZERO;

        collider = new Rect();
        setSprite("textures/placeholder.jpg");
        surface = new Rect();

        isCollidable = false;
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

}

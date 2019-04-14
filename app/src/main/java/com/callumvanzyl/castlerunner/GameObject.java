package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

class GameObject implements Drawable, Updateable {

    private static BitmapCache SHARED_CACHE = null;
    private static Paint PIXEL_PAINT = null;

    private Bitmap sprite;
    private Rect surface;

    private Vector2 position;
    private Vector2 size;

    GameObject(Context context) {
        if (SHARED_CACHE == null) {
            AssetManager assetManager = context.getAssets();
            SHARED_CACHE = new BitmapCache(assetManager);
        }

        if (PIXEL_PAINT == null) {
            PIXEL_PAINT = new Paint();
            PIXEL_PAINT.setAntiAlias(false);
            PIXEL_PAINT.setDither(false);
            PIXEL_PAINT.setFilterBitmap(false);
        }

        setSprite("textures/placeholder.jpg");
        surface = new Rect();

        position = Vector2.ZERO;
        size = Vector2.ZERO;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, position.x, position.y, PIXEL_PAINT);
    }

    @Override
    public void update(float deltaTime) {

    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
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

    public void setSprite(String path) {
        this.sprite = SHARED_CACHE.get(path);
        this.sprite = Bitmap.createScaledBitmap(sprite, size.x, size.y, false);
    }

}

package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

abstract class GameObject implements Drawable, Updateable {

    private static BitmapCache SHARED_CACHE = null;

    private Bitmap sprite;
    private Rect surface;

    GameObject(Context context) {
        if (SHARED_CACHE == null) {
            AssetManager assetManager = context.getAssets();
            SHARED_CACHE = new BitmapCache(assetManager);
        }

        surface = new Rect();

        setSprite("textures/placeholder.jpg");
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, null, surface, null);
    }

    @Override
    public void update(float deltaTime) {

    }

    void setPosition(int x, int y) {
        surface.offsetTo(x, y);
    }

    void setSize(int w, int h) {
        surface.right = surface.left + w;
        surface.bottom = surface.top + h;
    }

    void setSprite(String path) {
        this.sprite = SHARED_CACHE.get(path);
    }

}

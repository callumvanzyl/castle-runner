package com.callumvanzyl.castlerunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;

abstract class GameObject implements Drawable, Updateable {

    private Vector2 position;
    private Bitmap sprite;

    GameObject() {
        position = Vector2.ZERO;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void update(long time) {

    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setSprite(Bitmap sprite) {
        this.sprite = sprite;
    }

}

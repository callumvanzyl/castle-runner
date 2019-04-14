package com.callumvanzyl.castlerunner;

import android.graphics.Canvas;

import java.util.ArrayList;

class ObjectBatch {

    private ArrayList<GameObject> objects;

    ObjectBatch() {
        objects = new ArrayList<>();
    }

    public void addObject(GameObject object) {
        objects.add(object);
    }

    public void drawBatch(Canvas canvas) {
        for (GameObject object: objects) {
            if (object instanceof Drawable) {
                object.draw(canvas);
            }
        }
    }

    public void updateBatch(float deltaTime) {
        for (GameObject object: objects) {
            if (object instanceof Updateable) {
                object.update(deltaTime);
            }
        }
    }

}

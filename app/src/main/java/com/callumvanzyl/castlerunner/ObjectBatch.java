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

    public void offsetBatch(Vector2 offset) {
        for (GameObject object: objects) {
            Vector2 currentPosition = object.getPosition();
            object.setPosition(currentPosition.add(offset));
        }
    }

    public Vector2 getMaxBounds() {
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (GameObject object: objects) {
            Vector2 position = object.getPosition();
            if (position.x > maxX) {
                maxX = position.x;
            }
            if (position.y > maxY) {
                maxY = position.y;
            }
        }

        return new Vector2(maxX, maxY);
    }

    public Vector2 getMinBounds() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (GameObject object: objects) {
            Vector2 position = object.getPosition();
            if (position.x < minX) {
                minX = position.x;
            }
            if (position.y < minY) {
                minY = position.y;
            }
        }

        return new Vector2(minX, minY);
    }

    public Vector2 getSize() {
        Vector2 max = getMaxBounds();
        Vector2 min = getMinBounds();
        return new Vector2(max.x - min.x, max.y - min.y);
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

package com.callumvanzyl.castlerunner;

import android.graphics.Canvas;

import java.util.ArrayList;

class Chunk {

    private ObjectBatch objectBatch;

    Chunk() {
        objectBatch = new ObjectBatch();
    }

    public void addObject(GameObject object) {
        objectBatch.addObject(object);
    }

    public void offsetChunk(Vector2 offset) {
        objectBatch.offsetBatch(offset);
    }

    public Vector2 getMaxBounds() {
        return objectBatch.getMaxBounds();
    }

    public Vector2 getMinBounds() {
        return objectBatch.getMinBounds();
    }

    public ArrayList<GameObject> getObjects() {
        return objectBatch.getObjects();
    }

    public void removeObject(int i) {
        objectBatch.removeObject(i);
    }

    public Vector2 getSize() {
        return objectBatch.getSize();
    }

    public void drawChunk(Canvas canvas) {
        objectBatch.drawBatch(canvas);
    }

    public void updateChunk(float deltaTime) {
        objectBatch.updateBatch(deltaTime);
    }

}

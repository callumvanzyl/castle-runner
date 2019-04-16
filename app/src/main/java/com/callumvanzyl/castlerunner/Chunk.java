package com.callumvanzyl.castlerunner;

import android.graphics.Canvas;

class Chunk {
    private Vector2 size;

    private ObjectBatch objectBatch;

    Chunk(Vector2 size) {
        this.size = size;

        objectBatch = new ObjectBatch();
    }

    public void addObject(GameObject object) {
        objectBatch.addObject(object);
    }

    public Vector2 getMaxBounds() {
        return objectBatch.getMaxBounds();
    }

    public Vector2 getMinBounds() {
        return objectBatch.getMinBounds();
    }

    public void destroyChunk() {

    }

    public void offsetChunk(Vector2 offset) {
        objectBatch.offsetBatch(offset);
    }

    public void drawChunk(Canvas canvas) {
        objectBatch.drawBatch(canvas);
    }

    public void updateChunk(float deltaTime) {
        objectBatch.updateBatch(deltaTime);
    }

}

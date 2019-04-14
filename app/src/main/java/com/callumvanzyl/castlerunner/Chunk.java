package com.callumvanzyl.castlerunner;

import android.graphics.Canvas;

class Chunk {

    private Vector2 origin;
    private Vector2 size;

    private ObjectBatch objectBatch;

    Chunk(Vector2 size) {
        origin = Vector2.ZERO;
        this.size = size;

        objectBatch = new ObjectBatch();
    }

    public void drawChunk(Canvas canvas) {
        objectBatch.drawBatch(canvas);
    }

    public void updateChunk(float deltaTime) {
        objectBatch.updateBatch(deltaTime);
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }

}

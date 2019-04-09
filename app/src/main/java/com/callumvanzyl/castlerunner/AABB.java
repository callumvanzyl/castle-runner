package com.callumvanzyl.castlerunner;

class AABB {

    private Vector2 position;
    private Vector2 size;

    AABB(Vector2 position, Vector2 size) {
        this.position = position;
        this.size = size;
    }

    public boolean isColliding(AABB other) {
        return (position.x < other.position.x + other.size.x &&
                position.x + size.x > other.position.x &&
                position.y < other.position.y + other.size.y &&
                position.y + size.y > other.size.y);
    }

}

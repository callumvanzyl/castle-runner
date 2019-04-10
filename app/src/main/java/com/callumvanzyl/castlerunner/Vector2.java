package com.callumvanzyl.castlerunner;

class Vector2 {

    static final Vector2 ZERO = new Vector2(0, 0);

    int x;
    int y;

    Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 vec) {
        return new Vector2(this.x + vec.x, this.y + vec.y);
    }

    public Vector2 add(int x, int y) {
        return new Vector2(this.x + x, this.y + y);
    }

}

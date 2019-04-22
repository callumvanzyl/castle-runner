package com.callumvanzyl.castlerunner;

class ScrollingBackground extends GameObject {

    private int scrollingSpeed;

    ScrollingBackground(GameContext gameContext, int scrollingSpeed) {
        super(gameContext);

        this.scrollingSpeed = scrollingSpeed;
    }

    public void changeScreenSize(Vector2 screenSize) {
        setSize(new Vector2(screenSize.x*2, screenSize.y));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if ((getPosition().x+getSize().x) < getSize().x/2) {
            setPosition(Vector2.ZERO);
        }

        Vector2 currentPosition = getPosition();
        int newX = (int) (currentPosition.x - (scrollingSpeed*(deltaTime/100)));
        setPosition(new Vector2(newX, currentPosition.y));
    }

    public void setScrollingSpeed(int scrollingSpeed) {
        this.scrollingSpeed = scrollingSpeed;
    }
}

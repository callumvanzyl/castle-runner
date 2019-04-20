package com.callumvanzyl.castlerunner;

class UiElement extends GameObject {

    UiElement(GameContext gameContext) {
        super(gameContext);

        setCollidable(false);
    }

    public boolean isTouched() {
        Vector2 fingerPosition = gameContext.getFingerPosition();
        if (fingerPosition != null) {
            return (fingerPosition.x > getPosition().x && fingerPosition.x < getPosition().x + getSize().x
                    && fingerPosition.y > getPosition().y && fingerPosition.y < getPosition().y + getSize().y);
        }
        return false;
    }

}

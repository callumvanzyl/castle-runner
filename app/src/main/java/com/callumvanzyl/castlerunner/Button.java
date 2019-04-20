package com.callumvanzyl.castlerunner;

class Button extends UiElement {

    private String activeSpritePath;
    private String inactiveSpritePath;

    private boolean isActive;

    Button(GameContext gameContext, String activeSpritePath, String inactiveSpritePath) {
        super(gameContext);

        this.activeSpritePath = activeSpritePath;
        this.inactiveSpritePath = inactiveSpritePath;

        isActive = false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isActive) {
            setSprite(activeSpritePath);
        } else {
            setSprite(inactiveSpritePath);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}

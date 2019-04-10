package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.view.SurfaceHolder;

import java.util.logging.Handler;

class GameContext {

    private GameView gameView;

    private Context context;
    private Handler messageHandler;
    private SurfaceHolder surfaceHolder;

    GameContext(GameView gameView, Context context, Handler messageHandler, SurfaceHolder surfaceHolder) {
        this.gameView = gameView;
        this.context = context;
        this.messageHandler = messageHandler;
        this.surfaceHolder = surfaceHolder;
    }

    public GameView getGameView() {
        return gameView;
    }

    public Context getContext() {
        return context;
    }

    public Handler getMessageHandler() {
        return messageHandler;
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

}
package com.callumvanzyl.castlerunner;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

class Player extends AnimatedObject {

    private static boolean isInit = false;

    Player(Context context) {
        super(context);

        if (!isInit) {
            AnimatedObject.registerAnimation(
                    "player-die",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/player/die/1.png",
                            "textures/player/die/2.png",
                            "textures/player/die/3.png",
                            "textures/player/die/4.png",
                            "textures/player/die/5.png"
                    ))
            );

            AnimatedObject.registerAnimation(
                    "player-idle",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/player/idle/1.png"
                    ))
            );

            AnimatedObject.registerAnimation(
                    "player-jump",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/player/jump/1.png",
                            "textures/player/jump/2.png",
                            "textures/player/jump/3.png",
                            "textures/player/jump/4.png"
                    ))
            );

            AnimatedObject.registerAnimation(
                    "player-run",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/player/run/1.png",
                            "textures/player/run/2.png",
                            "textures/player/run/3.png",
                            "textures/player/run/4.png",
                            "textures/player/run/5.png",
                            "textures/player/run/6.png",
                            "textures/player/run/7.png"
                    ))
            );

            isInit = true;
        }

        setAnimation("player-run", true);
    }

}

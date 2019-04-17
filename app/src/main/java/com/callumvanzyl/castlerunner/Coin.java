package com.callumvanzyl.castlerunner;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

class Coin extends AnimatedObject {

    private static boolean isInit = false;

    Coin(Context context) {
        super(context);

        if (!isInit) {
            AnimatedObject.registerAnimation(
                    "coin-spinning",
                    1f,
                    new ArrayList<>(Arrays.asList(
                            "textures/loot/coin/1.png",
                            "textures/loot/coin/2.png",
                            "textures/loot/coin/3.png",
                            "textures/loot/coin/4.png"
                    ))
            );

            isInit = true;
        }

        setAnimation("coin-spinning", true);
    }

}

package org.glowacki.rpg.android;

import org.glowacki.rpg.core.RPG;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class RPGActivity extends AndroidApplication {
	
	@Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
       config.useGL20 = true;
       initialize(new RPG(), config);
   }
}

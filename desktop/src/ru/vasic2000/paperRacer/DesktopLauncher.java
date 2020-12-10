package ru.vasic2000.paperRacer;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = RaceGame.WIDTH;
		config.height = RaceGame.HEIGHT;
		config.title = RaceGame.TITLE;
		new LwjglApplication(new RaceGame(), config);
	}
}

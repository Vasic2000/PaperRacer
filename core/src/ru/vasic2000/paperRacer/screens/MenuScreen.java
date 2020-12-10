package ru.vasic2000.paperRacer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.vasic2000.paperRacer.RaceGame;
import ru.vasic2000.paperRacer.states.GameStateManager;
import ru.vasic2000.paperRacer.states.State;

public class MenuScreen extends State {

    private Texture background;
    private Texture buttonPlay;
    private Texture title;

    public MenuScreen(GameStateManager gsm) {
        super(gsm);
        background = new Texture("bg.jpg");
        buttonPlay = new Texture("playbtn.png");
        title = new Texture("title.png");

        camera.setToOrtho(false, RaceGame.WIDTH, RaceGame.HEIGHT);
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            gsm.set(new GameScreen(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        float x, y;
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(background, 0,0, RaceGame.WIDTH, RaceGame.HEIGHT);
        sb.draw(buttonPlay, camera.position.x - buttonPlay.getWidth() / 2, camera.position.y - 300);
        x =title.getWidth() / 2;
        y = 100;
        sb.draw(title, camera.position.x - 140, camera.position.y + 185, RaceGame.WIDTH / 1.75f, RaceGame.HEIGHT / 4f);
        sb.end();
    }

    @Override
    public void dispose() {
        buttonPlay.dispose();
        background.dispose();
        title.dispose();
    }
}

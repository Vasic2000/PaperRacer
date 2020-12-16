package ru.vasic2000.paperRacer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.vasic2000.paperRacer.RaceGame;
import ru.vasic2000.paperRacer.sprites.Car;
import ru.vasic2000.paperRacer.states.GameStateManager;
import ru.vasic2000.paperRacer.states.State;

class GameScreen extends State {

    private Texture track1, track2;
    private Vector2 trackPos1, trackPos2;
    private Car car;

    private float scaleX;

    public GameScreen(GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(false, RaceGame.WIDTH, RaceGame.HEIGHT);

        int x = RaceGame.WIDTH;
        int y = Gdx.graphics.getWidth();

        scaleX = (float) RaceGame.WIDTH / Gdx.graphics.getWidth();

        car = new Car();

        track1 = new Texture("track.jpg");
        track2 = new Texture("track.jpg");

        trackPos1 = new Vector2(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2);
        trackPos2 = new Vector2(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2 + track1.getHeight());
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isTouched()) {
            System.out.println(Gdx.input.getX() + " : " + Gdx.input.getY());
            if (Gdx.input.getX() * scaleX < car.getPosition().x + car.getCar().getWidth() / 2)
                car.goLeft();
            if (Gdx.input.getX() * scaleX > car.getPosition().x + car.getCar().getWidth() / 2)
                car.goRight();
        }

        if (!Gdx.input.isTouched()) {
            car.goStraight();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        moveTrack();
        car.update(dt);
        camera.position.y += car.getSpeed() * dt;
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(track1, trackPos1.x, trackPos1.y, track1.getWidth(), track1.getHeight());
        sb.draw(track2, trackPos2.x, trackPos2.y, track2.getWidth(), track2.getHeight());
        sb.draw(car.getCar(), car.getPosition().x, car.getPosition().y,
                car.getCar().getWidth(), car.getCar().getHeight());
        sb.end();
    }

    private void moveTrack() {

        if(camera.position.y - (camera.viewportHeight) / 2 > trackPos1.y + track1.getHeight())
            trackPos1.add(0, track1.getHeight() * 2);
        if(camera.position.y - (camera.viewportHeight) / 2 > trackPos2.y + track2.getHeight())
            trackPos2.add(0, track2.getHeight() * 2);
    }

    @Override
    public void dispose() {
        track1.dispose();
        track2.dispose();
    }

}

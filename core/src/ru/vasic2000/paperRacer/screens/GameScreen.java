package ru.vasic2000.paperRacer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import ru.vasic2000.paperRacer.RaceGame;
import ru.vasic2000.paperRacer.sprites.Obstacle;
import ru.vasic2000.paperRacer.sprites.Competitor;

import ru.vasic2000.paperRacer.sprites.Player;
import ru.vasic2000.paperRacer.states.GameStateManager;
import ru.vasic2000.paperRacer.states.State;

class GameScreen extends State {

    private Texture track1, track2;
    private Texture accelerator;
    private Vector2 trackPos1, trackPos2;
    private Player player;
    private Competitor competitor1;
    private Competitor competitor2;

    private Obstacle small_repair;
    private Obstacle big_repair;

    private Random rnd;

    private float scaleX, scaleY;

//    private float averageSpeed;

    public GameScreen(GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(false, RaceGame.WIDTH, RaceGame.HEIGHT);
        rnd = new Random();

        int x = RaceGame.WIDTH;
        int y = Gdx.graphics.getWidth();

        scaleX = (float) RaceGame.WIDTH / Gdx.graphics.getWidth();
        scaleY = (float) RaceGame.HEIGHT / Gdx.graphics.getHeight();

        player = new Player("mclaren.png", 240, 130);
        competitor1 = new Competitor("williams.png", 100, 400);
        competitor2 = new Competitor("benetton.png", 375, 300);
        big_repair = new Obstacle("building_yard.png", rnd.nextInt(480), 3000 + rnd.nextInt(1000));
        small_repair = new Obstacle("small_repair.png", rnd.nextInt(480), 1000 + rnd.nextInt(1000));


        track1 = new Texture("track.jpg");
        track2 = new Texture("track.jpg");
        accelerator = new Texture("accelerator.png");

        trackPos1 = new Vector2(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2);
        trackPos2 = new Vector2(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2 + track1.getHeight());

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isTouched()) {

            if((Gdx.input.getX() * scaleX > 180) && (Gdx.input.getX() * scaleX < 300) &&
                    (Gdx.input.getY() * scaleY > 0) && (Gdx.input.getY() * scaleY < 300))
                player.increaseSpeed();

            else {
                player.disposeSpeed();
                if (Gdx.input.getX() * scaleX < player.getPosition().x + player.getCar().getWidth() / 2)
                    player.goLeft();
                if (Gdx.input.getX() * scaleX > player.getPosition().x + player.getCar().getWidth() / 2)
                    player.goRight();
            }
        }

        if (!Gdx.input.isTouched()) {
            player.goStraight();
            player.disposeSpeed();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

        if(player.getCarBounds().overlaps(big_repair.getBYBounds())) {
            player.setSpeed(0);
        }

        if(player.getCarBounds().overlaps(small_repair.getBYBounds())) {
            player.setSpeed(0);
        }

//        if (player.getCarBounds().overlaps(competitor1.getCarBounds())) {
////        Столкновение боком
//            if (Math.abs(player.getPosition().x - competitor1.getPosition().x) <
//                    Math.abs(player.getPosition().y - competitor1.getPosition().y)) {
//                averageSpeed = (player.getSpeedH() + competitor1.getSpeedH());
//                competitor1.setSpeedH(averageSpeed);
//                player.setSpeedH(averageSpeed);
//            }
//
////        Столкновение сзади
//            if (Math.abs(player.getPosition().x - competitor1.getPosition().x) >
//                    Math.abs(player.getPosition().y - competitor1.getPosition().y)) {
//                averageSpeed = (player.getSpeed() + competitor1.getSpeed());
//                player.setSpeed(averageSpeed);
//                competitor1.setSpeed(averageSpeed);
//            }
//        }
//
//        if (player.getCarBounds().overlaps(competitor2.getCarBounds())) {
////        Столкновение боком
//            if (Math.abs(player.getPosition().x - competitor2.getPosition().x) <
//                    Math.abs(player.getPosition().y - competitor2.getPosition().y)) {
//                averageSpeed = (player.getSpeedH() + competitor2.getSpeedH())/2;
//                player.setSpeedH(averageSpeed);
//                competitor2.setSpeedH(averageSpeed);
//            }
//
////        Столкновение сзади
//            if (Math.abs(player.getPosition().x - competitor2.getPosition().x) >
//                    Math.abs(player.getPosition().y - competitor2.getPosition().y)) {
//                averageSpeed = (player.getSpeed() + competitor2.getSpeed())/2;
//                player.setSpeed(averageSpeed);
//                competitor2.setSpeed(averageSpeed);
//            }
//        }

        player.update(dt);
        competitor1.update(dt);
        competitor2.update(dt);

        moveTrack();

        camera.position.y += player.getSpeed() * dt;
        camera.update();

        if(big_repair.getPosition().y < camera.position.y - 400 - big_repair.getBY().getHeight()) {
            int x = rnd.nextInt(300);
            int y = (int) camera.position.y + 1000 + rnd.nextInt(3000);

            while(Math.abs(y - small_repair.getPosition().y) < big_repair.getBY().getHeight() + 500)
                y = (int) camera.position.y + 1000 + rnd.nextInt(3000);
            big_repair.setObstacle_Position(x, y);
            big_repair.setObstacle_Bounds(x, y);
        }

        if(small_repair.getPosition().y < camera.position.y - 400 - small_repair.getBY().getHeight()) {
            int x = rnd.nextInt(380);
            int y = (int) camera.position.y + 1000 + rnd.nextInt(3000);

            while(Math.abs(y - big_repair.getPosition().y) < small_repair.getBY().getHeight() + 500)
                y = (int) camera.position.y + 1000 + rnd.nextInt(3000);
            small_repair.setObstacle_Position(x, y);
            small_repair.setObstacle_Bounds(x, y);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);

        sb.begin();
        sb.draw(track1, trackPos1.x, trackPos1.y, track1.getWidth(), track1.getHeight());
        sb.draw(track2, trackPos2.x, trackPos2.y, track2.getWidth(), track2.getHeight());

        sb.draw(player.getCar(), player.getPosition().x, player.getPosition().y,
                player.getCar().getWidth(), player.getCar().getHeight());
        sb.draw(competitor1.getCar(), competitor1.getPosition().x, competitor1.getPosition().y,
                competitor1.getCar().getWidth(), competitor1.getCar().getHeight());
        sb.draw(competitor2.getCar(), competitor2.getPosition().x, competitor2.getPosition().y,
                competitor2.getCar().getWidth(), competitor2.getCar().getHeight());

        sb.draw(big_repair.getBY(), big_repair.getPosition().x, big_repair.getPosition().y,
                big_repair.getBY().getWidth(), big_repair.getBY().getHeight());
        sb.draw(small_repair.getBY(), small_repair.getPosition().x, small_repair.getPosition().y,
                small_repair.getBY().getWidth(), small_repair.getBY().getHeight());

        sb.draw(accelerator, 180, player.getPosition().y + 600, 85, 140);

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
        player.dispose();
        competitor1.dispose();
        competitor2.dispose();
        small_repair.dispose();
        big_repair.dispose();
    }

}

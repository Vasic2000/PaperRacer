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

    private Texture track1, track2, track3, track4;
    private Texture accelerator;
    private Vector2 trackPos1, trackPos2, trackPos3, trackPos4;
    private Player player;
    private Competitor competitor1;
    private Competitor competitor2;

    private Obstacle small_repair;
    private Obstacle big_repair;

    private Random rnd;

    private float scaleX, scaleY;

    private float averageSpeed;

    public GameScreen(GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(false, RaceGame.WIDTH, RaceGame.HEIGHT);
        rnd = new Random();

        scaleX = (float) RaceGame.WIDTH / Gdx.graphics.getWidth();
        scaleY = (float) RaceGame.HEIGHT / Gdx.graphics.getHeight();

        initGameObjects();
    }

    private void initGameObjects() {
        player = new Player("mclaren.png", 240, 130);
        competitor1 = new Competitor("williams.png", 100, 400);
        competitor2 = new Competitor("benetton.png", 375, 300);
        big_repair = new Obstacle("building_yard.png", rnd.nextInt(480),
                3000 + rnd.nextInt(1000));
        small_repair = new Obstacle("small_repair.png", rnd.nextInt(480),
                1000 + rnd.nextInt(1000));

        competitor1.setSmallRepairPosition((int) small_repair.getPosition().x, (int) small_repair.getPosition().y,
                small_repair.getBY().getWidth(), small_repair.getBY().getHeight());
        competitor2.setSmallRepairPosition((int) small_repair.getPosition().x, (int) small_repair.getPosition().y,
                small_repair.getBY().getWidth(), small_repair.getBY().getHeight());

        competitor1.setBigRepairPosition((int) big_repair.getPosition().x, (int) big_repair.getPosition().y,
                big_repair.getBY().getWidth(), big_repair.getBY().getHeight());
        competitor2.setBigRepairPosition((int) big_repair.getPosition().x, (int) big_repair.getPosition().y,
                big_repair.getBY().getWidth(), big_repair.getBY().getHeight());

        track1 = new Texture("track01.jpg");
        track2 = new Texture("track02.jpg");
        track3 = new Texture("track03.jpg");
        track4 = new Texture("track04.jpg");
        accelerator = new Texture("accelerator.png");

        trackPos1 = new Vector2(camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2);
        trackPos2 = new Vector2(camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2
                + track1.getHeight());
        trackPos3 = new Vector2(camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2
                + track1.getHeight() + track2.getHeight());
        trackPos4 = new Vector2(camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2
                + track1.getHeight() + track2.getHeight() + track3.getHeight());
    }

    @Override
    protected void handleInput() {
        boolean firstFingerTouching = Gdx.input.isTouched(0);
        boolean secondFingerTouching = Gdx.input.isTouched(1);
        boolean speedUp1;
        boolean speedUp2;

        if ((Gdx.input.getX(0) * scaleX > 180) && (Gdx.input.getX(0) * scaleX < 300) &&
                (Gdx.input.getY(0) * scaleY > 0) && (Gdx.input.getY(0) * scaleY < 200)) {
            player.increaseSpeed();
            speedUp1 = true;
        } else speedUp1 = false;

        if ((Gdx.input.getX(1) * scaleX > 180) && (Gdx.input.getX(1) * scaleX < 300) &&
                (Gdx.input.getY(1) * scaleY > 0) && (Gdx.input.getY(1) * scaleY < 200)) {
            player.increaseSpeed();
            speedUp2 = true;
        } else speedUp2 = false;

        if(!speedUp1 && !speedUp2) {
            player.disposeSpeed();
        }

        if (firstFingerTouching && !speedUp1) {
            if (Gdx.input.getX(0) * scaleX < player.getPosition().x + player.getCar().getWidth() / 2)
                player.goLeft();
            if (Gdx.input.getX(0) * scaleX > player.getPosition().x + player.getCar().getWidth() / 2)
                player.goRight();
        }

        if (secondFingerTouching && !speedUp2) {
            if (Gdx.input.getX(1) * scaleX < player.getPosition().x + player.getCar().getWidth() / 2)
                player.goLeft();
            if (Gdx.input.getX(1) * scaleX > player.getPosition().x + player.getCar().getWidth() / 2)
                player.goRight();
        }

        if (!firstFingerTouching && !secondFingerTouching) {
            player.goStraight();
            player.disposeSpeed();
        }

        if((!firstFingerTouching && speedUp2) || (!secondFingerTouching && speedUp1)) {
            player.goStraight();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

        checkCollisions(dt);
        player.update(dt);

        player.changeSpeed(dt);

        competitor1.changeSpeed(dt);
        competitor1.update(dt);

        competitor2.changeSpeed(dt);
        competitor2.update(dt);

        infinityTrack();

        camera.position.y = player.getPosition().y + 340;
        camera.update();

        infinityObstacle();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);

        sb.begin();
        sb.draw(track1, trackPos1.x, trackPos1.y, track1.getWidth(), track1.getHeight());
        sb.draw(track2, trackPos2.x, trackPos2.y, track2.getWidth(), track2.getHeight());
        sb.draw(track3, trackPos3.x, trackPos3.y, track3.getWidth(), track3.getHeight());
        sb.draw(track4, trackPos4.x, trackPos4.y, track4.getWidth(), track4.getHeight());

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

    @Override
    public void dispose() {
        track1.dispose();
        track2.dispose();
        track3.dispose();
        track4.dispose();
        player.dispose();
        competitor1.dispose();
        competitor2.dispose();
        small_repair.dispose();
        big_repair.dispose();
    }

    private void infinityTrack() {
        if(camera.position.y - (camera.viewportHeight) / 2 > trackPos1.y + track1.getHeight())
            trackPos1.add(0, track1.getHeight() * 4);
        if(camera.position.y - (camera.viewportHeight) / 2 > trackPos2.y + track2.getHeight())
            trackPos2.add(0, track2.getHeight() * 4);
        if(camera.position.y - (camera.viewportHeight) / 2 > trackPos3.y + track3.getHeight())
            trackPos3.add(0, track3.getHeight() * 4);
        if(camera.position.y - (camera.viewportHeight) / 2 > trackPos4.y + track4.getHeight())
            trackPos4.add(0, track4.getHeight() * 4);
    }

    private void infinityObstacle() {
        if(big_repair.getPosition().y < camera.position.y - 400 - big_repair.getBY().getHeight()) {
            int x = rnd.nextInt(300);
            int y = (int) camera.position.y + 1000 + rnd.nextInt(3000);

            while(Math.abs(y - small_repair.getPosition().y) < big_repair.getBY().getHeight() + 500)
                y = (int) camera.position.y + 1000 + rnd.nextInt(3000);
            big_repair.setObstacle_Position(x, y);
            big_repair.setObstacle_Bounds(x, y);
            competitor1.setBigRepairPosition(x, y, big_repair.getBY().getWidth(), big_repair.getBY().getHeight());
            competitor2.setBigRepairPosition(x, y, big_repair.getBY().getWidth(), big_repair.getBY().getHeight());
        }

        if(small_repair.getPosition().y < camera.position.y - 400 - small_repair.getBY().getHeight()) {
            int x = rnd.nextInt(380);
            int y = (int) camera.position.y + 1000 + rnd.nextInt(3000);

            while(Math.abs(y - big_repair.getPosition().y) < small_repair.getBY().getHeight() + 500)
                y = (int) camera.position.y + 1000 + rnd.nextInt(3000);
            small_repair.setObstacle_Position(x, y);
            small_repair.setObstacle_Bounds(x, y);
            competitor1.setSmallRepairPosition(x, y, small_repair.getBY().getWidth(), small_repair.getBY().getHeight());
            competitor2.setSmallRepairPosition(x, y, small_repair.getBY().getWidth(), small_repair.getBY().getHeight());
        }
    }

    private void checkCollisions(float dt) {
        if(player.getCarBounds().overlaps(big_repair.getBYBounds())) {
            player.setSpeed(0);
        }

        if(player.getCarBounds().overlaps(small_repair.getBYBounds())) {
            player.setSpeed(0);
        }

        if (player.getCarBounds().overlaps(competitor1.getCarBounds())) {
//        Столкновение боком
            if (Math.abs(player.getPosition().x - competitor1.getPosition().x) >
                    Math.abs(player.getPosition().y - competitor1.getPosition().y)) {
                if(!competitor1.leftBounds() && !competitor1.rightBounds()) {
                    averageSpeed = (player.getSpeedH() + competitor1.getSpeedH()) / 2;
                    player.setSpeedH(averageSpeed);
                    competitor1.setSpeedH(averageSpeed);
                } else {
                    if(competitor1.leftBounds()) {
                        player.setSpeedH(155);
                        competitor1.setSpeedH(0);

                        averageSpeed = player.getSpeed();
                        player.setSpeed(0);
                        player.update(dt);
                        player.setSpeed(averageSpeed);
                    }
                    if(competitor1.rightBounds()) {
                        player.setSpeedH(-155);
                        competitor1.setSpeedH(0);

                        averageSpeed = player.getSpeed();
                        player.setSpeed(0);
                        player.update(dt);
                        player.setSpeed(averageSpeed);
                    }
                }
            }
//        Столкновение сзади
            if (Math.abs(player.getPosition().x - competitor1.getPosition().x) <
                    Math.abs(player.getPosition().y - competitor1.getPosition().y)) {
                averageSpeed = (player.getSpeed() + competitor1.getSpeed()) / 2;

                if (competitor1.getSpeed() == 0) {
                    player.setSpeed(-5);
                    player.update(dt);
                }

//              Разлепить машинки
                if (player.getPosition().y > competitor1.getPosition().y) {
                    player.setSpeed(averageSpeed + 5);
                    player.update(dt);
                }
                if (player.getPosition().y < competitor1.getPosition().y) {
                    competitor1.setSpeed(averageSpeed - 5);
                    competitor1.update(dt);
                }
            }
        }

        if (player.getCarBounds().overlaps(competitor2.getCarBounds())) {
//        Столкновение боком
            if (Math.abs(player.getPosition().x - competitor2.getPosition().x) >
                    Math.abs(player.getPosition().y - competitor2.getPosition().y)) {
                if(!competitor2.leftBounds() && !competitor2.rightBounds()) {
                    averageSpeed = (player.getSpeedH() + competitor2.getSpeedH()) / 2;
                    player.setSpeedH(averageSpeed);
                    competitor2.setSpeedH(averageSpeed);
                } else {
                    if(competitor2.leftBounds()) {
                        player.setSpeedH(155);
                        competitor2.setSpeedH(0);

                        averageSpeed = player.getSpeed();
                        player.setSpeed(0);
                        player.update(dt);
                        player.setSpeed(averageSpeed);
                    }
                    if(competitor2.rightBounds()) {
                        player.setSpeedH(-155);
                        competitor2.setSpeedH(0);

                        averageSpeed = player.getSpeed();
                        player.setSpeed(0);
                        player.update(dt);
                        player.setSpeed(averageSpeed);
                    }
                }
            }
//        Столкновение сзади
            if (Math.abs(player.getPosition().x - competitor2.getPosition().x) <
                    Math.abs(player.getPosition().y - competitor2.getPosition().y)) {
                averageSpeed = (player.getSpeed() + competitor2.getSpeed()) / 2;

                if (competitor2.getSpeed() == 0) {
                    player.setSpeed(-5);
                    player.update(dt);
                }

//              Разлепить машинки
                if(player.getPosition().y > competitor2.getPosition().y) {
                    player.setSpeed(averageSpeed + 5);
                    player.update(dt);
                }
                if(player.getPosition().y < competitor2.getPosition().y) {
                    competitor2.setSpeed(averageSpeed + 5);
                    competitor2.update(dt);
                }
            }


        }
    }

}

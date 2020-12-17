package ru.vasic2000.paperRacer.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Texture car;
    private Vector2 carPosition;
    private Rectangle carBounds;

    public float getSpeed() {
        return speed;
    }

    float speed, speedH;
    private int acceleration;

    public Player(String picture, int x, int y) {
        car = new Texture(picture);
        speed = 0;
        speedH = 0;
        acceleration = 0;

        carPosition = new Vector2(x - car.getWidth() /  2,
                y - car.getHeight() / 2);

        carBounds = new Rectangle(carPosition.x, carPosition.y, car.getWidth(), car.getHeight());
    }

    public void dispose() {
        car.dispose();
    }

    public Vector2 getPosition() {
        return carPosition;
    }

    public void update(float dt) {
        speed = changeSpeed(speed, dt);

        carPosition.add(speedH * dt, speed * dt);

        if(carPosition.x < 0) carPosition.x = 0;
        if(carPosition.x > (480 - car.getWidth())) carPosition.x = 480 - car.getWidth();

        carBounds.setPosition(carPosition.x, carPosition.y);
    }

    private float changeSpeed(float speed, float dt) {
        if((acceleration == 1) && speed < 350) {
            speed = speed + 25 * dt;
            return speed;
        }

        if(speed < 250) {
            if ((speed + 25 * dt) < 250)
                speed += 25 * dt;
            else
                speed = 250;
        }

        if(speed > 250) {
            if ((speed - 25 * dt) > 250)
                speed -= 25 * dt;
            else
                speed = 250;
        }

        return speed;
    }

    public Texture getCar() {
        return car;
    }

    public void goLeft() {
        speedH = - 150;
    }

    public void goRight() {
        speedH = 150;
    }

    public void goStraight() {
        speedH = 0;
    }

    public void increaseSpeed() {
        acceleration = 1;
    }

    public void disposeSpeed() {
        acceleration = 0;
    }
}

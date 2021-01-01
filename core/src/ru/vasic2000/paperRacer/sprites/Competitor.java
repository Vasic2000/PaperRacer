package ru.vasic2000.paperRacer.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Competitor {
    private Texture car;
    private Vector2 carPosition;

    private Rectangle carBounds;

    float speed, speedH;

    public Competitor(String picture, int x, int y) {
        car = new Texture(picture);
        speed = 0;
        speedH = 0;

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

    private float changeSpeed(float speed, float dt) {
        if(speed < 250) {
            if ((speed + 35 * dt) < 175)
                speed += 35 * dt;
            else
                speed = 175;
        }

        if(speed > 250) {
            if ((speed - 35 * dt) > 250)
                speed -= 35 * dt;
            else
                speed = 250;
        }

        return speed;
    }

    public Rectangle getCarBounds() {
        return carBounds;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeedH() {
        return speedH;
    }

    public void setSpeedH(float speed) {
        this.speedH = speed;
    }
}

package ru.vasic2000.paperRacer.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Car {
    private Texture car;
    private Vector2 carPosition;
    private Rectangle carBounds;

    public int getSpeed() {
        return speed;
    }

    int speed, speedH;

    public Car() {
        car = new Texture("mclaren.png");
        speed = 250;
        speedH = 0;

        carPosition = new Vector2(240 - car.getWidth() /  2,
                150 - car.getHeight() / 2);

        carBounds = new Rectangle(carPosition.x, carPosition.y, car.getWidth(), car.getHeight());
    }

    public void dispose() {
        car.dispose();
    }

    public Vector2 getPosition() {
        return carPosition;
    }

    public void update(float dt) {
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
}

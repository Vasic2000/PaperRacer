package ru.vasic2000.paperRacer.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Competitor {
    private Texture car;
    private Vector2 carPosition;
    private Rectangle carBounds, big_repair, small_repair;

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

        carPosition.add(speedH * dt, speed * dt);

        if(leftBounds()) {
            carPosition.x = 0;
            speedH = 0;
        }
        if(rightBounds()) {
            carPosition.x = 480 - car.getWidth();
            speedH = 0;
        }

        if((frontalCollision(small_repair)) || (frontalCollision(big_repair)))
            carPosition.add(0, -speed * dt);

        carBounds.setPosition(carPosition.x, carPosition.y);
    }

    public boolean rightBounds() {
        return carPosition.x >= (480 - car.getWidth());
    }

    public boolean leftBounds() {
        return carPosition.x <= 0;
    }

    public boolean frontalCollision(Rectangle obstacle) {
        if (((carPosition.x + car.getWidth() >= obstacle.getX()) &&
                        (carPosition.x <= obstacle.getX() + obstacle.getWidth())) &&
                ((carPosition.y + car.getHeight() >= obstacle.getY()) &&
                        (carPosition.y + car.getHeight() <= obstacle.getY() + obstacle.getHeight())))
            return true;
        else
            return false;
    }

    public Texture getCar() {
        return car;
    }

    public void changeSpeed(float dt) {
//        SPEED
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

        if((frontalCollision(small_repair)) || (frontalCollision(big_repair)))
            speed = 0;

//        SPEEDH
        float leftDiff, rightDiff;
        if(small_repair.getY() < big_repair.getY()) {
            leftDiff = small_repair.getX() - carPosition.x - car.getWidth();
            rightDiff = small_repair.getX() + small_repair.getWidth() - carPosition.x;

            if((leftDiff < rightDiff) && (small_repair.getX() > car.getWidth()) && (leftDiff > 0))
                speedH = -25;
            else
                if(rightDiff > 0)
                    speedH = 25;

        } else {
            leftDiff = big_repair.getX() - carPosition.x - car.getWidth();
            rightDiff = big_repair.getX() + big_repair.getWidth() - carPosition.x;
        }

        if((leftDiff < rightDiff) && (big_repair.getX() > car.getWidth()) && (leftDiff > 0))
            speedH = -25;
        else
        if(rightDiff > 0)
            speedH = 25;
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

    public void setSmallRepairPosition(int x, int y, int width, int height) {
        small_repair = new Rectangle(x, y, width, height);
    }

    public void setBigRepairPosition(int x, int y, int width, int height) {
        big_repair = new Rectangle(x, y, width, height);
    }
}

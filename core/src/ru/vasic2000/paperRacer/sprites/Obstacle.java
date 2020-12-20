package ru.vasic2000.paperRacer.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {
    private Texture obstacle;
    private Vector2 obstacle_Position;
    private Rectangle obstacle_Bounds;

    public Obstacle(String picture, int x, int y) {
        obstacle = new Texture(picture);
        obstacle_Bounds = new Rectangle(x, y, obstacle.getWidth(), obstacle.getHeight());
        obstacle_Position = new Vector2(x , y);
    }

    public Vector2 getPosition() {
        return obstacle_Position;
    }

    public void dispose() {
        obstacle.dispose();
    }

    public Texture getBY() {
        return obstacle;
    }

    public Rectangle getBYBounds() {
        return obstacle_Bounds;
    }
}

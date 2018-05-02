package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Player
{
    private Vector2 position;
    private ExtendViewport viewport;
    private int deaths = 0;
    private Sound hit;

    public Player(ExtendViewport viewport)
    {
        this.viewport = viewport;
        init();
    }

    public void init()
    {
        position = new Vector2(this.viewport.getWorldWidth() / 2, 2.0F);
    }

    public void renderPlayer(ShapeRenderer renderer)
    {
        renderer.setColor(Color.BLACK);
        renderer.set(ShapeRenderer.ShapeType.Filled);

        renderer.circle(this.position.x, this.position.y, 0.5F, 20);
        Vector2 torsoTop = new Vector2(this.position.x, this.position.y - 0.5F);
        Vector2 torsoBottom = new Vector2(torsoTop.x, torsoTop.y - 1.0F);

        renderer.rectLine(torsoTop, torsoBottom, 0.1F);
        renderer.rectLine(torsoTop.x, torsoTop.y, torsoTop.x + 0.5F, torsoTop.y - 0.5F, 0.1F);
        renderer.rectLine(torsoTop.x, torsoTop.y, torsoTop.x - 0.5F, torsoTop.y - 0.5F, 0.1F);
        renderer.rectLine(torsoBottom.x, torsoBottom.y, torsoBottom.x + 0.5F, torsoBottom.y - 0.5F, 0.1F);
        renderer.rectLine(torsoBottom.x, torsoBottom.y, torsoBottom.x - 0.5F, torsoBottom.y - 0.5F, 0.1F);
    }

    public void updatePlayerPosition(float delta)
    {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            position.x -= delta * Constants.PLAYER_VELOCITY;


        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            position.x += delta * Constants.PLAYER_VELOCITY;


        // the 2 lines below allows Android users to play the game by tilting their phones

        float yAxis = -Gdx.input.getAccelerometerY() / 9.8f * Constants.ACCELEROMETER_SENSITIVITY;
        position.x += -delta * yAxis * Constants.PLAYER_VELOCITY;

        inBounds(viewport);
    }

    public boolean hitByMeteor(MeteorShower meteorShower)
    {
        boolean isHit = false;

        for(Meteor meteor : meteorShower.meteors)
        {
            if(meteor.getPosition().y + Constants.METEOR_RADIUS <= this.position.y + 0.5f &&
                    (meteor.getPosition().x >= this.position.x - 0.5f && meteor.getPosition().x <= this.position.x + 0.5f))
            {
                isHit = true;
                hit = Gdx.audio.newSound(Gdx.files.internal("sound/hit.mp3"));
                hit.play(1f);
                deaths++;
                meteorShower.setScore(0);
            }
        }
        //hit.dispose();
        return isHit;
    }

    private void inBounds(ExtendViewport viewport)
    {
        if(position.x - 0.5F < 0)
            position.x = 0.5F;

        if(position.x + 0.5F > viewport.getWorldWidth())
            position.x = viewport.getWorldWidth() - 0.5F;

    }

    public int getDeaths()
    {
        return this.deaths;
    }
}

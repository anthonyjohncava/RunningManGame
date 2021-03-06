package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * This class implements the Main Menu Screen.
 */
public class MenuScreen implements Screen {

    MyGdxGame game;
    private SpriteBatch batch;      // Will render the UI elements.
    private Skin skin;              // The skin/style of the buttons.
    private Stage stage;            // This will hold the button Actors.

    public MenuScreen(MyGdxGame game) {
        this.game = game;
    }

    public void create() {
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        stage = new Stage();

        // Play button. When pressed, it opens the GameScreen.
        final TextButton btn_play = new TextButton("Play", skin);
        btn_play.setWidth(500f);
        btn_play.setHeight(200f);
        btn_play.setPosition(Gdx.graphics.getWidth() /2 - 300f, Gdx.graphics.getHeight()/2 + 100);
        btn_play.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                game.setScreen(MyGdxGame.gameScreen);
            }
        });

        // Exit button. When pressed, it closes the app.
        final TextButton btn_exit = new TextButton("Exit", skin);
        btn_exit.setWidth(500f);
        btn_exit.setHeight(200f);
        btn_exit.setPosition(Gdx.graphics.getWidth() /2 - 300f, Gdx.graphics.getHeight()/2 - 200f);
        btn_exit.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Adds the play and exit buttons to the stage to be rendered.
        stage.addActor(btn_play);
        stage.addActor(btn_exit);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        create();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Renders buttons
        batch.begin();
        stage.draw();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}

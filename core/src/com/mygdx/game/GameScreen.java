package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameScreen implements Screen {
    MyGdxGame game;
    private Music backgroundMusic;
    SpriteBatch spriteBatch;

    // Character running
    Texture runningSheet;
    TextureRegion[] runFrames;
    // Rows and cols of the running spritesheet
    private static final int FRAME_COLS = 4;
    private static final int FRAME_ROWS = 2;
    // Variables for the running animation
//    Animation runAnimation;		// This will store the array containing all of the frames, which is the walkFrames. It will also have the defined duration (in seconds) for each frame
//    TextureRegion currentFrame;
    // Position of the character
    private static final int character_posX = 10;
    private static final int character_posY =10;
    // Size of the character
    private static final int character_height = 300;
    private static final int character_width = 300;


//    int frameIndex;
//    float stateTime;

    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    public void create() {
        spriteBatch = new SpriteBatch();

        // background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        // Running character---------------
        runningSheet = new Texture(Gdx.files.internal("assets/running.png"));
        // Creates a 2D array of the given spritesheet
        TextureRegion[][] temp = TextureRegion.split(runningSheet, runningSheet.getWidth() / FRAME_COLS, runningSheet.getHeight() / FRAME_ROWS);
        // We want the above code to only be a single array. This sets the size of the array.
        runFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        // Store each texture on the runFrames array.
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                runFrames[index++] = temp[i][j];    // i++ is post increment.
            }
//            // Animation setup
//            runAnimation = new Animation(0.4f, runFrames);
//            stateTime = 0.0f;
        }
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen: ","gameScreen show called");
        create();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        // Running animation -----------------
//        stateTime += Gdx.graphics.getDeltaTime();
//        currentFrame = (TextureRegion) runAnimation.getKeyFrame(stateTime, true);
        // Draw character
        spriteBatch.begin();
        spriteBatch.draw(runFrames[0], character_posX, character_posY, character_width, character_height);
        spriteBatch.end();
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

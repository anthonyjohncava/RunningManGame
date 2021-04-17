package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameScreen implements Screen {
    MyGdxGame game;
    private Music backgroundMusic;                          // Background music while playing the game
    SpriteBatch spriteBatch;                                // Spritebatch for rendering

    // Main character variables
    Texture runningSheet;                                   // Texture to hold the spritesheet
    TextureRegion[] runFrames;                              // Texture array for the running frames
    private static final int FRAME_COLS = 4;                // Number of columns of the running spritesheet
    private static final int FRAME_ROWS = 2;                // Number of rows of the running spritesheet
    private static final int character_height = 210;        // Height of the character
    private static final int character_width = 140;         // Width of the character

    // Variables for the running animation
    Animation runAnimation;		                            // Stores the array containing all of runFrames. It will also have the defined duration (in seconds) for each frame
    TextureRegion currentFrame;                             // Current frame to display

    float stateTime;                                        // The time the program has been running.

    // variables for the tiledMap
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;
//    int frameIndex;

    int characterX;
    int characterY;

    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    public void create() {
        // Loaded the background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));

        // Running character------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Loaded the runnning spritesheet
        runningSheet = new Texture(Gdx.files.internal("assets/running.png"));

        // Creates a 2D array of the given spritesheet
        TextureRegion[][] temp = TextureRegion.split(runningSheet, runningSheet.getWidth() / FRAME_COLS, runningSheet.getHeight() / FRAME_ROWS);

        // Sets the size of the array for the above code to only be a 1D array
        runFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];

        // Transfers each texture on the temp 2D array to the 1D runFrames array.
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                runFrames[index++] = temp[i][j];    // i++ is post increment.
            }
        }

        // Sets the runFrames TextureTesgion into an Animation object, with a framerate set to 0.033, which is 30 frames per second.
        runAnimation = new Animation(0.033f, runFrames);
        // Running character------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // TiledMap---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Initialised the TiledMap and its renderer.
        tiledMap = new TmxMapLoader().load("assets/level1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        camera.position.set(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/2+100,0);



        // TiledMap---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Initialises the stateTime
        stateTime = 0.0f;

        // initialises the spriteBatch
        spriteBatch = new SpriteBatch();

        // Move the character
        characterX = 10;
        characterY = 410;
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

        // Starts background music
        backgroundMusic.setLooping(true);
        backgroundMusic.play();



        // Moves the camera
        camera.translate(0, 0);
        camera.update();
        // render tiledMap
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // Updates the stateTime using the deltaTime (to have the same time across all devices with different processors)
        stateTime += Gdx.graphics.getDeltaTime();

        // Gets the currentFrame of the running animation
        currentFrame = (TextureRegion) runAnimation.getKeyFrame(stateTime, true);

        // Draws the character's currentframe on the screen, with a set position, and the size of the character.
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, characterX, characterY, character_width, character_height);
        spriteBatch.end();


        characterX += 10;

    }

    private void update() {


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

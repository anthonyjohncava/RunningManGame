package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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
    private final int character_width = 140;                // Width of the character
    // Variables for the running animation
    Animation runAnimation;		                            // Stores the array containing all of runFrames. It will also have the defined duration (in seconds) for each frame
    TextureRegion currentFrame;                             // Current frame to display

    float stateTime;                                        // The time the program has been running.

    // variables for the tiledMap
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;

    private static String state = "run";

    private static int characterX;                          // Character's X position
    private static int characterY;                // Character's Y position
    private final int jumpHeight = 105;

    Texture slimeSheet;
    TextureRegion[] slimeFrames;
    private static final int FRAME_COLS_SLIME = 2;                // Number of columns of the slime spritesheet
    private static final int FRAME_ROWS_SLIME = 1;                // Number of rows of the slime spritesheet
    Animation runAnimation_slime;		                            // Stores the array containing all of runFrames. It will also have the defined duration (in seconds) for each frame
    TextureRegion currentFrame_slime;
    private static int slimeX;                          // Slime's X position
    private static int slimeY = 410;                          // Slime's X position

    private static int jumpStart;

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

        // Enemy1 ------------------------------------------------------------------------------------------------------
        slimeSheet = new Texture(Gdx.files.internal("assets/slime.png"));
        temp = TextureRegion.split(slimeSheet, slimeSheet.getWidth()/FRAME_COLS_SLIME, slimeSheet.getHeight()/FRAME_ROWS_SLIME);
        slimeFrames = new TextureRegion[FRAME_COLS_SLIME * FRAME_ROWS_SLIME];
        index = 0;
        for (int i = 0; i < FRAME_ROWS_SLIME; i++) {
            for (int j = 0; j < FRAME_COLS_SLIME; j++) {
                slimeFrames[index++] = temp[i][j];    // i++ is post increment.
            }
        }
        runAnimation_slime = new Animation(0.033f, slimeFrames);
        // Enemy1 ------------------------------------------------------------------------------------------------------

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
        characterX = 0;
        newGame();
        // Starts background music
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    private void newGame() {
        characterY = 410;

    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen: ","gameScreen show called");
        create();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Starts background music
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        // Moves the camera
        camera.update();
        // render tiledMap
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // Updates the stateTime using the deltaTime (to have the same time across all devices with different processors)
        stateTime += Gdx.graphics.getDeltaTime();

        // Gets the currentFrame of the running animation
        currentFrame = (TextureRegion) runAnimation.getKeyFrame(stateTime, true);
        currentFrame_slime = (TextureRegion) runAnimation_slime.getKeyFrame(stateTime, true);

        // Draws the character's currentframe on the screen, with a set position, and the size of the character.
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // Moves the character and camera until the end of the tiledMap.
        if (characterX <= 16400) {
            characterX += 5;
            camera.translate(5,0);
        } else if (characterX >= 17500){
            // Stops the character (out of screen)
//            backgroundMusic.stop();
            // Play winning music
            game.setScreen(MyGdxGame.winningScreen);

        } else {
            characterX += 5;
        }

        Gdx.app.log("State: ",String.valueOf(characterX));

        spriteBatch.draw(currentFrame, characterX, characterY, character_width, character_height);

        // Spawn single slimes
        if (characterX % 900 == 0 && (characterX <= 16400)) {
            slimeX = characterX + 1200;
        }
        slimeX -= 2;
        spriteBatch.draw(currentFrame_slime, slimeX, slimeY, character_width/2, character_height/2);

        spriteBatch.end();

        // Jump character
        if (Gdx.input.isTouched() && state == "run") {
            state = "jump";
            jumpStart = characterX + 15;
        }

        if (state == "jump" && characterX == jumpStart) {
            characterY += jumpHeight;
        }
        if (state == "jump" && characterX == jumpStart + 80) {
            characterY -= jumpHeight;
            state = "run";
        }
    }

    private void spawnEnemies() {
        if (characterX == 500) {

        }
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

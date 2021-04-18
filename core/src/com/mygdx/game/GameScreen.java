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
    private Sound jumpSound;
    private Sound deathSound;
    private Sound winningSound;
    SpriteBatch spriteBatch;                                // Spritebatch for rendering

    // Main character variables
    Texture runningSheet;                                   // Texture to hold the spritesheet
    TextureRegion[] runFrames;                              // Texture array for the running frames
    private static final int FRAME_COLS = 4;                // Number of columns of the running spritesheet
    private static final int FRAME_ROWS = 2;                // Number of rows of the running spritesheet
    private static final int character_height = 280;        // Height of the character
    private final int character_width = 210;                // Width of the character
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
    private final int jumpHeight = 140;

    Texture slimeSheet;
    TextureRegion[] slimeFrames;
    private static final int FRAME_COLS_SLIME = 2;                // Number of columns of the slime spritesheet
    private static final int FRAME_ROWS_SLIME = 1;                // Number of rows of the slime spritesheet
    Animation runAnimation_slime;		                            // Stores the array containing all of runFrames. It will also have the defined duration (in seconds) for each frame
    TextureRegion currentFrame_slime;
    private static int slimeX;                          // Slime's X position
    private static int slimeY = 410;                          // Slime's X position

    private static int jumpStart;

    // Jumping variables
    Texture jumpSheet;                                   // Texture to hold the spritesheet
    TextureRegion[] jumpFrames;                              // Texture array for the running frames
    private static final int FRAME_COLS_JUMP = 3;                // Number of columns of the running spritesheet
    private static final int FRAME_ROWS_JUMP = 1;                // Number of rows of the running spritesheet
    // Variables for the running animation
    Animation jumpAnimation;		                            // Stores the array containing all of runFrames. It will also have the defined duration (in seconds) for each frame

    // Landing variables
    Texture landSheet;                                   // Texture to hold the spritesheet
    TextureRegion[] landFrames;                              // Texture array for the running frames
    private static final int FRAME_COLS_LAND = 2;                // Number of columns of the running spritesheet
    private static final int FRAME_ROWS_LAND = 1;                // Number of rows of the running spritesheet
    // Variables for the running animation
    Animation landAnimation;		                            // Stores the array containing all of runFrames. It will also have the defined duration (in seconds) for each frame

    // Deading variables
    Texture deadSheet;                                   // Texture to hold the spritesheet
    TextureRegion[] deadFrames;                              // Texture array for the running frames
    private static final int FRAME_COLS_DEAD = 3;                // Number of columns of the running spritesheet
    private static final int FRAME_ROWS_DEAD = 1;                // Number of rows of the running spritesheet
    private static int deathPosition;

    // Retry button
    private Skin skin;
    private Stage stage;


    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    public void create() {

        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        stage = new Stage();

        // Start Game button
        final TextButton btn_retry = new TextButton("Retry", skin);
        btn_retry.setWidth(500f);
        btn_retry.setHeight(200f);
        btn_retry.setPosition(deathPosition + 600, characterY + 200);
        btn_retry.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                game.setScreen(MyGdxGame.menuScreen);
            }
        });
        stage.addActor(btn_retry);
        Gdx.input.setInputProcessor(stage);


        // Loaded the background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("bounce.mp3"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));
        winningSound = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));
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

        // Jump Animation -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        jumpSheet = new Texture(Gdx.files.internal("assets/jumping start.png"));
        temp = TextureRegion.split(jumpSheet, jumpSheet.getWidth()/FRAME_COLS_JUMP, jumpSheet.getHeight()/FRAME_ROWS_JUMP);
        jumpFrames = new TextureRegion[FRAME_ROWS_JUMP * FRAME_COLS_JUMP];
        index = 0;
        for (int i = 0; i < FRAME_ROWS_JUMP; i++) {
            for (int j = 0; j < FRAME_COLS_JUMP; j++) {
                jumpFrames[index++] = temp[i][j];    // i++ is post increment.
            }
        }
        jumpAnimation = new Animation(0.05f, jumpFrames);
        // Jump Animation -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Land Animation -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        landSheet = new Texture(Gdx.files.internal("assets/jumping end.png"));
        temp = TextureRegion.split(landSheet, landSheet.getWidth()/FRAME_COLS_LAND, landSheet.getHeight()/FRAME_ROWS_LAND);
        landFrames = new TextureRegion[FRAME_ROWS_LAND * FRAME_COLS_LAND];
        index = 0;
        for (int i = 0; i < FRAME_ROWS_LAND; i++) {
            for (int j = 0; j < FRAME_COLS_LAND; j++) {
                landFrames[index++] = temp[i][j];    // i++ is post increment.
            }
        }
        landAnimation = new Animation(0.05f, landFrames);
        // Land Animation -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Dead Animation -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        deadSheet = new Texture(Gdx.files.internal("assets/deading.png"));
        temp = TextureRegion.split(deadSheet, deadSheet.getWidth()/FRAME_COLS_DEAD, deadSheet.getHeight()/FRAME_ROWS_DEAD);
        deadFrames = new TextureRegion[FRAME_ROWS_DEAD * FRAME_COLS_DEAD];
        index = 0;
        for (int i = 0; i < FRAME_ROWS_DEAD; i++) {
            for (int j = 0; j < FRAME_COLS_DEAD; j++) {
                deadFrames[index++] = temp[i][j];    // i++ is post increment.
            }
        }
        // Dead Animation -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

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

    }

    private void newGame() {
        characterY = 410;
        // Starts background music
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
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

        // Moves the camera
        camera.update();
        // render tiledMap
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // Updates the stateTime using the deltaTime (to have the same time across all devices with different processors)
        stateTime += Gdx.graphics.getDeltaTime();

        // Gets the currentFrame of the running animation

        currentFrame_slime = (TextureRegion) runAnimation_slime.getKeyFrame(stateTime, true);

        if (state == "run") {
            currentFrame = (TextureRegion) runAnimation.getKeyFrame(stateTime, true);
        }



        // Jump character + animations
        if (Gdx.input.isTouched() && state == "run") {
            state = "jump";
            currentFrame = jumpFrames[0];
            jumpStart = characterX;
            jumpSound.play();
        }
        // before air
        if (state == "jump" && characterX == jumpStart + 15) {
            characterY += jumpHeight;
            currentFrame = jumpFrames[1];
        }
        // In air
        if (state == "jump" && characterX == jumpStart + 50) {
            currentFrame = jumpFrames[2];
            state = "land";
        }
        //15
        if (state == "land" && characterX == jumpStart + 100) {
            currentFrame = landFrames[0];
        }
        if (state == "land" && characterX == jumpStart + 150) {
            characterY -= jumpHeight;
            currentFrame = landFrames[1];
        }
        if (state == "land" && characterX == jumpStart + 200) {
            state = "run";
        }


        // Dead animation
        if (state == "dead" && characterX == deathPosition + 30) {
            currentFrame = deadFrames[1];
            characterY = 410;
        }

        // Dead animation
        if (state == "dead" && characterX == deathPosition + 60) {
            currentFrame = deadFrames[2];
            state = "really dead";
        }
        if (state == "really dead") {
            stage.draw();

            backgroundMusic.stop();
        }


        Gdx.app.log("Test - Character: ","X is: " + String.valueOf(characterX) + " X2 is: " + String.valueOf(characterX + character_width));
        Gdx.app.log("Test - Slime: ","X is: " + String.valueOf(slimeX) + " X2 is: " + String.valueOf(slimeX + 100));

        int character_back = characterX;
        int character_front = characterX + character_width;

        int slime_front = slimeX;
        int slime_back = slimeX + 100;

        int character_bottom = characterY;

        int slime_bottom = 410;
        int slime_height = slime_bottom + 90;

        if ((slimeX >0) &&
                // If the front(right side) of the character hits the slime
                (((character_front >= slime_front + 50) && (character_front <= slime_back + 50)) ||
                // If the back side of the character hits the slime
                ((character_back >= slime_front + 50) && (character_front <= slime_back + 50)) ) &&
                // If the bottom part of the character is anywhere within the area of the slime
                (character_bottom >= slime_bottom && character_bottom <= slime_height)) {
            state = "dead";
            deathPosition = characterX;
            currentFrame = deadFrames[0];
            deathSound.play();
        }



        // Draws the character's currentframe on the screen, with a set position, and the size of the character.
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // Moves the character and camera until the end of the tiledMap.
        if (characterX <= 16400 && state != "really dead") {
            characterX += 5;
            camera.translate(5,0);
        } else if (characterX >= 17500){
            // Stops the character (out of screen)
            backgroundMusic.stop();
            // Play winning music
//            state = "win";
            winningSound.play();
            game.setScreen(MyGdxGame.winningScreen);

        } else {
            if (state != "really dead") {
                characterX += 5;
            }
        }

        spriteBatch.draw(currentFrame, characterX, characterY, character_width, character_height);

        // Spawn single slimes
        if (characterX % 900 == 0 && (characterX <= 16400)) {
            slimeX = characterX + 1200;
        }
        slimeX -= 2;
        spriteBatch.draw(currentFrame_slime, slimeX, slimeY, 100, 100);
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

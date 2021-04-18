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

/**
 * This class implements the GameScreen, which renders the entire game.
 */
public class GameScreen implements Screen {
    MyGdxGame game;
    private Music backgroundMusic;                          // Background music while playing the game.
    private Sound jumpSound;                                // Sound played when the character jumps.
    private Sound deathSound;                               // Sound played when the character dies.
    private Sound winningSound;                             // Sound played when the character wins.
    private Sound slideSound;                               // Sound played when the character slides.
    SpriteBatch spriteBatch;                                // Spritebatch for rendering

    // Main character variables.
    Texture runningSheet;                                   // Texture to hold the spritesheet.
    TextureRegion[] runFrames;                              // Texture array for the running frames.
    private static final int FRAME_COLS = 4;                // Number of columns of the running spritesheet.
    private static final int FRAME_ROWS = 2;                // Number of rows of the running spritesheet.
    private static int character_height = 280;              // Height of the character.
    private final int character_width = 210;                // Width of the character.
    private static int characterX;                          // Character's X position.
    private static int characterY;                          // Character's Y position.
    private final int jumpHeight = 140;                     // The pixels added when the character jumps.
    private final int slideHeight = 70;                     // The height deducted when the character slides.

    // Variables for the character running animation.
    Animation runAnimation;		                            // Stores the array containing all of runFrames. It will also have the defined duration (in seconds) for each frame.
    TextureRegion currentFrame;                             // Current frame to display.
    float stateTime;                                        // The time the program has been running.

    // Variables for rendering the tiledMap.
    private TiledMap tiledMap;                              // Loads the tiledMap.
    private OrthogonalTiledMapRenderer tiledMapRenderer;    // Renders the tiledMap.
    private OrthographicCamera camera;                      // Camera to show a specific portion of the world to the player.

    private static String state;                            // The state of the character int the game.
    private static int jumpStart;                           // Variable to determine when the character will land based on the time it jumped.
    private static int slideStart;                          // Variable to determine when the character will rise based on the time it slides.
    private static int deathPosition;                       // Variable to determine the final position of the character when it dies.

    // Variables for displaying the slime enemy.
    Texture slimeSheet;                                     // Texture to hold the spritesheet.
    TextureRegion[] slimeFrames;                            // Texture array for the slime frames.
    private static final int FRAME_COLS_SLIME = 2;          // Number of columns of the slime spritesheet.
    private static final int FRAME_ROWS_SLIME = 1;          // Number of rows of the slime spritesheet.
    Animation runAnimation_slime;		                    // Stores the array containing all of slimeFrames.
    TextureRegion currentFrame_slime;                       // Current frame of the slime to display.
    private static int slimeX;                              // Slime's X position.
    private static int slimeY = 410;                        // Slime's Y position.

    // Variables for displaying the bees enemy.
    Texture beesSheet;                                      // Texture to hold the spritesheet.
    TextureRegion[] beesFrames;                             // Texture array for the bees frames.
    private static final int FRAME_COLS_BEES = 2;           // Number of columns of the bees spritesheet.
    private static final int FRAME_ROWS_BEES = 1;           // Number of rows of the bees spritesheet.
    Animation runAnimation_bees;		                    // Stores the array containing all of beesFrames.
    TextureRegion currentFrame_bees;                        // Current frame of the bee to display.
    private static int beesX;                               // Bee's X position.
    private static int beesY = 500;                         // Bee's X position.

    // Variables for jumping.
    Texture jumpSheet;                                      // Texture to hold the spritesheet.
    TextureRegion[] jumpFrames;                             // Texture array for the jumping frames.
    private static final int FRAME_COLS_JUMP = 3;           // Number of columns of the jumping spritesheet.
    private static final int FRAME_ROWS_JUMP = 1;           // Number of rows of the jumping spritesheet.
    Animation jumpAnimation;		                        // Stores the array containing all of jumoFrames.

    // Variables for landing after jumping.
    Texture landSheet;                                      // Texture to hold the spritesheet.
    TextureRegion[] landFrames;                             // Texture array for the landing frames.
    private static final int FRAME_COLS_LAND = 2;           // Number of columns of the landing spritesheet.
    private static final int FRAME_ROWS_LAND = 1;           // Number of rows of the landing spritesheet.

    // Variables for sliding.
    Texture slideSheet;                                     // Texture to hold the spritesheet.
    TextureRegion[] slideFrames;                            // Texture array for the sliding frames.
    private static final int FRAME_COLS_SLIDE = 2;          // Number of columns of the sliding spritesheet.
    private static final int FRAME_ROWS_SLIDE = 1;          // Number of rows of the sliding spritesheet.

    // Variables for rising after sliding.
    Texture riseSheet;                                      // Texture to hold the spritesheet.
    TextureRegion[] riseFrames;                             // Texture array for the rising frames.
    private static final int FRAME_COLS_RISE = 1;           // Number of columns of the rising spritesheet.
    private static final int FRAME_ROWS_RISE = 1;           // Number of rows of the rising spritesheet.

    // Variables when the character dies.
    Texture deadSheet;                                      // Texture to hold the spritesheet.
    TextureRegion[] deadFrames;                             // Texture array for the dead frames.
    private static final int FRAME_COLS_DEAD = 3;           // Number of columns of the dead spritesheet.
    private static final int FRAME_ROWS_DEAD = 1;           // Number of rows of the dead spritesheet.

    // Variables for the Retry game button.
    private Skin skin;                                      // The skin/style of the buttons.
    private Stage stage;                                    // This will hold the button Actors.
    private TextButton btn_retry;                           // The button shown when the character dies or wins to play again.

    /**
     * Constructor of the GameScreen.
     */
    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    /**
     * This method is called initially when the GameScreen starts to initialise the variables.
     */
    public void create() {

        // Loads the background music and sound effects.
        loadMusic();

        // Loads the Main Character. ----------------------------------------------------------------
        runningSheet = new Texture(Gdx.files.internal("assets/running.png"));

        // Creates a 2D array of the given spritesheet
        TextureRegion[][] temp = TextureRegion.split(runningSheet, runningSheet.getWidth() / FRAME_COLS, runningSheet.getHeight() / FRAME_ROWS);

        // Sets the size of the array for the above code to only be a 1D array
        runFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];

        // Transfers each texture on the temp 2D array to the 1D runFrames array.
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                runFrames[index++] = temp[i][j];
            }
        }

        // Sets the runFrames TextureTesgion into an Animation object, with a framerate set to 0.033, which is 30 frames per second.
        runAnimation = new Animation(0.033f, runFrames);

        // Loads the slime enemy. The process is similar to the Main Character. --------------------
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

        // Loads the bees enemy. The process is similar to the Main Character. ---------------------
        beesSheet = new Texture(Gdx.files.internal("assets/bee.png"));
        temp = TextureRegion.split(beesSheet, beesSheet.getWidth()/FRAME_COLS_BEES, beesSheet.getHeight()/FRAME_ROWS_BEES);
        beesFrames = new TextureRegion[FRAME_COLS_BEES * FRAME_ROWS_BEES];
        index = 0;
        for (int i = 0; i < FRAME_ROWS_BEES; i++) {
            for (int j = 0; j < FRAME_COLS_BEES; j++) {
                beesFrames[index++] = temp[i][j];    // i++ is post increment.
            }
        }
        runAnimation_bees = new Animation(0.033f, beesFrames);

        // Loads the slide animation. --------------------------------------------------------------
        slideSheet = new Texture(Gdx.files.internal("assets/sliding start.png"));
        temp = TextureRegion.split(slideSheet, slideSheet.getWidth()/FRAME_COLS_SLIDE, slideSheet.getHeight()/FRAME_ROWS_SLIDE);
        slideFrames = new TextureRegion[FRAME_ROWS_SLIDE * FRAME_COLS_SLIDE];
        index = 0;
        for (int i = 0; i < FRAME_ROWS_SLIDE; i++) {
            for (int j = 0; j < FRAME_COLS_SLIDE; j++) {
                slideFrames[index++] = temp[i][j];    // i++ is post increment.
            }
        }

        // Loads the rising animation. -------------------------------------------------------------
        riseSheet = new Texture(Gdx.files.internal("assets/sliding end.png"));
        temp = TextureRegion.split(riseSheet, riseSheet.getWidth()/FRAME_COLS_RISE, riseSheet.getHeight()/FRAME_ROWS_RISE);
        riseFrames = new TextureRegion[FRAME_ROWS_RISE * FRAME_COLS_RISE];
        index = 0;
        for (int i = 0; i < FRAME_ROWS_RISE; i++) {
            for (int j = 0; j < FRAME_COLS_RISE; j++) {
                riseFrames[index++] = temp[i][j];    // i++ is post increment.
            }
        }

        // Loads the jumping animation. ------------------------------------------------------------
        jumpSheet = new Texture(Gdx.files.internal("assets/jumping start.png"));
        temp = TextureRegion.split(jumpSheet, jumpSheet.getWidth()/FRAME_COLS_JUMP, jumpSheet.getHeight()/FRAME_ROWS_JUMP);
        jumpFrames = new TextureRegion[FRAME_ROWS_JUMP * FRAME_COLS_JUMP];
        index = 0;
        for (int i = 0; i < FRAME_ROWS_JUMP; i++) {
            for (int j = 0; j < FRAME_COLS_JUMP; j++) {
                jumpFrames[index++] = temp[i][j];    // i++ is post increment.
            }
        }

        // Loads the landing animation. ------------------------------------------------------------
        landSheet = new Texture(Gdx.files.internal("assets/jumping end.png"));
        temp = TextureRegion.split(landSheet, landSheet.getWidth()/FRAME_COLS_LAND, landSheet.getHeight()/FRAME_ROWS_LAND);
        landFrames = new TextureRegion[FRAME_ROWS_LAND * FRAME_COLS_LAND];
        index = 0;
        for (int i = 0; i < FRAME_ROWS_LAND; i++) {
            for (int j = 0; j < FRAME_COLS_LAND; j++) {
                landFrames[index++] = temp[i][j];    // i++ is post increment.
            }
        }

        // Loads the dying animation. --------------------------------------------------------------
        deadSheet = new Texture(Gdx.files.internal("assets/deading.png"));
        temp = TextureRegion.split(deadSheet, deadSheet.getWidth()/FRAME_COLS_DEAD, deadSheet.getHeight()/FRAME_ROWS_DEAD);
        deadFrames = new TextureRegion[FRAME_ROWS_DEAD * FRAME_COLS_DEAD];
        index = 0;
        for (int i = 0; i < FRAME_ROWS_DEAD; i++) {
            for (int j = 0; j < FRAME_COLS_DEAD; j++) {
                deadFrames[index++] = temp[i][j];    // i++ is post increment.
            }
        }

        // Loads the tiledMap. ---------------------------------------------------------------------
        tiledMap = new TmxMapLoader().load("assets/level1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Sets the camera. ------------------------------------------------------------------------
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        camera.position.set(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/2+100,0);


        // Sets the Retry button. ------------------------------------------------------------------
        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        stage = new Stage();
        btn_retry = new TextButton("Try again?", skin);
        btn_retry.setWidth(500f);
        btn_retry.setHeight(200f);
        btn_retry.setPosition(camera.position.x, camera.position.y);
        btn_retry.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                backgroundMusic.stop();
                game.setScreen(MyGdxGame.gameScreen);
            }
        });
        stage.addActor(btn_retry);
        Gdx.input.setInputProcessor(stage);

        // Initialises the stateTime.
        stateTime = 0.0f;

        // initialises the spriteBatch for rendering.
        spriteBatch = new SpriteBatch();

        // Sets the initial position and state of the character.
        characterX = 0;
        characterY = 410;
        state = "run";

        // Starts the background music.
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    /**
     *  This method loads the background music and sound effects.
     */
    private void loadMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("bounce.mp3"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));
        winningSound = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));
        slideSound = Gdx.audio.newSound(Gdx.files.internal("slide.mp3"));
    }

    /**
     * This method calls the create() method after being called from a different page/menu.
     */
    @Override
    public void show() {
        Gdx.app.log("GameScreen: ","gameScreen show called");
        create();
    }

    /**
     * The render method loops continuously, and is called after the create() method.
     */
    @Override
    public void render(float delta) {

        // Clears the screen and sets a sky blue background.
        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Moves the camera.
        camera.update();

        // render tiledMap.
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // Updates the stateTime using the deltaTime (to have the same time across all devices with different processors).
        stateTime += Gdx.graphics.getDeltaTime();

        // Gets the currentFrame of the running animation of the enemies.
        currentFrame_slime = (TextureRegion) runAnimation_slime.getKeyFrame(stateTime, true);
        currentFrame_bees = (TextureRegion) runAnimation_bees.getKeyFrame(stateTime, true);

        // The currentFrame of the Mian Character is set to run, only if the state is "run".
        if (state == "run") {
            currentFrame = (TextureRegion) runAnimation.getKeyFrame(stateTime, true);
        }

        // Determines if the character will jump or slide based on the click location.
        int click_location = Gdx.input.getY();

        // If the lower part of the screen is clicked, slide. If the upper half, then it jumps. Can only do so if it's running.
        if (click_location > 450) {
            if (Gdx.input.isTouched() && state == "run") {
                state = "slide";
                currentFrame = slideFrames[0];
                slideStart = characterX;
                slideSound.play();
            }
        } else {
            if (Gdx.input.isTouched() && state == "run") {
                state = "jump";
                currentFrame = jumpFrames[0];
                jumpStart = characterX;
                jumpSound.play();
            }
        }

        // These are the slide animations based on the state. The numbers are also adjusted to delay the animation to show more of each currentFrame.
        if (state == "slide" && characterX == slideStart + 15) {
            // if the character slides, lessen the height to avoid obstacles from above.
            character_height -= slideHeight;
            currentFrame = slideFrames[1];
        }

        // After sliding, rise.
        if (state == "slide" && characterX == slideStart + 50) {
            state = "rise";
        }

        // Increases the height back to normal. Returns the state to "run".
        if (state == "rise" && characterX == slideStart + 150) {
            currentFrame = riseFrames[0];
            character_height += slideHeight;
            state = "run";
        }

        // These are the jump animations based on the state. The numbers are also adjusted to delay the animation to show more of each currentFrame.
        if (state == "jump" && characterX == jumpStart + 15) {
            characterY += jumpHeight;
            currentFrame = jumpFrames[1];
        }

        // While in the air.
        if (state == "jump" && characterX == jumpStart + 50) {
            currentFrame = jumpFrames[2];
            state = "land";
        }

        // About to land.
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

        // These are the dead animations.
        if (state == "dead" && characterX == deathPosition + 30) {
            currentFrame = deadFrames[1];
            characterY = 410;
        }
        if (state == "dead" && characterX == deathPosition + 60) {
            currentFrame = deadFrames[2];
            state = "really dead";
        }
        // Draws the retry button on the screen if the character dies.
        if (state == "really dead") {
            stage.draw();
        }

        // If the character wins, draws the retry button, but instead saying "Play again?".
        if (state == "win") {
            btn_retry.setText("Congratulations! Play again?");
            btn_retry.setWidth(900f);
            stage.draw();
        }

//        Gdx.app.log("Test - Character: ","X is: " + String.valueOf(characterX) + " X2 is: " + String.valueOf(characterX + character_width));
//        Gdx.app.log("Test - Slime: ","X is: " + String.valueOf(slimeX) + " X2 is: " + String.valueOf(slimeX + 100));
//        Gdx.app.log("Camera", String.valueOf(camera.position.x));

        // Collision detection. This records the character's area/sides, and the enemies sides, then determines if each point is in the range of the other.
        int character_back = characterX;
        int character_front = characterX + character_width;
        int character_bottom = characterY;

        int slime_front = slimeX;
        int slime_back = slimeX + 100;
        int slime_bottom = 410;
        int slime_height = slime_bottom + 90;

        if ((slimeX >0) &&
                // If the front(right side) of the character hits the slime
                (((character_front >= slime_front + 50) && (character_front <= slime_back + 50)) ||
                // If the back side of the character hits the slime
                ((character_back >= slime_front + 50) && (character_front <= slime_back + 50)) ) &&
                // If the bottom part of the character is anywhere within the area of the slime
                (character_bottom >= slime_bottom && character_bottom <= slime_height)) {
            // The character dies if it hits the slime.
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
        }
        // If the character finishes the game, stops the camera movement and draws the button to play again.
        else if (characterX >= 17500 && characterX <= 17600){
            winningSound.play();
            state = "win";
            characterX = 17601;
            stage.draw();
        }
        // This stops the camera from moving when the character reaches a certain point in the map.
        else {
            if (state != "really dead") {
                characterX += 5;
            }
        }

        // Draws the main Character based on its state.
        spriteBatch.draw(currentFrame, characterX, characterY, character_width, character_height);

        // Spawn single slimes based on the character's position.
        if (characterX % 900 == 0 && (characterX <= 16400)) {
            slimeX = characterX + 1200;
        }
        // Moves the slimes slowly to the character.
        slimeX -= 2;
        spriteBatch.draw(currentFrame_slime, slimeX, slimeY, 100, 100);

        // Spawn single bees based on the player's position.
        if (characterX % 1000 == 0 && (characterX <= 16400)) {
            beesX = characterX + 1200;
        }
        // Moves the bees to the character.
        beesX -= 2;
        spriteBatch.draw(currentFrame_bees, beesX, beesY, 100, 100);

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

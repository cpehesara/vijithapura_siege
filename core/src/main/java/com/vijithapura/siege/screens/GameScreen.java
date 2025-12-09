package com.vijithapura.siege.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.VijithapuraGame;
import com.vijithapura.siege.effects.VisualEffectsManager;
import com.vijithapura.siege.entities.Building;
import com.vijithapura.siege.entities.Gate;
import com.vijithapura.siege.entities.Notification;
import com.vijithapura.siege.entities.ResourceNode;
import com.vijithapura.siege.entities.Unit;
import com.vijithapura.siege.systems.BuildingManager;
import com.vijithapura.siege.systems.CameraController;
import com.vijithapura.siege.systems.CombatSystem;
import com.vijithapura.siege.systems.CommandExecutor;
import com.vijithapura.siege.systems.ResourceManager;
import com.vijithapura.siege.systems.SelectionManager;
import com.vijithapura.siege.systems.UnitManager;
import com.vijithapura.siege.systems.WaveManager;
import com.vijithapura.siege.systems.WorldRenderer;
import com.vijithapura.siege.ui.DetailPanel;
import com.vijithapura.siege.ui.MinimapRenderer;
import com.vijithapura.siege.ui.UIRenderer;
import com.vijithapura.siege.utils.Constants;
import com.vijithapura.siege.utils.InputHandler;

/**
 * REFACTORED GameScreen - Much cleaner and organized
 * Delegates responsibilities to specialized managers
 */
public class GameScreen implements Screen {
    private VijithapuraGame game;
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private InputHandler inputHandler;

    // ===== MANAGERS (Delegation Pattern) =====
    private ResourceManager resourceManager;
    private UnitManager unitManager;
    private BuildingManager buildingManager;
    private WaveManager waveManager;
    private SelectionManager selectionManager;
    private WorldRenderer worldRenderer;
    private UIRenderer uiRenderer;
    private MinimapRenderer minimapRenderer;
    private VisualEffectsManager effectsManager;
    private DetailPanel detailPanel;

    // ===== GAME STATE =====
    private float gameTime;
    private boolean gameWon;
    private boolean gameLost;
    private boolean isPaused;
    private GameDifficulty difficulty;
    private ArrayList<Notification> notifications;

    public enum GameDifficulty {
        EASY(0.7f, 0.8f, 1.5f),
        NORMAL(1.0f, 1.0f, 1.0f),
        HARD(1.3f, 1.2f, 0.7f);

        public final float enemyDamageMultiplier;
        public final float enemyHealthMultiplier;
        public final float resourceMultiplier;

        GameDifficulty(float dmg, float hp, float res) {
            this.enemyDamageMultiplier = dmg;
            this.enemyHealthMultiplier = hp;
            this.resourceMultiplier = res;
        }
    }

    public GameScreen(VijithapuraGame game) {
        this(game, GameDifficulty.NORMAL);
    }

    public GameScreen(VijithapuraGame game, GameDifficulty difficulty) {
        this.game = game;
        this.difficulty = difficulty;

        initializeCameras();
        initializeManagers();
        initializeInput();
        initializeGame();

        Gdx.app.log("GameScreen", "✓ Game initialized - Difficulty: " + difficulty);
    }

    private void initializeCameras() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        camera.position.set(400, 400, 0);
        camera.zoom = 0.4f;
        camera.update();

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }

    private void initializeManagers() {
        // Resource management
        resourceManager = new ResourceManager(
            (int)(Constants.STARTING_GOLD * difficulty.resourceMultiplier),
            (int)(Constants.STARTING_WOOD * difficulty.resourceMultiplier),
            (int)(Constants.STARTING_STONE * difficulty.resourceMultiplier),
            (int)(Constants.STARTING_FOOD * difficulty.resourceMultiplier)
        );

        // Unit management
        unitManager = new UnitManager();

        // Building management
        buildingManager = new BuildingManager();

        // Wave system
        waveManager = new WaveManager(difficulty);

        // Selection system
        selectionManager = new SelectionManager();

        // Rendering systems
        worldRenderer = new WorldRenderer();
        uiRenderer = new UIRenderer(game.font);
        minimapRenderer = new MinimapRenderer(20, 20, 180, 180);
        effectsManager = new VisualEffectsManager();
        effectsManager.setFont(game.font);
        detailPanel = new DetailPanel();

        // Notifications
        notifications = new ArrayList<>();
    }

    private void initializeInput() {
        inputHandler = new InputHandler(camera);
        inputHandler.setClickListener(new InputHandler.ClickListener() {
            @Override
            public void onLeftClick(float worldX, float worldY) {
                if (isClickInUI()) return;
                handleLeftClick(worldX, worldY);
            }

            @Override
            public void onRightClick(float worldX, float worldY) {
                if (isClickInUI()) return;
                handleRightClick(worldX, worldY);
            }
        });
        Gdx.input.setInputProcessor(inputHandler);
    }

    private void initializeGame() {
        buildingManager.initializeStartingBuildings();
        unitManager.spawnStartingUnits();
        waveManager.startFirstWave(unitManager);

        addNotification("Welcome to the Siege of Vijithapura!", Color.CYAN);
        addNotification("Difficulty: " + difficulty.name(), Color.YELLOW);
        addNotification("Destroy all gates to win!", Color.GREEN);
    }

    @Override
    public void render(float delta) {
        if (!isPaused) {
            gameTime += delta;
            update(delta);
        }

        camera.update();

        Gdx.gl.glClearColor(0.15f, 0.25f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render game world
        worldRenderer.render(game, camera,
            unitManager.getPlayerUnits(),
            unitManager.getEnemyUnits(),
            buildingManager.getBuildings(),
            buildingManager.getGates(),
            buildingManager.getResourceNodes(),
            unitManager.getProjectiles(),
            selectionManager.isSelecting(),
            selectionManager.getSelectionStart(),
            selectionManager.getSelectionEnd()
        );

        // Render effects
        effectsManager.render(game.shapeRenderer, game.batch);

        // Render UI
        renderUI();
    }

    private void update(float delta) {
        if (gameWon || gameLost) return;

        // Update all managers
        resourceManager.update(delta, unitManager.getPlayerUnits(),
            buildingManager.getResourceNodes());

        unitManager.update(delta, buildingManager.getGates(), effectsManager);
        buildingManager.update(delta);
        waveManager.update(delta, unitManager, resourceManager, notifications);
        selectionManager.update(delta);

        uiRenderer.update(delta);
        minimapRenderer.update(delta);
        effectsManager.update(delta);
        updateNotifications(delta);

        handleInput(delta);
        checkVictoryDefeat();
    }

    private void handleInput(float delta) {
        // Camera movement
        CameraController.handleCameraMovement(camera, delta);

        // Hotkeys
        handleHotkeys();

        // Selection box
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !isClickInUI()) {
            if (!selectionManager.isSelecting()) {
                Vector2 worldPos = inputHandler.screenToWorld(Gdx.input.getX(), Gdx.input.getY());
                selectionManager.startSelection(worldPos);
            } else {
                Vector2 worldPos = inputHandler.screenToWorld(Gdx.input.getX(), Gdx.input.getY());
                selectionManager.updateSelection(worldPos);
            }
        } else if (selectionManager.isSelecting()) {
            selectionManager.finishSelection(unitManager.getPlayerUnits(), effectsManager);
        }

        // UI clicks
        if (Gdx.input.justTouched() && !isClickInUI()) {
            handleUIButtonClicks();
        }
    }

    private void handleHotkeys() {
        // Close detail panel with ESC
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            detailPanel.hide();
        }
        
        // Unit training
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) trainWorker();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) trainSoldier();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) trainArcher();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) trainCavalry();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) trainChampion();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) trainRam();

        // Commands
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q))
            CommandExecutor.attackNearestGate(selectionManager.getSelectedUnits(),
                buildingManager.getGates(), notifications);
        if (Gdx.input.isKeyJustPressed(Input.Keys.E))
            CommandExecutor.stopUnits(selectionManager.getSelectedUnits(), notifications);
        if (Gdx.input.isKeyJustPressed(Input.Keys.H))
            CommandExecutor.holdPosition(selectionManager.getSelectedUnits(), notifications);
        if (Gdx.input.isKeyJustPressed(Input.Keys.F))
            CommandExecutor.formationMove(selectionManager.getSelectedUnits(), notifications);

        // Pause
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
            addNotification(isPaused ? "PAUSED" : "RESUMED", Color.YELLOW);
        }
    }

    private void handleLeftClick(float worldX, float worldY) {
        // Check if detail panel consumed the click
        int screenX = Gdx.input.getX();
        int screenY = Constants.SCREEN_HEIGHT - Gdx.input.getY();
        if (detailPanel.handleClick(screenX, screenY)) {
            return;
        }
        
        // Check if clicking on any entity to show details
        Object clickedEntity = getEntityAtPosition(worldX, worldY);
        if (clickedEntity != null && Gdx.input.justTouched()) {
            detailPanel.show(clickedEntity, screenX, screenY);
        }
        
        selectionManager.handleLeftClick(worldX, worldY,
            unitManager.getPlayerUnits(),
            buildingManager.getBuildings(),
            effectsManager,
            notifications
        );
    }

    private void handleRightClick(float worldX, float worldY) {
        selectionManager.handleRightClick(worldX, worldY,
            unitManager.getEnemyUnits(),
            buildingManager.getGates(),
            buildingManager.getResourceNodes(),
            notifications
        );
    }

    private void handleUIButtonClicks() {
        int mouseX = Gdx.input.getX();
        int mouseY = Constants.SCREEN_HEIGHT - Gdx.input.getY();

        if (mouseY > 200) return; // Not in bottom panel

        // Check button regions (simplified)
        if (isInRect(mouseX, mouseY, 250, 140, 120, 30)) trainWorker();
        else if (isInRect(mouseX, mouseY, 390, 140, 120, 30)) trainSoldier();
        else if (isInRect(mouseX, mouseY, 530, 140, 120, 30)) trainArcher();
        else if (isInRect(mouseX, mouseY, 250, 95, 120, 30)) trainCavalry();
        else if (isInRect(mouseX, mouseY, 390, 95, 120, 30)) trainChampion();
        else if (isInRect(mouseX, mouseY, 530, 95, 120, 30)) trainRam();
    }

    private boolean isInRect(int x, int y, float rx, float ry, float w, float h) {
        return x >= rx && x <= rx + w && y >= ry && y <= ry + h;
    }

    private boolean isClickInUI() {
        int mouseY = Gdx.input.getY();
        return mouseY < 50 || mouseY > Constants.SCREEN_HEIGHT - 200;
    }

    // ===== UNIT TRAINING =====

    private void trainWorker() {
        trainUnit(Unit.UnitType.WORKER,
            Constants.WORKER_COST_GOLD, Constants.WORKER_COST_WOOD, 0, Constants.WORKER_COST_FOOD);
    }

    private void trainSoldier() {
        trainUnit(Unit.UnitType.SOLDIER,
            Constants.SOLDIER_COST_GOLD, Constants.SOLDIER_COST_WOOD, 0, Constants.SOLDIER_COST_FOOD);
    }

    private void trainArcher() {
        trainUnit(Unit.UnitType.ARCHER,
            Constants.ARCHER_COST_GOLD, Constants.ARCHER_COST_WOOD, 0, Constants.ARCHER_COST_FOOD);
    }

    private void trainCavalry() {
        trainUnit(Unit.UnitType.CAVALRY,
            Constants.CAVALRY_COST_GOLD, 0, 0, Constants.CAVALRY_COST_FOOD);
    }

    private void trainChampion() {
        trainUnit(Unit.UnitType.CHAMPION,
            Constants.CHAMPION_COST_GOLD, Constants.CHAMPION_COST_WOOD, 0, Constants.CHAMPION_COST_FOOD);
    }

    private void trainRam() {
        trainUnit(Unit.UnitType.SIEGE_RAM,
            Constants.RAM_COST_GOLD, Constants.RAM_COST_WOOD, 0, 0);
    }

    private void trainUnit(Unit.UnitType type, int gold, int wood, int stone, int food) {
        if (!resourceManager.canAfford(gold, wood, stone, food)) {
            addNotification("Insufficient resources!", Color.RED);
            return;
        }

        resourceManager.spend(gold, wood, stone, food);
        unitManager.queueUnit(type, buildingManager.getSpawnPoint());
        addNotification(type.name() + " training started!", Color.GREEN);
    }

    private void renderUI() {
        uiCamera.update();
        game.batch.setProjectionMatrix(uiCamera.combined);
        game.shapeRenderer.setProjectionMatrix(uiCamera.combined);

        // Prepare UI data
        UIRenderer.GameUIData uiData = new UIRenderer.GameUIData();
        uiData.gold = resourceManager.getGold();
        uiData.wood = resourceManager.getWood();
        uiData.stone = resourceManager.getStone();
        uiData.food = resourceManager.getFood();
        uiData.currentPop = unitManager.getPlayerUnits().size();
        uiData.maxPop = Constants.MAX_POPULATION;
        uiData.gameTime = gameTime;
        uiData.currentWave = waveManager.getCurrentWave();
        uiData.waveInProgress = waveManager.isWaveInProgress();
        uiData.timeToNextWave = waveManager.getTimeToNextWave();
        uiData.selectedUnits = selectionManager.getSelectedUnits();
        uiData.notifications = notifications;

        // Render UI
        uiRenderer.renderGameUI(game.batch, game.shapeRenderer, uiData);

        // Render minimap
        minimapRenderer.render(
            game.shapeRenderer, game.batch, camera,
            unitManager.getPlayerUnits(),
            unitManager.getEnemyUnits(),
            buildingManager.getGates(),
            buildingManager.getBuildings(),
            buildingManager.getResourceNodes()
        );

        // Game over screen
        if (gameWon || gameLost) {
            renderGameOverScreen();
        }
        
        // Render detail panel on top of everything
        detailPanel.render(game.shapeRenderer, game.batch, game.font);
    }

    private void renderGameOverScreen() {
        game.batch.begin();
        game.font.getData().setScale(3f);
        game.font.setColor(gameWon ? Color.GREEN : Color.RED);

        String text = gameWon ? "VICTORY!" : "DEFEAT!";
        float x = Constants.SCREEN_WIDTH / 2 - 150;
        float y = Constants.SCREEN_HEIGHT / 2;

        game.font.draw(game.batch, text, x, y);

        game.font.getData().setScale(1.2f);
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Click to return to menu", x - 50, y - 50);

        game.batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new MenuScreen(game));
            dispose();
        }
    }

    private void checkVictoryDefeat() {
        if (!gameWon && CombatSystem.checkVictory(buildingManager.getGates())) {
            gameWon = true;
            addNotification("VICTORY! Fortress conquered!", Color.GREEN);
        }

        if (!gameLost && CombatSystem.checkDefeat(unitManager.getPlayerUnits())) {
            gameLost = true;
            addNotification("DEFEAT! Army destroyed!", Color.RED);
        }
    }
    
    private Object getEntityAtPosition(float worldX, float worldY) {
        // Check units (closest to click)
        float minDist = 50; // Click radius
        Unit closestUnit = null;
        
        for (Unit unit : unitManager.getPlayerUnits()) {
            float dist = unit.getPosition().dst(worldX, worldY);
            if (dist < minDist) {
                minDist = dist;
                closestUnit = unit;
            }
        }
        
        for (Unit unit : unitManager.getEnemyUnits()) {
            float dist = unit.getPosition().dst(worldX, worldY);
            if (dist < minDist) {
                minDist = dist;
                closestUnit = unit;
            }
        }
        
        if (closestUnit != null) return closestUnit;
        
        // Check buildings
        for (Building building : buildingManager.getBuildings()) {
            if (building.getBounds().contains(worldX, worldY)) {
                return building;
            }
        }
        
        // Check gates
        for (Gate gate : buildingManager.getGates()) {
            if (gate.getBounds().contains(worldX, worldY)) {
                return gate;
            }
        }
        
        // Check resources
        for (ResourceNode node : buildingManager.getResourceNodes()) {
            float dist = node.getPosition().dst(worldX, worldY);
            if (dist < 40) { // Resource click radius
                return node;
            }
        }
        
        return null;
    }

    private void updateNotifications(float delta) {
        notifications.removeIf(n -> {
            n.update(delta);
            return n.isExpired();
        });
    }

    private void addNotification(String message, Color color) {
        notifications.add(new Notification(message, color));
        if (notifications.size() > 5) notifications.remove(0);
        Gdx.app.log("Notification", message);
    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
        uiCamera.viewportWidth = width;
        uiCamera.viewportHeight = height;
        uiCamera.update();
    }

    @Override
    public void pause() { isPaused = true; }

    @Override
    public void resume() { isPaused = false; }

    @Override
    public void hide() { Gdx.input.setInputProcessor(null); }

    @Override
    public void dispose() {
        Gdx.app.log("GameScreen", "Disposing game screen");
    }

    // Getters for legacy compatibility
    public ArrayList<Unit> getPlayerUnits() { return unitManager.getPlayerUnits(); }
    public ArrayList<Unit> getEnemyUnits() { return unitManager.getEnemyUnits(); }
    public int getCurrentWave() { return waveManager.getCurrentWave(); }
    public float getGameTime() { return gameTime; }
    public ArrayList<Notification> getNotifications() { return notifications; }
    public ArrayList<Unit> getSelectedUnits() { return selectionManager.getSelectedUnits(); }
    public boolean isWaveInProgress() { return waveManager.isWaveInProgress(); }
    
    // Resource getters
    public int getPlayerGold() { return resourceManager.getGold(); }
    public int getPlayerWood() { return resourceManager.getWood(); }
    public int getPlayerStone() { return resourceManager.getStone(); }
    public int getPlayerFood() { return resourceManager.getFood(); }
    
    // Population getters
    public int getCurrentPopulation() { 
        return (int) unitManager.getPlayerUnits().stream().filter(Unit::isAlive).count();
    }
    public int getMaxPopulation() { return Constants.MAX_POPULATION; }
    
    // Wave getters
    public int getWavesDefeated() { 
        return waveManager.isWaveInProgress() ? waveManager.getCurrentWave() - 1 : waveManager.getCurrentWave();
    }
}

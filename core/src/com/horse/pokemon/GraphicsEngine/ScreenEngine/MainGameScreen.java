package com.horse.pokemon.GraphicsEngine.ScreenEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.horse.pokemon.Audio.AudioData;
import com.horse.pokemon.Engine;
import com.horse.pokemon.GraphicsEngine.MainInterface.DialogEngine.Dialog;
import com.horse.pokemon.GraphicsEngine.MainInterface.DialogEngine.TextSpeeds;
import com.horse.pokemon.ObjectData.Players.User;
import com.horse.pokemon.ObjectData.TiledObjects.Door;

import java.util.ArrayList;
import java.util.HashMap;

public class MainGameScreen implements Screen {
    private Engine                            engine;
    private OrthographicCamera                camera;
    private Viewport                          viewport;
    private Hud                               hud;
    private TmxMapLoader                      mapLoader;
    private TiledMap                          map;
    private MultiTileMapRenderer              renderer;
    private User                              user;
    private Stage                             stage;
    private AudioData                         sound;
    private Dialog                            dialog;
    private FPSLogger                         fpsLogger;
    private MapCreator                        mapCreator;
    private ArrayList<TiledMapTileLayer.Cell> doorsInMap;
    private HashMap<Integer, TiledMapTile>    doorTiles;
    private Door                              doorToOpen;
    private int                               framesToAnimateDoor;
    private int                               currentDoorFrameCount;
    
    public MainGameScreen(Engine engine) {
        setEngine(engine);
        fpsLogger = new FPSLogger();
    }
    
    public int getCurrentDoorFrameCount() {
        return currentDoorFrameCount;
    }
    
    public void setCurrentDoorFrameCount(int currentDoorFrameCount) {
        this.currentDoorFrameCount = currentDoorFrameCount;
    }
    
    public Door getDoorToOpen() {
        return doorToOpen;
    }
    
    public void setDoorToOpen(Door doorToOpen) {
        this.doorToOpen = doorToOpen;
    }
    
    public int getFramesToAnimateDoor() {
        return framesToAnimateDoor;
    }
    
    public void setFramesToAnimateDoor(int framesToAnimateDoor) {
        this.framesToAnimateDoor = framesToAnimateDoor;
    }
    
    public ArrayList<TiledMapTileLayer.Cell> getDoorsInMap() {
        return doorsInMap;
    }
    
    public void setDoorsInMap(ArrayList<TiledMapTileLayer.Cell> doorsInMap) {
        this.doorsInMap = doorsInMap;
    }
    
    public HashMap<Integer, TiledMapTile> getDoorTiles() {
        return doorTiles;
    }
    
    public void setDoorTiles(HashMap<Integer, TiledMapTile> doorTiles) {
        this.doorTiles = doorTiles;
    }
    
    private MapCreator getMapCreator() {
        return mapCreator;
    }
    
    private void setMapCreator(MapCreator mapCreator) {
        this.mapCreator = mapCreator;
    }
    
    private Stage getStage() {
        return stage;
    }
    
    private void setStage(Stage stage) {
        this.stage = stage;
    }
    
    private OrthographicCamera getCamera() {
        return camera;
    }
    
    private void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }
    
    public Engine getEngine() {
        return engine;
    }
    
    public void setEngine(Engine engine) {
        this.engine = engine;
    }
    
    public TmxMapLoader getMapLoader() {
        return mapLoader;
    }
    
    private void setMapLoader(TmxMapLoader mapLoader) {
        this.mapLoader = mapLoader;
    }
    
    public TiledMap getMap() {
        return map;
    }
    
    public void setMap(TiledMap map) {
        this.map = map;
    }
    
    private Viewport getViewport() {
        return viewport;
    }
    
    private void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }
    
    @Override
    public void show() {
        setCamera(new OrthographicCamera());
        setViewport(new FitViewport(Engine.getvWidth() / Engine.getCameraZoomScale(), Engine.getvHeight() / Engine.getCameraZoomScale(), getCamera()));
        setHud(new Hud(getEngine()));
        setMapLoader(new TmxMapLoader());
        
        setMap(getMapLoader().load(Maps.TWINLEAF_TOWN.getTmxPath()));
        setRenderer(new MultiTileMapRenderer(getMap(), 1.0f, getEngine().getBatch()));
        
        setMapCreator(new MapCreator(this, getMap()));
    
        setUser(new User(getMapCreator()));
    
        getCamera().position.set(getViewport().getWorldWidth() / Engine.getCameraZoomScale(), getViewport().getWorldHeight() / Engine.getCameraZoomScale(), 0);
        
        setStage(new Stage(getViewport(), getEngine().getBatch()));
        getStage().addActor(getUser());
        Gdx.input.setInputProcessor(getStage());
    
        sound = AudioData.TWINLEAF_TOWN_DAYTIME;
        sound.playAudio();
        dialog = new Dialog(getEngine(), 0, 0, Engine.getvWidth(), 64, TextSpeeds.FAST,
                            "Test Character Writer ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz 0123456789 Test to wrap to the next line " +
                            "Test Character Writer ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz 0123456789 Test to wrap to the next line"
        );
    
        TiledMapTileSet tileSet = getMap().getTileSets().getTileSet("SinnohTileSet");
    
        setDoorTiles(new HashMap<>());
        for(TiledMapTile tile : tileSet) {
            Object property = tile.getProperties().get("Door Animation");
            if(property != null) {
                getDoorTiles().put((int)(property), tile);
            }
        }
    
        setDoorsInMap(new ArrayList<>());
        TiledMapTileLayer layer = (TiledMapTileLayer)(getMap().getLayers().get("Object Bottom"));
        for(int x = 0; x < layer.getWidth(); x++) {
            for(int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if(cell != null && cell.getTile().getProperties().containsKey("Door Animation")) {
                    Object property = cell.getTile().getProperties().get("Door Animation");
                    if(property != null) {
                        getDoorsInMap().add(cell);
                    }
                }
            }
        }
        setDoorToOpen(null);
        setFramesToAnimateDoor(6);
        setCurrentDoorFrameCount(0);
    }
    
    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            getEngine().setScreen(getEngine().getScreen(Engine.screenTypes.BATTLE_SCREEN));
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            getRenderer().setOffsetX(Engine.getTileSize());
            getMapCreator().resetTiledObjects(this, getMap());
            getUser().setMapCreator(getMapCreator());
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            getRenderer().setOffsetX(-Engine.getTileSize());
            getMapCreator().resetTiledObjects(this, getMap());
            getUser().setMapCreator(getMapCreator());
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            getRenderer().setOffsetX(0);
            getMapCreator().resetTiledObjects(this, getMap());
            getUser().setMapCreator(getMapCreator());
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            getRenderer().alterOffsetMapValues(16, 0);
            getMapCreator().resetTiledObjects(this, getMap());
            getUser().setMapCreator(getMapCreator());
        }
        
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        renderBackground();
        
        getStage().act(delta);
        getStage().draw();
        
        renderObjects();
    
        if(getDoorToOpen() != null) {
            setCurrentDoorFrameCount(getCurrentDoorFrameCount() + 1);
            if(getCurrentDoorFrameCount() % getFramesToAnimateDoor() == 0) {
                animateDoor();
            }
        }
        
        getEngine().getBatch().setProjectionMatrix(getHud().stage.getCamera().combined);
        getHud().stage.draw();
        
        getEngine().getBatch().setProjectionMatrix(dialog.getStage().getCamera().combined);
        dialog.getStage().act(delta);
        dialog.getStage().draw();
        
        fpsLogger.log();
    }
    
    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height);
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
        getMap().dispose();
        getRenderer().dispose();
        getHud().dispose();
        getStage().dispose();
        sound.getAudio().dispose();
        dialog.dispose();
    }
    
    public void animateDoor() {
        for(TiledMapTileLayer.Cell cell : getDoorsInMap()) {
            try {
                int currentAnimationFrame = (int)(cell.getTile().getProperties().get("Door Animation"));
                
                currentAnimationFrame++;
                
                TiledMapTile newTile = getDoorTiles().get(currentAnimationFrame);
                cell.setTile(newTile);
            } catch(NullPointerException e) {
                if(getDoorToOpen() == null) {
                    return;
                }
                getDoorToOpen().switchRooms();
            }
        }
    }
    
    private void renderBackground() {
        for(MapLayer mapLayer : getMap().getLayers()) {
            if(!mapLayer.getName().equalsIgnoreCase("Objects") && !mapLayer.getName().equalsIgnoreCase("Collisions")) {
                try {
                    getRenderer().renderTileLayer((TiledMapTileLayer)(mapLayer));
                } catch(ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void renderObjects() {
        for(MapLayer mapLayer : getMap().getLayers()) {
            if(mapLayer.getName().equalsIgnoreCase("Objects")) {
                try {
                    getRenderer().renderTileLayer((TiledMapTileLayer)(mapLayer));
                } catch(ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void update(float deltaTime) {
        handleInput(deltaTime);
        
        getUser().update(deltaTime);
        
        float halfViewportWidth  = getCamera().viewportWidth / 2;
        float halfViewportHeight = getCamera().viewportHeight / 2;
        
        int mapWidth  = (int)(getMap().getProperties().get("width"));
        int mapHeight = (int)(getMap().getProperties().get("height"));
    
        getCamera().position.x =
                (mapWidth > getCamera().viewportWidth) ? MathUtils.clamp(getUser().getPositionX(), halfViewportWidth, mapWidth * Engine.getTileSize() - halfViewportWidth) :
                getUser().getPositionX();
    
        getCamera().position.y =
                (mapHeight > getCamera().viewportHeight) ? MathUtils.clamp(getUser().getPositionY(), halfViewportHeight, mapHeight * Engine.getTileSize() - halfViewportHeight) :
                getUser().getPositionY();
        
        getCamera().update();
        getRenderer().setView(getCamera());
    }
    
    MultiTileMapRenderer getRenderer() {
        return renderer;
    }
    
    private void setRenderer(MultiTileMapRenderer renderer) {
        this.renderer = renderer;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    private Hud getHud() {
        return hud;
    }
    
    private void setHud(Hud hud) {
        this.hud = hud;
    }
    
    private void handleInput(float deltaTime) {
        getUser().handleInput(deltaTime);
    }
}
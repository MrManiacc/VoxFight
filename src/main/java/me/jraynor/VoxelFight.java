package me.jraynor;

import me.jraynor.bootstrap.IEngine;
import me.jraynor.bootstrap.Window;
import me.jraynor.core.block.Blocks;
import me.jraynor.core.entity.PlayerEntity;
import me.jraynor.core.lighting.Pipeline;
import me.jraynor.core.physics.PhysicsWorld;
import me.jraynor.core.physics.PlayerCollider;
import me.jraynor.core.registry.utils.Parser;
import me.jraynor.core.world.World;
import me.jraynor.core.ui.MainController;
import me.jraynor.uison.UIMaster;
import me.jraynor.uison.misc.Input;
import org.joml.Vector3f;

public class VoxelFight extends IEngine {
    private final Window window;
    private World world;
    private PhysicsWorld physicsWorld;
    private PlayerCollider playerCollider;
    private String pack;
    private Pipeline pipeline;

    private VoxelFight(String pack, int width, int height, String title) {
        super(40D);
        this.pack = pack;
        this.window = new Window(width, height, false, false, false, false, title);
        PlayerEntity playerEntity = new PlayerEntity(window, new Vector3f(0, 10, 0), 4);
        this.world = new World(playerEntity, "");
        this.physicsWorld = new PhysicsWorld(10f, playerEntity, world);
        this.world.setPhysicsWorld(physicsWorld);
        this.playerCollider = new PlayerCollider(world.getPlayerEntity(), physicsWorld, world);
        this.pipeline = new Pipeline(window, world);
        this.window.start(this);
    }

    public void preInit() {
        Parser.parse(this.pack);
    }

    public void postInit() {
        UIMaster.createUIMaster("src/main/resources/" + pack + "/ui/", window, new MainController(window, world));
        Blocks.initBlocks();
        pipeline.init();
        world.init(pack);
        physicsWorld.init();
    }

    public void renderUI(float v) {
//        UIMaster.update(window);
    }

    @Override
    public void render(float delta) {
        pipeline.render();
    }

    public void tick(float tps) {
        playerCollider.tick();
        physicsWorld.tick();
    }

    public void update(float v) {
        physicsWorld.update(v);
        playerCollider.update();
        world.update(v);
        world.tick(v);
        Input.globalMouse();
    }


    public static void main(String[] args) {
        switch (args.length) {
            case 1:
                new VoxelFight(args[0], 1080, 720, "VoxelFight");
                break;
            case 2:
                new VoxelFight(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[1]), "VoxelFight");
            case 3:
                new VoxelFight("core", Integer.parseInt(args[1]), Integer.parseInt(args[2]), "VoxelFight");
                break;
            default:
                new VoxelFight("core", 1080, 720, "VoxelFight");
        }
    }
}

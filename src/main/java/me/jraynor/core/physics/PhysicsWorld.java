package me.jraynor.core.physics;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import me.jraynor.core.entity.PlayerEntity;
import me.jraynor.core.world.World;
import org.joml.Vector4i;

import javax.vecmath.Vector3f;
import java.util.HashSet;
import java.util.Set;

public class PhysicsWorld {
    //Stupid resolvers for jbullet
    private DynamicsWorld dynamicsWorld;
    private DebugDraw debugDraw;
    private PlayerEntity playerEntity;
    private Set<Body> bodies = new HashSet<>();
    private BoxBody[][][] localBlocks = new BoxBody[10][10][10];//The blocks around the player with a radius of 16
    private World world;

    public PhysicsWorld(float gravity, PlayerEntity playerEntity, World world) {
        this.playerEntity = playerEntity;
        this.world = world;
        BroadphaseInterface broadphaseInterface = new DbvtBroadphase();
        CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
        ConstraintSolver solver = new SequentialImpulseConstraintSolver();
        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphaseInterface, solver, collisionConfiguration);
        dynamicsWorld.setGravity(new Vector3f(0, -gravity, 0));
        createGround();
    }

    public void init() {
        debugDraw = new DebugDraw();
        debugDraw.init(playerEntity);
        debugDraw.setDebugMode(2);
        dynamicsWorld.setDebugDrawer(debugDraw);
        for (int x = 0; x < localBlocks.length; x++) {
            for (int y = 0; y < localBlocks[0].length; y++) {
                for (int z = 0; z < localBlocks[0][0].length; z++) {
                    localBlocks[x][y][z] = new BoxBody(new org.joml.Vector3f(x, y, z), new org.joml.Vector3f(0.5f, 0.5f, 0.5f));
//                    localBlocks[x][y][z].getBody().setCollisionFlags(CollisionFlags.STATIC_OBJECT);
                    localBlocks[x][y][z].setActivationState(Body.DISABLE_DEACTIVATION);
                    localBlocks[x][y][z].setMargin(0.3f);
                    localBlocks[x][y][z].rigidBody.setUserPointer(new Vector4i(x, y, z, -69));
                    addBody(localBlocks[x][y][z]);
                }
            }
        }
    }


    public void tick() {
//        for (int x = (int) (playerEntity.getPosition().x - (localBlocks.length / 2)); x < playerEntity.getPosition().x + (localBlocks.length / 2); x++) {
//            for (int y = (int) (playerEntity.getPosition().y - (localBlocks.length / 2)); y < playerEntity.getPosition().y + (localBlocks.length / 2); y++) {
//                for (int z = (int) (playerEntity.getPosition().z - (localBlocks.length / 2)); z < playerEntity.getPosition().z + (localBlocks.length / 2); z++) {
////                    localBlocks[0][0][0].setPosition(x, y, z);
//                    System.out.println(x + " : " + y + " : " + z);
////                    if (world.getBlock(x, y, z) == 0)
////                        removeBody(localBlocks[x1][y1][z1]);
////                    else
////                        addBody(localBlocks[x1][y1][z1]);
//                }
//            }
//        }

        for (int x = 0; x < localBlocks.length; x++) {
            for (int y = 0; y < localBlocks[0].length; y++) {
                for (int z = 0; z < localBlocks[0][0].length; z++) {
                    int x1 = (playerEntity.getBlock().x + (x - localBlocks.length / 2));
                    int y1 = (playerEntity.getBlock().y + (y - localBlocks[0].length / 2));
                    int z1 = (playerEntity.getBlock().z + (z - localBlocks[0][0].length / 2));
                    localBlocks[x][y][z].setPosition(x1, y1, z1);
                    Vector4i pointer = (Vector4i) localBlocks[x][y][z].rigidBody.getUserPointer();
                    pointer.x = x1;
                    pointer.y = y1;
                    pointer.z = z1;
                    if (world.getBlock(x1, y1, z1) == 0)
                        removeBody(localBlocks[x][y][z]);
                    else
                        addBody(localBlocks[x][y][z]);
                }
            }
        }
    }

    public void addBody(Body body) {
        if (!bodies.contains(body)) {
            dynamicsWorld.addRigidBody(body.getBody());
            bodies.add(body);
        }
    }

    public void removeBody(Body body) {
        if (bodies.contains(body)) {
            bodies.remove(body);
            dynamicsWorld.removeRigidBody(body.getBody());
        }
    }

    private void createGround() {
        Body groundBody = new Body(0, new StaticPlaneShape(new Vector3f(0, 1, 0), 0.5f), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
        groundBody.setRestitution(0.1f);
        dynamicsWorld.addRigidBody(groundBody.getBody());
    }


    public void update(float deltaTime) {
        dynamicsWorld.stepSimulation(deltaTime * 0.001f);
    }

    public void render() {
//        debugDraw.loadShader();
//        dynamicsWorld.debugDrawWorld();
//        debugDraw.stopShader();
    }

    public DynamicsWorld getWorld() {
        return dynamicsWorld;
    }


}

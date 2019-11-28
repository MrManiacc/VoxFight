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

import javax.vecmath.Vector3f;
import java.util.HashSet;
import java.util.Set;

public class PhysicsWorld{
    //Stupid resolvers for jbullet
    private DynamicsWorld dynamicsWorld;
    private BroadphaseInterface broadphaseInterface;
    private CollisionConfiguration collisionConfiguration;
    private CollisionDispatcher dispatcher;
    private ConstraintSolver solver;
    private DebugDraw debugDraw;
    private PlayerEntity playerEntity;
    private Set<Body> bodies = new HashSet<>();
    private boolean update;

    public PhysicsWorld(float gravity, PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
        broadphaseInterface = new DbvtBroadphase();
        collisionConfiguration = new DefaultCollisionConfiguration();
        dispatcher = new CollisionDispatcher(collisionConfiguration);
        solver = new SequentialImpulseConstraintSolver();
        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphaseInterface, solver, collisionConfiguration);
        dynamicsWorld.setGravity(new Vector3f(0, -gravity, 0));
        createGround();
    }

    public void init() {
        debugDraw = new DebugDraw();
        debugDraw.init(playerEntity);
//        debugDraw.setDebugMode(2);
//        dynamicsWorld.setDebugDrawer(debugDraw);
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
        debugDraw.loadShader();
        dynamicsWorld.debugDrawWorld();
        debugDraw.stopShader();
    }

    public DynamicsWorld getWorld() {
        return dynamicsWorld;
    }


}

package me.jraynor.core.physics;

import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.linearmath.Transform;
import me.jraynor.core.chunk.Chunk;
import me.jraynor.core.entity.PlayerEntity;
import me.jraynor.core.world.World;
import org.joml.Vector2i;
import org.joml.Vector4i;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class PlayerCollider {
    private PlayerEntity playerEntity;
    private PhysicsWorld physicsWorld;
    private World world;
    private CollisionWorld.ClosestRayResultCallback rayCastCallback;

    public PlayerCollider(PlayerEntity playerEntity, PhysicsWorld physicsWorld, World world) {
        this.playerEntity = playerEntity;
        this.physicsWorld = physicsWorld;
        this.world = world;
        physicsWorld.addBody(playerEntity.getPhysicsBody());
    }

    public void tick() {
        rayCastCallback = new CollisionWorld.ClosestRayResultCallback(new Vector3f(playerEntity.getPosition().x, playerEntity.getPosition().y - 1, playerEntity.getPosition().z), new javax.vecmath.Vector3f(playerEntity.getPosition().x, -256, playerEntity.getPosition().z));
        physicsWorld.getWorld().rayTest(new Vector3f(playerEntity.getPosition().x, playerEntity.getPosition().y - 1, playerEntity.getPosition().z), new javax.vecmath.Vector3f(playerEntity.getPosition().x, -256, playerEntity.getPosition().z), rayCastCallback);
        if (rayCastCallback.hasHit()) {
            float val = rayCastCallback.closestHitFraction * 256;
            playerEntity.setDistanceFromGround(val);
        }
    }


    public void update() {
        int reach = 1000;
        Vector3f from = new Vector3f(playerEntity.getPosition().x, playerEntity.getPosition().y, playerEntity.getPosition().z);
        Vector3f to = new Vector3f(playerEntity.getPosition().x + (playerEntity.getDir().x * reach), (playerEntity.getPosition().y) + (playerEntity.getDir().y * reach), playerEntity.getPosition().z + (playerEntity.getDir().z * reach));
        RayCallback callback = new RayCallback(from, to);
        physicsWorld.getWorld().rayTest(from, to, callback);
        if (callback.hasHit()) {
            Transform transform = new Transform();
            callback.collisionObject.getWorldTransform(transform);
            if (callback.collisionObject.getUserPointer() instanceof Vector4i) {
                Vector4i position = (Vector4i) callback.collisionObject.getUserPointer();
                double distance = distance(transform.origin.x, transform.origin.y, transform.origin.z, playerEntity.getPosition().x, playerEntity.getPosition().y, playerEntity.getPosition().z);
                if (distance <= 4.5) {
                    Vector3f normal = callback.hitNormalWorld;
                    int face = getFace(normal);
                    playerEntity.setActiveBlock(new Vector4i(position.x, position.y, position.z, face));
                    playerEntity.setBlockSelected(true);
                }else{
                    playerEntity.setBlockSelected(false);
                }
            }
        }
    }

    private double distance(float x1, float y1, float z1, float x2, float y2, float z2) {
        double d = Math.pow((Math.pow(x2 - x1, 2) +
                Math.pow(y2 - y1, 2) +
                Math.pow(z2 - z1, 2) *
                        1.0), 0.5);
        return d;
    }


    private int getFace(Vector3f normal) {
        if (normal.y >= 0.98f && normal.y <= 1.0f)
            return 2;
        if (normal.y <= -0.98f && normal.y >= -1.0f)
            return 3;
        if (normal.x >= 0.98f && normal.x <= 1.0f)
            return 4;
        if (normal.x <= -0.98f && normal.x >= -1.0f)
            return 5;
        if (normal.z >= 0.98f && normal.z <= 1.0f)
            return 0;
        if (normal.z <= -0.98f && normal.z >= -1.0f)
            return 1;
        return -1;
    }

}

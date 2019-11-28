package me.jraynor.core.physics;

import com.bulletphysics.collision.broadphase.BroadphaseProxy;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.dynamics.RigidBody;
import org.joml.Vector4i;

import javax.vecmath.Vector3f;

public class RayCallback extends CollisionWorld.ClosestRayResultCallback {
    public RayCallback(Vector3f rayFromWorld, Vector3f rayToWorld) {
        super(rayFromWorld, rayToWorld);
    }

    @Override
    public boolean needsCollision(BroadphaseProxy proxy0) {
        if (proxy0.clientObject != null) {
            if (proxy0.clientObject instanceof RigidBody) {
                RigidBody rigidBody = (RigidBody) proxy0.clientObject;
                if (rigidBody.getUserPointer() instanceof Vector4i) {
                    Vector4i mask = (Vector4i) rigidBody.getUserPointer();
                    if (mask.w == -69)
                        return super.needsCollision(proxy0);
                }
            }
        }
        return false;
    }

    @Override
    public float addSingleResult(CollisionWorld.LocalRayResult rayResult, boolean normalInWorldSpace) {
//        if (rayResult != null && rayResult.localShapeInfo != null)
//            System.out.println(rayResult.localShapeInfo.triangleIndex);
        return super.addSingleResult(rayResult, normalInWorldSpace);
    }
}

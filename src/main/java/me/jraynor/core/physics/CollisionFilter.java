package me.jraynor.core.physics;

import com.bulletphysics.collision.broadphase.BroadphaseProxy;
import com.bulletphysics.collision.broadphase.OverlapFilterCallback;

public class CollisionFilter extends OverlapFilterCallback {
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 2;

    @Override
    public boolean needBroadphaseCollision(BroadphaseProxy proxy0, BroadphaseProxy proxy1) {
        return proxy0.collisionFilterGroup == ACTIVE && proxy1.collisionFilterGroup == ACTIVE;
    }
}

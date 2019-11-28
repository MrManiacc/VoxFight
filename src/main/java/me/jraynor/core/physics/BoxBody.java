package me.jraynor.core.physics;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.shapes.BoxShape;
import org.joml.Vector3f;


public class BoxBody extends Body {
    public BoxBody(Vector3f position, Vector3f size) {
        super(0, new BoxShape(new javax.vecmath.Vector3f(size.x, size.y, size.z)), new javax.vecmath.Vector3f(position.x + size.x, position.y + size.y, position.z + size.z), new javax.vecmath.Vector3f(0, 0, 0));
        rigidBody.setCollisionFlags(CollisionFlags.STATIC_OBJECT);
    }

    public void setSize(Vector3f size) {
        BoxShape shape = (BoxShape) this.shape;
        shape.setLocalScaling(new javax.vecmath.Vector3f(size.x, size.y, size.z));
        rigidBody.setCollisionShape(shape);
    }

    public void setMargin(float margin) {
        BoxShape shape = (BoxShape) this.shape;
        shape.setMargin(margin);
        rigidBody.setCollisionShape(shape);
    }

    public float getMargin() {
        BoxShape shape = (BoxShape) this.shape;
        return shape.getMargin();
    }

    public Vector3f getSize() {
        BoxShape shape = (BoxShape) this.shape;
        javax.vecmath.Vector3f out = new javax.vecmath.Vector3f();
        shape.getHalfExtentsWithMargin(out);
        return new Vector3f(out.x, out.y, out.z);
    }
}

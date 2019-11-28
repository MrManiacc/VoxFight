package me.jraynor.core.physics;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class Body {
    public static final int ACTIVE_TAG = 1;
    public static final int ISLAND_SLEEPING = 2;
    public static final int WANTS_DEACTIVATION = 3;
    public static final int DISABLE_DEACTIVATION = 4;
    public static final int DISABLE_SIMULATION = 5;
    ////////////////////////
    private Transform transform;
    private MotionState motionState;
    protected CollisionShape shape;
    private RigidBodyConstructionInfo rigidBodyInfo;
    protected RigidBody rigidBody;
    private float mass;
    private Vector3f inertia;
    ///////////////////////
    private org.joml.Vector3f position = new org.joml.Vector3f();
    private Vector3f positionOther = new Vector3f();

    public Body(float mass, CollisionShape collisionShape, Vector3f position, Vector3f rotation) {
        this.mass = mass;
        this.shape = collisionShape;
        this.inertia = new Vector3f();
        this.transform = createTransform(position.x, position.y, position.z, rotation.x, rotation.y, rotation.z);
        this.motionState = new DefaultMotionState(transform);
        this.rigidBodyInfo = createBodyInfo(mass, motionState, shape, new Vector3f(0, 0, 0));
        this.rigidBody = new RigidBody(rigidBodyInfo);
    }

    public Body(float mass, CollisionShape collisionShape, Vector3f position, Vector3f rotation, Vector3f inertia) {
        this.mass = mass;
        this.shape = collisionShape;
        this.inertia = inertia;
        this.shape.calculateLocalInertia(mass, inertia);
        this.transform = createTransform(position.x, position.y, position.z, rotation.x, rotation.y, rotation.z);
        this.motionState = new DefaultMotionState(transform);
        this.rigidBodyInfo = createBodyInfo(mass, motionState, shape, new Vector3f(0, 0, 0));
        this.rigidBody = new RigidBody(rigidBodyInfo);
    }

    public Body(float mass, CollisionShape collisionShape, Vector3f position, Vector3f rotation, Vector3f inertia, float margin) {
        this.mass = mass;
        this.shape = collisionShape;
        this.inertia = inertia;
        this.shape.calculateLocalInertia(mass, inertia);
        shape.setMargin(margin);
        this.transform = createTransform(position.x, position.y, position.z, rotation.x, rotation.y, rotation.z);
        this.motionState = new DefaultMotionState(transform);
        this.rigidBodyInfo = createBodyInfo(mass, motionState, shape, new Vector3f(0, 0, 0));
        this.rigidBody = new RigidBody(rigidBodyInfo);
    }

    public Body(float mass, CollisionShape collisionShape, Vector3f position, Vector3f rotation, MotionState motionState) {
        this.mass = mass;
        this.shape = collisionShape;
        this.inertia = new Vector3f();
        this.transform = createTransform(position.x, position.y, position.z, rotation.x, rotation.y, rotation.z);
        this.motionState = motionState;
        this.rigidBodyInfo = createBodyInfo(mass, motionState, shape, new Vector3f(0, 0, 0));
        this.rigidBody = new RigidBody(rigidBodyInfo);
    }

    public Body(float mass, CollisionShape collisionShape, Vector3f position, Vector3f rotation, MotionState motionState, Vector3f resistance) {
        this.mass = mass;
        this.shape = collisionShape;
        this.inertia = new Vector3f();
        this.transform = createTransform(position.x, position.y, position.z, rotation.x, rotation.y, rotation.z);
        this.motionState = motionState;
        this.rigidBodyInfo = createBodyInfo(mass, motionState, shape, resistance);
        this.rigidBody = new RigidBody(rigidBodyInfo);
    }

    public Body(float mass, CollisionShape collisionShape, Vector3f position, Vector3f rotation, MotionState motionState, Vector3f resistance, Vector3f inertia) {
        this.mass = mass;
        this.shape = collisionShape;
        this.inertia = inertia;
        this.shape.calculateLocalInertia(mass, inertia);
        this.transform = createTransform(position.x, position.y, position.z, rotation.x, rotation.y, rotation.z);
        this.motionState = motionState;
        this.rigidBodyInfo = createBodyInfo(mass, motionState, shape, resistance);
        this.rigidBody = new RigidBody(rigidBodyInfo);
    }


    public void setMass(float mass) {
        this.mass = mass;
        this.rigidBody.setMassProps(mass, inertia);
    }

    /**
     * The bounciness
     */
    public void setRestitution(float bounce) {
        this.rigidBody.setRestitution(bounce);
    }

    /**
     * Resistance to moving?
     * how quickly the object will stop moving
     *
     * @param damping
     */
    public void setAngularDamping(float damping) {
        this.rigidBody.setDamping(rigidBody.getLinearDamping(), damping);
    }

    /**
     * How much damping should be done to the linear side of damping
     *
     * @param damping
     */
    public void setLinearDamping(float damping) {
        this.rigidBody.setDamping(damping, rigidBody.getAngularDamping());
    }

    /**
     * The actictivation state, whether to keep checking physics or to let it sleep
     *
     * @param state
     */
    public void setActivationState(int state) {
        rigidBody.setActivationState(state);
    }


    public org.joml.Vector3f getPosition() {
        Vector3f pos = rigidBody.getWorldTransform(new Transform()).origin;
        position.x = pos.x;
        position.y = pos.y;
        position.z = pos.z;
        return position;
    }

    public void setPosition(float x, float y, float z) {
        Transform temp = new Transform();
        rigidBody.getCenterOfMassTransform(temp);
        temp.origin.x = x;
        temp.origin.y = y;
        temp.origin.z = z;
        rigidBody.setWorldTransform(temp);
    }


    /**
     * Gets the rigid body
     *
     * @return the rigidbody
     */
    public RigidBody getBody() {
        return rigidBody;
    }

    /**
     * Helper to create a transform
     */
    private com.bulletphysics.linearmath.Transform createTransform(float x, float y, float z, float rx, float ry, float rz) {
        return new com.bulletphysics.linearmath.Transform(new Matrix4f(new Quat4f(rx, ry, rz, 1), new Vector3f(x, y, z), 1.0f));
    }

    private RigidBodyConstructionInfo createBodyInfo(float mass, MotionState motionState, CollisionShape shape, Vector3f resistance) {
        return new RigidBodyConstructionInfo(mass, motionState, shape, resistance);
    }

}

package me.jraynor.core.entity;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.CylinderShape;
import me.jraynor.bootstrap.Window;
import me.jraynor.core.chunk.Chunk;
import me.jraynor.core.gl.Camera;
import me.jraynor.core.physics.Body;
import me.jraynor.core.world.World;
import me.jraynor.uison.misc.Input;
import org.joml.*;

import java.lang.Math;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerEntity extends BaseEntity {
    private Camera camera;
    private Vector3f dir = new Vector3f();
    private Vector3f right = new Vector3f();
    private Vector3f up = new Vector3f();
    private float sensitivity = 5;
    private float speed = 0.02f;
    private Vector2i chunkOrigin = new Vector2i();
    private Vector2i[] nextChunks = new Vector2i[]{new Vector2i(), new Vector2i(), new Vector2i(), new Vector2i(), new Vector2i(), new Vector2i(), new Vector2i(), new Vector2i()};
    private World world;
    private FrustumIntersection frustumIntersection;
    private Matrix4f prjViewMatrix;
    private boolean chunkUpdate = true;
    private Body physicsBody;
    private javax.vecmath.Vector3f tempVec = new javax.vecmath.Vector3f();
    private float distanceFromGround = 0;
    private Vector3i activeBlock = new Vector3i();
    private Vector3i nextBlock = new Vector3i();// the position of the next block which is calculated based on face of the active block
    private boolean grabbed = true;
    private boolean blockSelected = false;
    private Vector3f linearAcc = new Vector3f();
    private Vector3f linearVel = new Vector3f();
    private float linearDamping = 0.05f;

    /**
     * ALWAYS rotation about the local XYZ axes of the camera!
     */


    public Quaternionf rotation = new Quaternionf();

    public PlayerEntity(Window window, Vector3f position, int reach) {
        super(position);
        this.camera = new Camera(window, 80, 0.1f, 10000f);
        this.camera.setPerspective();
        this.frustumIntersection = new FrustumIntersection();
        this.prjViewMatrix = new Matrix4f().identity();
        this.physicsBody = new Body(100, new CylinderShape(new javax.vecmath.Vector3f(0.45f, 1, 0.45f)), new javax.vecmath.Vector3f(position.x, position.y, position.z), new javax.vecmath.Vector3f(0, 0, 0), new javax.vecmath.Vector3f(0, 0, 0));
        physicsBody.getBody().setFriction(0.001f);
        physicsBody.getBody().setAngularFactor(0);
        physicsBody.setLinearDamping(0.05f);
        physicsBody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
    }

    public void update(float deltaTime) {
        move(deltaTime);

        camera.getViewMatrix().identity().rotateX(rotation.x).rotateY(rotation.y).translate(-position.x, -position.y, -position.z);
        updateChunk();

        if (Input.keyPressed(GLFW_KEY_E)) {
            physicsBody.setPosition(position.x, position.y + 100, position.z);
        }

        prjViewMatrix.set(camera.getProjectionMatrix());
        prjViewMatrix.mul(camera.getViewMatrix());
        frustumIntersection.set(prjViewMatrix);
        linearVel.fma(deltaTime, linearAcc);
        linearVel.mul(1.0f - linearDamping * deltaTime);

        if (linearVel.x <= -6)
            linearVel.x = -6;
        else if (linearVel.x >= 6)
            linearVel.x = 6;
        if (linearVel.z <= -6)
            linearVel.z = -6;
        else if (linearVel.z >= 6)
            linearVel.z = 6;

        physicsBody.getBody().getLinearVelocity(tempVec);
        physicsBody.getBody().setLinearVelocity(new javax.vecmath.Vector3f(linearVel.x, tempVec.y, linearVel.z));
        position.x = physicsBody.getPosition().x;
        position.y = physicsBody.getPosition().y + 0.5f;
        position.z = physicsBody.getPosition().z;

        if (Input.keyDown(GLFW_KEY_Q) || Input.mousePressed(GLFW_MOUSE_BUTTON_LEFT)) {
            if (blockSelected) {
                world.setBlock(activeBlock.x, activeBlock.y, activeBlock.z, (byte) 0);
                blockSelected = false;
            }
        }

        if (Input.mousePressed(GLFW_MOUSE_BUTTON_RIGHT)) {
            if (blockSelected) {
                world.setBlock(nextBlock.x, nextBlock.y, nextBlock.z, (byte) 15);
                blockSelected = false;
            }
        }

    }


    /**
     * Checks if the chunk is inside the given frustum
     *
     * @param chunk
     * @return
     */
    public boolean chunkInFrustum(Chunk chunk) {
        Vector3f min = new Vector3f(chunk.getBounds().minX, chunk.getBounds().minY, chunk.getBounds().minZ);
        Vector3f max = new Vector3f(chunk.getBounds().maxX, chunk.getBounds().maxY, chunk.getBounds().maxZ);
        return frustumIntersection.testAab(min, max);
    }


    private void updateChunk() {
        int x = (int) position.x;
        int z = (int) position.z;
        if (0 > x)
            x = x - 16;
        if (z < 0)
            z = z - 16;
        int xO = Math.round(x - (x % 16));
        int zO = Math.round(z - (z % 16));
        if (chunkOrigin.x != xO || chunkOrigin.y != zO)
            chunkUpdate = true;
        chunkOrigin.x = xO;
        chunkOrigin.y = zO;
        nextChunks[0].x = chunkOrigin.x + 16;
        nextChunks[0].y = chunkOrigin.y;
        nextChunks[1].x = chunkOrigin.x - 16;
        nextChunks[1].y = chunkOrigin.y;
        nextChunks[2].x = chunkOrigin.x;
        nextChunks[2].y = chunkOrigin.y + 16;
        nextChunks[3].x = chunkOrigin.x;
        nextChunks[3].y = chunkOrigin.y - 16;
    }

    public Vector2i[] getNextChunks() {
        return nextChunks;
    }

    /**
     * Updates player rotation
     */
    private void updateRotation() {
        rotation.x = (float) (Input.mousePosition.y / Window.INSTANCE.getWidth()) * sensitivity;
        rotation.y = (float) (Input.mousePosition.x / Window.INSTANCE.getHeight()) * sensitivity;
    }


    /**
     * Updates direction
     *
     * @param deltaTime
     */
    private void updateDir(float deltaTime) {
        camera.getViewMatrix().positiveZ(dir).negate().mul(speed * deltaTime);
        camera.getViewMatrix().positiveX(right).mul(speed * deltaTime);
        camera.getViewMatrix().positiveY(up).mul(speed * deltaTime);
    }

    private void updatePositionLinear(float delta) {
        linearAcc.zero();
        float speed = 3.5f;
        if (Input.keyDown(GLFW_KEY_LEFT_SHIFT))
            speed = 5f;
        if (Input.keyDown(GLFW_KEY_W))
            linearAcc.fma(speed, dir);
        if (Input.keyDown(GLFW_KEY_S))
            linearAcc.fma(-speed, dir);
        if (Input.keyDown(GLFW_KEY_D)) {
            linearAcc.fma(speed, right);
        }
        if (Input.keyDown(GLFW_KEY_A))
            linearAcc.fma(-speed, right);

        if (Input.keyPressed(GLFW_KEY_SPACE) && distanceFromGround < 1.0f) {
            physicsBody.getBody().getLinearVelocity(tempVec);
            physicsBody.getBody().setLinearVelocity(new javax.vecmath.Vector3f(tempVec.x, 5, tempVec.z));
        }
    }


    private void move(float deltaTime) {
        if (Input.keyPressed(GLFW_KEY_ESCAPE)) {
            Input.setMousePosition(Window.INSTANCE.getWidth() / 2.0, Window.INSTANCE.getHeight() / 2.0);
            grabbed = !grabbed;
        }

        if (grabbed) {
            Input.setMouseGrabbed(true);
            updateDir(deltaTime);
            updatePositionLinear(deltaTime);
            updateRotation();
        } else
            Input.setMouseGrabbed(false);
    }

    public Vector2i getChunk() {
        return chunkOrigin;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Camera getCamera() {
        return camera;
    }

    public boolean isChunkUpdate() {
        return chunkUpdate;
    }

    public void setChunkUpdate(boolean chunkUpdate) {
        this.chunkUpdate = chunkUpdate;
    }

    public Body getPhysicsBody() {
        return physicsBody;
    }

    public void setDistanceFromGround(float distanceFromGround) {
        this.distanceFromGround = distanceFromGround;
    }

    public Vector3f getDir() {
        return dir;
    }

    public void setActiveBlock(Vector4i block) {
        activeBlock = new Vector3i(block.x, block.y, block.z);
        switch (block.w) {
            case 0:
                //north block
                nextBlock.x = activeBlock.x;
                nextBlock.y = activeBlock.y;
                nextBlock.z = activeBlock.z + 1;
                break;
            case 1:
                //south block
                nextBlock.x = activeBlock.x;
                nextBlock.y = activeBlock.y;
                nextBlock.z = activeBlock.z - 1;
                break;
            case 2:
                //top block
                nextBlock.x = activeBlock.x;
                nextBlock.y = activeBlock.y + 1;
                nextBlock.z = activeBlock.z;
                break;
            case 3:
                //bottom block
                nextBlock.x = activeBlock.x;
                nextBlock.y = activeBlock.y - 1;
                nextBlock.z = activeBlock.z;
                break;
            case 4:
                //east block
                nextBlock.x = activeBlock.x + 1;
                nextBlock.y = activeBlock.y;
                nextBlock.z = activeBlock.z;
                break;
            case 5:
                //west block
                nextBlock.x = activeBlock.x - 1;
                nextBlock.y = activeBlock.y;
                nextBlock.z = activeBlock.z;
                break;
        }
    }

    public Vector3i getActiveBlock() {
        return activeBlock;
    }

    public void setNeedsUpdate(boolean b) {
        this.chunkUpdate = b;
    }

    public void setBlockSelected(boolean blockSelected) {
        this.blockSelected = blockSelected;
    }
}

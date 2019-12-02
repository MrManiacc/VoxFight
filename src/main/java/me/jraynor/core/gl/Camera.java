package me.jraynor.core.gl;

import lombok.Getter;
import lombok.Setter;
import me.jraynor.engine.registry.CoreRegistry;
import me.jraynor.engine.window.Window;
import org.joml.Matrix4f;

public class Camera {
    @Getter
    private Matrix4f projectionMatrix, viewMatrix, vp;
    @Setter
    private float fov, near, far, aspect;
    @Getter
    private boolean perspective = true;
    private Window window;

    public Camera(float fov, float near, float far) {
        this.window = CoreRegistry.get(Window.class);
        this.fov = fov;
        this.near = near;
        this.far = far;
        this.aspect = (float) window.getWidth() / (float) window.getHeight();
        projectionMatrix = new Matrix4f().identity();
        viewMatrix = new Matrix4f().identity();
        vp = new Matrix4f().identity();
        setPerspective();
    }

    public void toggleView() {
        if (perspective)
            setOrthographic();
        else
            setPerspective();
    }

    public void setOrthographic() {
        perspective = false;
        float left = -(window.getWidth() / 2.0f);
        float right = (window.getWidth() / 2.0f);
        float top = -(window.getHeight() / 2.0f);
        float bottom = (window.getHeight() / 2.0f);

        this.projectionMatrix.identity();
        this.projectionMatrix = projectionMatrix.ortho(left, right, top, bottom, near, far);
    }

    public void setPerspective() {
        perspective = true;
        projectionMatrix.identity();
        this.projectionMatrix = projectionMatrix.setPerspective((float) Math.toRadians(fov), aspect, near, far);
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }
}

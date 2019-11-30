package me.jraynor.core.physics;

import com.bulletphysics.linearmath.IDebugDraw;
import me.jraynor.core.entity.PlayerEntity;
import me.jraynor.core.gl.GLUtils;
import me.jraynor.core.gl.Shader;
import me.jraynor.core.gl.ShaderBind;
import me.jraynor.core.gl.Vao;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

public class DebugDraw extends IDebugDraw {
    private int debugMode = 0;
    private Vao vao;
    private Shader shader;
    private PlayerEntity playerEntity;

    public void init(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
        this.shader = new Shader("debug") {
            @Override
            protected void doBinds() {
                super.binds(new ShaderBind("vertex", 0), new ShaderBind("color", 1));
            }

            @Override
            protected void doUniformBinds() {
                super.bindUniforms("viewMatrix", "projectionMatrix");
            }
        };
        shader.setPath("src/main/resources/core/shaders/debug.glsl");
        shader.loadShader();
    }


    @Override
    public void drawLine(Vector3f from, Vector3f to, Vector3f color) {
        vao = Vao.create();
        vao.bind(0, 1);
        float[] vertices = new float[6];
        float[] colors = new float[6];
        vertices[0] = from.x;
        vertices[1] = from.y;
        vertices[2] = from.z;
        colors[0] = color.x;
        colors[1] = color.y;
        colors[2] = color.z;
        vertices[3] = to.x;
        vertices[4] = to.y;
        vertices[5] = to.z;
        colors[3] = color.x;
        colors[4] = color.y;
        colors[5] = color.z;
        vao.createAttribute(0, vertices, 3);
        vao.createAttribute(1, colors, 3);
        glDrawArrays(GL_LINES, 0, 2);
        vao.unbind(0, 1);
        vao.delete();
    }

    public void loadShader() {
        shader.start();
        GLUtils.enableDepthTesting(true);
        shader.loadMat4("viewMatrix", playerEntity.getCamera().getViewMatrix());
        shader.loadMat4("projectionMatrix", playerEntity.getCamera().getProjectionMatrix());
    }

    public void stopShader() {
        shader.stop();
    }

    @Override
    public void drawContactPoint(Vector3f PointOnB, Vector3f normalOnB, float distance, int lifeTime, Vector3f color) {

    }

    @Override
    public void reportErrorWarning(String warningString) {

    }

    @Override
    public void draw3dText(Vector3f location, String textString) {

    }

    @Override
    public void setDebugMode(int debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public int getDebugMode() {
        return debugMode;
    }
}

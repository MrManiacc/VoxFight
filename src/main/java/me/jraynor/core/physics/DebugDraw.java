package me.jraynor.core.physics;

import com.bulletphysics.linearmath.IDebugDraw;
import me.jraynor.core.entity.PlayerEntity;
import me.jraynor.gl.GLUtils;
import me.jraynor.gl.Shader;
import me.jraynor.gl.ShaderBind;
import me.jraynor.gl.Vao;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

public class DebugDraw extends IDebugDraw {
    private int debugMode = 0;
    private Vao vao;
    private List<Line> lines = new ArrayList<>();
    private Shader shader;
    private PlayerEntity playerEntity;

    private static class Line {
        float fX, fY, fZ, tX, tY, tZ, r, g, b;

        public Line(float fX, float fY, float fZ, float tX, float tY, float tZ, float r, float g, float b) {
            this.fX = fX;
            this.fY = fY;
            this.fZ = fZ;
            this.tX = tX;
            this.tY = tY;
            this.tZ = tZ;
            this.r = r;
            this.g = g;
            this.b = b;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Line line = (Line) o;
            return Float.compare(line.fX, fX) == 0 &&
                    Float.compare(line.fY, fY) == 0 &&
                    Float.compare(line.fZ, fZ) == 0 &&
                    Float.compare(line.tX, tX) == 0 &&
                    Float.compare(line.tY, tY) == 0 &&
                    Float.compare(line.tZ, tZ) == 0 &&
                    Float.compare(line.r, r) == 0 &&
                    Float.compare(line.g, g) == 0 &&
                    Float.compare(line.b, b) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(fX, fY, fZ, tX, tY, tZ, r, g, b);
        }
    }

    public void init(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
        this.shader = new Shader("debug") {
            @Override
            protected void doBinds() {
                super.binds(new ShaderBind("vertex", 0), new ShaderBind("color", 1));
            }

            @Override
            protected void doUniformBinds() {
                super.bindUniforms("viewMatrix", "proMatrix");
            }
        };
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
        shader.loadMat4("proMatrix", playerEntity.getCamera().getProjectionMatrix());
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

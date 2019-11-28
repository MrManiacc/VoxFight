package me.jraynor.core.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import static org.lwjgl.opengl.GL11.*;

public class GLUtils {

    private static boolean cullingBackFace = false;
    public static boolean inWireframe = false;
    private static boolean isAlphaBlending = false;
    private static boolean additiveBlending = false;
    private static boolean antialiasing = false;
    private static boolean depthTesting = false;

    public static void antialias(boolean enable) {
        if (enable && !antialiasing) {
            GL11.glEnable(GL13.GL_MULTISAMPLE);
            antialiasing = true;
        } else if (!enable && antialiasing) {
            GL11.glDisable(GL13.GL_MULTISAMPLE);
            antialiasing = false;
        }
    }

    public static void enableAlphaBlending() {
        if (!isAlphaBlending) {
            GL11.glEnable(GL11.GL_BLEND);
            glBlendFunc(GL11.GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            isAlphaBlending = true;
            additiveBlending = false;
        }
    }

    public void setWireFrame(boolean wireFrame) {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, !wireFrame ? GL11.GL_FILL : GL11.GL_LINE);
    }

    public static void enableAdditiveBlending() {
        if (!additiveBlending) {
            GL11.glEnable(GL11.GL_BLEND);
            glBlendFunc(GL11.GL_SRC_ALPHA, GL_ONE);
            additiveBlending = true;
            isAlphaBlending = false;
        }
    }
    public static void enableOtherBlending() {
        if (!additiveBlending) {
            GL11.glEnable(GL11.GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
            additiveBlending = true;
            isAlphaBlending = false;
        }
    }



    public static void disableBlending() {
        if (isAlphaBlending || additiveBlending) {
            GL11.glDisable(GL11.GL_BLEND);
            isAlphaBlending = false;
            additiveBlending = false;
        }
    }

    public static void enableDepthTesting(boolean enable) {
        if (enable && !depthTesting) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            depthTesting = true;
        } else if (!enable && depthTesting) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            depthTesting = false;
        }
    }

    public static void cullBackFaces(boolean cull) {
        if (cull && !cullingBackFace) {
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glCullFace(GL11.GL_BACK);
            cullingBackFace = true;
        } else if (!cull && cullingBackFace) {
            GL11.glDisable(GL11.GL_CULL_FACE);
            cullingBackFace = false;
        }
    }

    public static void goWireframe(boolean goWireframe) {
        if (goWireframe && !inWireframe) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            inWireframe = true;
        } else if (!goWireframe && inWireframe) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            inWireframe = false;
        }
    }

}

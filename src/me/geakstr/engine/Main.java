package me.geakstr.engine;

import me.geakstr.engine.core.*;
import me.geakstr.engine.math.Matrix4f;
import me.geakstr.engine.math.Vector3f;
import me.geakstr.engine.model.Model;

import me.geakstr.engine.model.ResourceBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Main extends Game {
	private Camera camera;
    private Frustum frustum;
	private Shader baseShader;
	private Transform transform;

    private List<Model> models;

    private boolean wasInput;

    public Main(String resDir) {
        super(resDir);
    }

	public void init() {
        Display.setTitle("3D Engine");
		
		camera = new Camera();
		camera.setPosition(0, 0, -8);

        frustum = new Frustum();

        baseShader = new Shader();
        baseShader.attachVertexShader("shader.vert");
        baseShader.attachFragmentShader("shader.frag");
        baseShader.link();

        transform = new Transform();

        ResourceBuffer.loadModels("cube/cube.obj");
        Model box = ResourceBuffer.getModels().get("cube/cube.obj");

        models = new ArrayList<Model>();
        for (float z = 0, ang = 0; z <= 100; z += 2) {
            for (float y = 0; y <= 100; y += 2) {
                for (float x = 0; x <= 100; x += 2, ang += 0) {
                    models.add(box.clone(x, y, z, ang, ang, ang));
                }
            }
        }

        wasInput = true;

		glCullFace(GL_FRONT_FACE);
	}

	public void update() {
		wasInput = camera.input();
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
		
		camera.apply();

        if (wasInput) {
            frustum.update(camera.getProjectionMatrix(), camera.getViewMatrix());
        }

        baseShader.bind();
        baseShader.setUniform("mProjection", camera.getProjectionMatrix());
        baseShader.setUniform("mView", camera.getViewMatrix());
        baseShader.setUniform("mNormal", camera.getNormalMatrix());
        baseShader.setUniform("mTransform", transform.getTransform());
        baseShader.setUniform("lightPos", camera.getPosition());
        baseShader.setUniform("texture", 0);

        for (Model model : models) {
            // TODO: May be -1?
            float x = model.getX();
            float y = model.getY();
            float z = model.getZ();

            if (frustum.checkCube(x, y, z, 2) >= 1) {
                model.render(baseShader);
            }
        }

        baseShader.unbind();
	}

	public void resized() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public void dispose() {
        for (Model model : models) model.dispose();
        baseShader.dispose();
	}

	public static void main(String[] args) {
		new Main(args[0]);
	}
}

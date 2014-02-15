package me.geakstr.engine.core;

import me.geakstr.engine.math.Vector3f;
import me.geakstr.engine.math.Matrix4f;
import me.geakstr.engine.utils.FileUtil;
import me.geakstr.engine.utils.MatrixUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
	int programID;
	int vertexShaderID;
	int fragmentShaderID;

	public Shader() {
		programID = glCreateProgram();
	}

	public void attachVertexShader(String name) {
		name = Game.RES_DIR + "/shaders/" + name;
		String vertexShaderSource = FileUtil.readFromFile(name);
		vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShaderID, vertexShaderSource);
		glCompileShader(vertexShaderID);

		if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Unable to create vertex shader:");
			System.err.println(glGetShaderInfoLog(vertexShaderID, glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH)));
			dispose();
			Game.end();
		}
		glAttachShader(programID, vertexShaderID);
	}

	public void attachFragmentShader(String name) {
		name = Game.RES_DIR + "/shaders/" + name;
		String fragmentShaderSource = FileUtil.readFromFile(name);
		fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShaderID, fragmentShaderSource);
		glCompileShader(fragmentShaderID);
		if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Unable to create fragment shader:");
			System.err.println(glGetShaderInfoLog(fragmentShaderID, glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH)));
			dispose();
			Game.end();
		}
		glAttachShader(programID, fragmentShaderID);
	}

	public void link() {
		glLinkProgram(programID);
		if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Unable to link shader program:");
			dispose();
			Game.end();
		}
	}

	public void bind() {
		glUseProgram(programID);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void dispose() {
		unbind();

		glDetachShader(programID, vertexShaderID);
		glDetachShader(programID, fragmentShaderID);

		glDeleteShader(vertexShaderID);
		glDeleteShader(fragmentShaderID);

		glDeleteProgram(programID);
	}

	public int getID() {
		return programID;
	}

	public void setUniform(String name, Matrix4f value) {
		glUniformMatrix4(glGetUniformLocation(programID, name), false, MatrixUtil.toFloatBuffer(value));
	}

	public void setUniform(String name, Vector3f value) {
		glUniform3f(glGetUniformLocation(programID, name), value.x, value.y, value.z);
	}

	public void setUniform(String name, float value) {
		glUniform1f(glGetUniformLocation(programID, name), value);
	}
}

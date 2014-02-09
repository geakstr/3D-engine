package me.geakstr.engine.model;

import me.geakstr.engine.math.Vector3f;

public class Face {
	private Vector3f vertex;
	private Vector3f normal;
	private Vector3f texCoords;
	private Material material;

	public Face(Vector3f vertex, Vector3f normal, Vector3f texCoords, Material material) {
		this.vertex = vertex;
		this.normal = normal;
		this.material = material;
		this.texCoords = texCoords;

		if (material == null) {
			material = new Material("Default");
		}
	}

	public Vector3f getVertex() {
		return vertex;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public Vector3f getTexCoord() {
		return texCoords;
	}

	public Material getMaterial() {
		return material;
	}
}

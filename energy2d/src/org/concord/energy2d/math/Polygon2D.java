/*
 *   Copyright (C) 2009  The Concord Consortium, Inc.,
 *   25 Love Lane, Concord, MA 01742
 */

package org.concord.energy2d.math;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Mutatable implementation of polygon (GeneralPath is immutatable).
 * 
 * @author Charles Xie
 * 
 */
public class Polygon2D implements TransformableShape {

	private Point2D.Float[] vertex;
	private GeneralPath path;

	/** the coordinates of the vertices of this polygon. */
	public Polygon2D(float[] x, float[] y) {
		if (x.length != y.length)
			throw new IllegalArgumentException("the number of x coodinates must be equal to that of the y coordinates.");
		if (x.length < 3)
			throw new IllegalArgumentException("the number of vertices must be no less than 3.");
		vertex = new Point2D.Float[x.length];
		for (int i = 0; i < x.length; i++)
			setVertex(i, x[i], y[i]);
		path = new GeneralPath();
	}

	public Polygon2D duplicate() {
		int n = vertex.length;
		float[] x = new float[n];
		float[] y = new float[n];
		for (int i = 0; i < n; i++) {
			x[i] = vertex[i].x;
			y[i] = vertex[i].y;
		}
		return new Polygon2D(x, y);
	}

	private void update() {
		path.reset();
		path.moveTo(vertex[0].x, vertex[0].y);
		for (int i = 1; i < vertex.length; i++)
			path.lineTo(vertex[i].x, vertex[i].y);
		path.closePath();
	}

	public void setVertex(int i, float x, float y) {
		if (i < 0 || i >= vertex.length)
			throw new IllegalArgumentException("index of vertex is out of bound.");
		if (vertex[i] == null)
			vertex[i] = new Point2D.Float(x, y);
		else
			vertex[i].setLocation(x, y);
	}

	public Point2D.Float getVertex(int i) {
		if (i < 0 || i >= vertex.length)
			throw new IllegalArgumentException("index of vertex is out of bound.");
		return vertex[i];
	}

	public int getVertexCount() {
		return vertex.length;
	}

	public void translateBy(float dx, float dy) {
		for (Point2D.Float p : vertex) {
			p.x += dx;
			p.y += dy;
		}
	}

	public void rotateBy(float degree) {
		Rectangle2D r = path.getBounds2D();
		double cx = r.getCenterX();
		double cy = r.getCenterY();
		double a = Math.toRadians(degree);
		double sin = Math.sin(a);
		double cos = Math.cos(a);
		double dx = 0;
		double dy = 0;
		for (Point2D.Float v : vertex) {
			dx = v.x - cx;
			dy = v.y - cy;
			v.x = (float) (dx * cos - dy * sin + cx);
			v.y = (float) (dx * sin + dy * cos + cy);
		}
	}

	public void scale(float scale) {
		Rectangle2D r = path.getBounds2D();
		double cx = r.getCenterX();
		double cy = r.getCenterY();
		for (Point2D.Float v : vertex) {
			v.x = (float) ((v.x - cx) * scale + cx);
			v.y = (float) ((v.y - cy) * scale + cy);
		}
	}

	public void scaleX(float scale) {
		Rectangle2D r = path.getBounds2D();
		double cx = r.getCenterX();
		for (Point2D.Float v : vertex) {
			v.x = (float) ((v.x - cx) * scale + cx);
		}
	}

	public void scaleY(float scale) {
		Rectangle2D r = path.getBounds2D();
		double cy = r.getCenterY();
		for (Point2D.Float v : vertex) {
			v.y = (float) ((v.y - cy) * scale + cy);
		}
	}

	public void shearX(float shear) {
		Rectangle2D r = path.getBounds2D();
		double cy = r.getCenterY();
		for (Point2D.Float v : vertex) {
			v.x += (float) (v.y - cy) * shear;
		}
	}

	public void shearY(float shear) {
		Rectangle2D r = path.getBounds2D();
		double cx = r.getCenterX();
		for (Point2D.Float v : vertex) {
			v.y += (float) (v.x - cx) * shear;
		}
	}

	public void flipX() {
		float cx = (float) path.getBounds2D().getCenterX();
		float dx = 0;
		for (Point2D.Float v : vertex) {
			dx = v.x - cx;
			v.x = cx - dx;
		}
	}

	public void flipY() {
		float cy = (float) path.getBounds2D().getCenterY();
		float dy = 0;
		for (Point2D.Float v : vertex) {
			dy = v.y - cy;
			v.y = cy - dy;
		}
	}

	public boolean contains(Point2D p) {
		return contains(p.getX(), p.getY());
	}

	public boolean intersects(Rectangle r) {
		update();
		return path.intersects(r);
	}

	public boolean contains(double x, double y) {
		update();
		return path.contains(x, y);
	}

	public Point2D.Float getBoundCenter() {
		Rectangle2D r = path.getBounds2D();
		return new Point2D.Float((float) r.getCenterX(), (float) r.getCenterY());
	}

	public Point2D.Float getCenter() {
		float xc = 0;
		float yc = 0;
		for (Point2D.Float v : vertex) {
			xc += v.x;
			yc += v.y;
		}
		return new Point2D.Float(xc / vertex.length, yc / vertex.length);
	}

	public Rectangle getBounds() {
		int xmin = Integer.MAX_VALUE;
		int ymin = xmin;
		int xmax = -xmin;
		int ymax = -xmin;
		for (Point2D.Float v : vertex) {
			if (xmin > v.x)
				xmin = Math.round(v.x);
			if (ymin > v.y)
				ymin = Math.round(v.y);
			if (xmax < v.x)
				xmax = Math.round(v.x);
			if (ymax < v.y)
				ymax = Math.round(v.y);
		}
		return new Rectangle(xmin, ymin, xmax - xmin, ymax - ymin);
	}

	public Rectangle2D getBounds2D() {
		float xmin = Float.MAX_VALUE;
		float ymin = xmin;
		float xmax = -xmin;
		float ymax = -xmin;
		for (Point2D.Float v : vertex) {
			if (xmin > v.x)
				xmin = v.x;
			if (ymin > v.y)
				ymin = v.y;
			if (xmax < v.x)
				xmax = v.x;
			if (ymax < v.y)
				ymax = v.y;
		}
		return new Rectangle2D.Float(xmin, ymin, xmax - xmin, ymax - ymin);
	}

	public boolean contains(Rectangle2D r) {
		update();
		return path.contains(r);
	}

	public boolean contains(double x, double y, double w, double h) {
		update();
		return path.contains(x, y, w, h);
	}

	public PathIterator getPathIterator(AffineTransform at) {
		update();
		return path.getPathIterator(at);
	}

	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		update();
		return path.getPathIterator(at, flatness);
	}

	public boolean intersects(Rectangle2D r) {
		update();
		return intersects(r);
	}

	public boolean intersects(double x, double y, double w, double h) {
		update();
		return intersects(x, y, w, h);
	}

}

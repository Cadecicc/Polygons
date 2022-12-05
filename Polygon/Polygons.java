import java.util.*;
import java.awt.*;

/**
 * Class that creates and stores which polygons you have made
 * @author Cade Ciccone
 *
 */
public class Polygons extends Polygon {

	//Instance variables
	private ArrayList<Point> vertices;
	private Color col;
	private boolean filledShape;
	
	/**
	 * Three-arg constructor to make a new Polygons object
	 * @param vert an ArrayList of all the points representing each vertice of the 
	 * new Polygons object
	 * @param c the Color of the new Polygons object
	 * @param filled a boolean representing if the new Polygons object is filled or not
	 */
	public Polygons(ArrayList<Point> vert, Color c, boolean filled) {
		vertices = vert;
		col = c;
		filledShape = filled;
		setVertices();
		setColor();
	}
	
	/**
	 * A method to tell the Polygon superclass where all of the vertices are
	 */
	private void setVertices() {
		for (Point p : vertices) {
			this.addPoint((int)p.getX(), (int)p.getY());
		}
	}
	
	/**
	 * Making the shape transparent if it is filled
	 */
	private void setColor() {
		if (filledShape) {
			col = new Color(col.getRed(), col.getGreen(), col.getBlue(), 50);
		}
	}
	
	/**
	 * Getter method to get the Color of the Polygons object
	 * @return the Color of the Polygons object
	 */
	public Color getColor() {
		return col;
	}
	
	/**
	 * Getter method to see if the Polygons object is filled or not
	 * @return true if it is filled and false otherwise
	 */
	public boolean isFilled() {
		return filledShape;
	}
	
	/**
	 * Getter method to get the ArrayList of all the vertices of the Polygons object
	 * @return the vertices of the Polygons object as an ArrayList
	 */
	public ArrayList<Point> getVertices() {
		return vertices;
	}
}

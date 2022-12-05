import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

/**
 * GUI class that generates the Shape displayer
 * 
 * @author Cade Ciccone
 *
 */
public class GUI extends JFrame {

	// Instance variables
	private JButton load;
	private JButton save;
	private JButton clear;
	private JPanel panel;
	private ArrayList<Point> vertices;
	private ArrayList<Polygons> polys;
	private Color col;

	/**
	 * No-arg constructor to make the JFrame object
	 */
	public GUI() {
		setTitle("Polygon Maker");
		setSize(800, 800);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.BLACK);

		vertices = new ArrayList<Point>();
		polys = new ArrayList<Polygons>();
		col = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));

		panelMaker();
	}

	/**
	 * Method to create the buttons on screen
	 */
	private void buttonMaker() {
		load = new JButton("Load Data");
		save = new JButton("Save Data");
		clear = new JButton("Clear");

		MouseClicked ear = new MouseClicked();
		load.addMouseListener(ear);
		save.addMouseListener(ear);
		clear.addMouseListener(ear);
	}

	/**
	 * Method to make the panel with a black background and add the buttons
	 */
	private void panelMaker() {
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.setBackground(Color.BLACK);

		buttonMaker();

		panel.add(load);
		panel.add(save);
		panel.add(clear);
		panel.addMouseListener(new MouseClicked());

		add(panel);
	}

	/**
	 * Paint method to add the shapes, lines, and points to the frame
	 */
	public void paint(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		if (vertices.size() == 0 && polys.size() == 0) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}

		// Drawing polygons
		paintPolygons(g);

		g.setColor(col);

		// Drawing dots
		for (int i = 0; i < vertices.size(); i++) {
			Point p = vertices.get(i);
			g.fillOval((int) p.getX() - 5, (int) p.getY() - 5, 10, 10);
		}

		// Drawing lines between dots
		if (vertices.size() > 1) {
			for (int i = 1; i < vertices.size(); i++) {
				Point p = vertices.get(i);
				Point p1 = vertices.get(i - 1);
				g.drawLine((int) p.getX(), (int) p.getY(), (int) p1.getX(), (int) p1.getY());
			}
		}
	}
	
	/**
	 * Method to paint each polygon with their vertices, lines, and if they are 
	 * filled in or not
	 * @param g The Graphics object passed from the paint method
	 */
	public void paintPolygons(Graphics g) {
		for (Polygons p : polys) {
			if (p.isFilled()) {
				g.setColor(p.getColor());
				g.fillPolygon(p);
				Color lessTransparent = new Color(p.getColor().getRed(), p.getColor().getGreen(), p.getColor().getBlue());
				g.setColor(lessTransparent);
			} else {
				g.setColor(p.getColor());
				g.drawPolygon(p);
				Color lessTransparent = new Color(p.getColor().getRed(), p.getColor().getGreen(), p.getColor().getBlue());
				g.setColor(lessTransparent);
			}
			for (int i = 0; i < p.npoints; i++) {
				g.fillOval(p.xpoints[i] - 5, p.ypoints[i] - 5, 10, 10);
			}
			for (int i = 0; i < p.npoints - 1; i++) {
				g.drawLine(p.xpoints[i], p.ypoints[i], p.xpoints[i + 1], p.ypoints[i + 1]);
			}
			g.drawLine(p.xpoints[0], p.ypoints[0], p.xpoints[p.npoints - 1], p.ypoints[p.npoints - 1]);
		}
	}

	/**
	 * Mouse listener class to listen for left and right clicks, and if the buttons 
	 * are clicked or just the frame is
	 * @author Cade Ciccone
	 *
	 */
	class MouseClicked extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			// Checking if the mouse presses any of the buttons and executes
			// the operations respectively
			if (e.getSource() == load) {
				try {
					loadFile();
				} catch (IOException excep) {
				}
			} else if (e.getSource() == save) {
				try {
					saveFile();
				} catch (IOException excep) {
				}
			} else if (e.getSource() == clear) {
				boardClear();
			} else if (e.getButton() == MouseEvent.BUTTON1) {
				shapeMaker(e);
			} else if (e.getButton() == MouseEvent.BUTTON3 && vertices.size() >= 3) {
				shapeFinisher(e);
			}
		}
	}

	/**
	 * Method to save the current finished and unfinished polygons to the 
	 * 'poly.dat' file
	 * @throws IOException
	 */
	private void saveFile() throws IOException {

		File file = new File("poly.dat");
		int reply = 0;

		//Warning the user that saving their data will overwrite the current 'poly.dat'
		//file, if it exists
		if (file.exists()) {
			reply = JOptionPane.showConfirmDialog(panel,
					"Doing this will overwrite the current file.\n" + "Are you sure you want to continue?",
					"Saving Warning", JOptionPane.YES_NO_OPTION);
		}

		//If the user decides to overwrite or if the file doesn't exist, take steps
		//to save the current data
		if (reply == JOptionPane.YES_OPTION || !file.exists()) {

			FileOutputStream out = new FileOutputStream(file);
			DataOutputStream dout = new DataOutputStream(out);

			//Writing the necessary data
			saving(dout);

			dout.close();
			out.close();
		}
	}

	/**
	 * Method to write all the necessary data to the 'poly.dat' file
	 * @param dout the DataOutputStream object that is being written to
	 * @throws IOException
	 */
	private void saving(DataOutputStream dout) throws IOException {
		dout.writeInt(polys.size());

		for (Polygons p : polys) {
			dout.writeInt(p.npoints);
			dout.writeBoolean(p.isFilled());
			int[] xpoints = p.xpoints;
			int[] ypoints = p.ypoints;

			for (int i = 0; i < p.npoints; i++) {
				dout.writeInt(xpoints[i]);
				dout.writeInt(ypoints[i]);
			}
			dout.writeInt(p.getColor().getRed());
			dout.writeInt(p.getColor().getGreen());
			dout.writeInt(p.getColor().getBlue());
		}
		for (Point p : vertices) {
			int px = (int) p.getX();
			int py = (int) p.getY();
			dout.writeInt(px);
			dout.writeInt(py);
		}
	}

	/**
	 * Method to load the data from a previous 'poly.dat' file and display that data
	 * @throws IOException
	 */
	private void loadFile() throws IOException {

		File file = new File("poly.dat");
		if (!file.exists()) {
			JOptionPane.showMessageDialog(panel, "Data file 'poly.dat' does not exist. Nothing loaded.",
					"Loading Error", JOptionPane.OK_OPTION);
		} else {

			FileInputStream in = new FileInputStream(file);
			DataInputStream din = new DataInputStream(in);
			vertices.clear();
			polys.clear();
			
			int reply = JOptionPane.showConfirmDialog(panel, "Loading a file overwrites current data."
					+ " Continue loading?", "Overwrite Data?", JOptionPane.YES_NO_OPTION);

			if (reply == JOptionPane.YES_OPTION) {
				loading(din);
				repaint();
			}

			din.close();
			in.close();
		}
	}

	/**
	 * Method to read the data from the 'poly.dat' file
	 * @param din The DataInputStream object to be read from
	 * @throws IOException
	 */
	private void loading(DataInputStream din) throws IOException {
		int polyCount = din.readInt();

		for (int i = 0; i < polyCount; i++) {
			int vertCount = din.readInt();
			boolean isFilled = din.readBoolean();
			ArrayList<Point> vert = new ArrayList<Point>();
			for (int j = 0; j < vertCount; j++) {
				vert.add(new Point(din.readInt(), din.readInt()));
			}
			Color c = new Color(din.readInt(), din.readInt(), din.readInt());
			polys.add(new Polygons(vert, c, isFilled));
		}

		boolean atEOF = false;
		while (!atEOF) {
			try {
				int x = din.readInt();
				int y = din.readInt();
				vertices.add(new Point(x, y));
			} catch (EOFException e) {
				atEOF = true;
			}
		}
	}

	/**
	 * Method to clear the frame of all shapes, dots, and lines
	 */
	private void boardClear() {
		JOptionPane.showMessageDialog(panel, "All polygons will be cleared.", "Alert", JOptionPane.INFORMATION_MESSAGE);

		vertices.clear();
		polys.clear();

		repaint();
	}

	/**
	 * Method to add the points where the user clicks to the vertices to be displayed
	 * @param e The MouseEvent from which the position is read from
	 */
	private void shapeMaker(MouseEvent e) {
		Point p = e.getPoint();
		p.translate(0, 18);

		vertices.add(p);
		repaint();
	}

	/**
	 * Method to create a Polygons object and add that to the polys to be displayed
	 * @param e The MouseEvent that is being checked for if the user right-clicks while
	 * holding down the SHIFT key
	 */
	private void shapeFinisher(MouseEvent e) {
		Polygons poly;
		if (e.isShiftDown()) {
			poly = new Polygons(vertices, col, true);
		} else {
			poly = new Polygons(vertices, col, false);
		}

		polys.add(poly);
		repaint();

		String hollowOrFilled;
		if (poly.isFilled()) {
			hollowOrFilled = "Filled";
		} else {
			hollowOrFilled = "Hollow";
		}

		JOptionPane.showMessageDialog(panel, "New " + hollowOrFilled + " " + vertices.size() + "-sided polygon recorded.\n" + 
				polys.size() + " polygons in all.", "Message", JOptionPane.INFORMATION_MESSAGE);

		vertices.clear();

		col = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
	}
}

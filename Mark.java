import java.awt.*;

public class Mark {
	private Point point;
	private Color color;
	private Dimension size;

	public Point getPoint() {
		return point;
	}

	public int getX() {
		return (int) getPoint().getX();
	}

	public int getY() {
		return (int) getPoint().getY();
	}

	public Color getColor() {
		return color;
	}

	public Dimension getSize() {
		return size;
	}

	public int getHeight() {
		return (int) getSize().getHeight();
	}

	public int getWidth() {
		return (int) getSize().getWidth();
	}

	public Mark(Point p, Dimension d, Color c) {
		point = p;
		size = d;
		color = c;
	}

	public Mark(int x, int y, int width, int height, Color c) {
		point = new Point(x, y);
		size = new Dimension(width, height);
		color = c;
	}
}

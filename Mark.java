import java.awt.*;

public class Mark {
	public final static int CIRCLE_BRUSH = 1;
	public final static int SQUARE_BRUSH = 2;
	public final static int TRIANGLE_BRUSH = 3;

	private int brush;
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

	public Mark(Point p, Dimension d, Color c, int brush) {
		point = p;
		size = d;
		color = c;
		setBrush(brush);
	}

	public Mark(int x, int y, int width, int height, Color c, int brush) {
		point = new Point(x, y);
		size = new Dimension(width, height);
		color = c;
		setBrush(brush);
	}

	public int getBrush() {
		return brush;
	}

	public void setBrush(int b) {
		if (b > 0 && b < 4) {
			brush = b;
		}
	}
}

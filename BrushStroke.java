import java.util.ArrayList;

public class BrushStroke {
	private ArrayList<Mark> marks;

	public BrushStroke() {
	}

	public BrushStroke(ArrayList<Mark> markList) {
		marks = markList;
	}

	public ArrayList<Mark> getMarks() {
		return marks;
	}

	public void setMarks(ArrayList<Mark> markList) {
		marks = markList;
	}

}

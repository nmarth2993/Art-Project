import java.util.ArrayList;

public class BrushStroke {
	private Mark startMark;
	private Mark endMark;
	private ArrayList<Mark> marks;

	public BrushStroke(Mark startMark, Mark endMark) {
		this.startMark = startMark;
		this.endMark = endMark;
	}

	public Mark getStartMark() {
		return startMark;
	}

	public void setStartMark(Mark startMark) {
		this.startMark = startMark;
	}

	public Mark getEndMark() {
		return endMark;
	}

	public void setEndMark(Mark endMark) {
		this.endMark = endMark;
	}

	public ArrayList<Mark> getMarks() {
		return marks;
	}

	public void setMarks(ArrayList<Mark> marks) {
		this.marks = marks;
	}

}

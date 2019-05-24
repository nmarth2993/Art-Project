import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class TicTacToe {

	final static int EMPTY = -1;
	final static int O = 0;
	final static int X = 1;

	JFrame frame;
	GamePanel panel;

	Mouse m;

	int[][] board;
	int[] winListX;
	int[] winListY;
	int current = X;
	int xCount;
	int oCount;
	boolean gameOver;

	public TicTacToe() {
		frame = new JFrame("3T");
		panel = new GamePanel();
		m = new Mouse();
		panel.addMouseListener(m);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(panel);
		frame.setPreferredSize(new Dimension(400, 400));
		frame.setVisible(true);
		frame.pack();

		board = new int[3][3];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = EMPTY;
			}
		}

		winListX = new int[3];
		winListY = new int[3];
		for (int i = 0; i < winListX.length; i++) {
			winListX[i] = -1;
		}
		for (int i = 0; i < winListY.length; i++) {
			winListY[i] = -1;
		}

//		printBoard();
	}

	public void printBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[j][i] == EMPTY ? "-" : board[j][i] == X ? "X" : "O");
			}
			System.out.println();
		}
	}

	public void printInfo(Point p) {
		System.out.println("X: " + p.getX());
		System.out.println("Y: " + p.getY());
		System.out.print("turn: ");
		System.out.println(current == X ? "X" : "O");
	}

	public void printWinList() {
		for (int i = 0; i < winListX.length; i++) {
			System.out.println("(" + winListX[i] + ", " + winListY[i] + ")");
		}
	}

	public int checkWin() {
//		printBoard();
		for (int i = 0; i < board.length; i++) {
			if (board[i][0] != -1 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
				for (int j = 0; j < winListX.length; j++) {
					winListX[j] = j;
					winListY[j] = i;
				}
//				System.out.println("winList: ");
//				printWinList();
				return board[i][0];
			}
		}
//		printWinList();
		for (int i = 0; i < board.length; i++) {
			if (board[0][i] != -1 && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
				for (int j = 0; j < winListX.length; j++) {
					winListX[j] = j;
					winListY[j] = i;
				}
				return board[0][i];
			}
		}
		if (board[0][0] != -1 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
			for (int j = 0; j < winListX.length; j++) {
				winListX[j] = j;
				winListY[j] = j;
			}
			return board[0][0];
		}
		if (board[2][0] != -1 && board[2][0] == board[1][1] && board[1][1] == board[0][2]) {
			for (int j = 0; j < winListX.length; j++) {
				winListX[j] = 0 + j;
				winListY[j] = 2 - j;
			}
			return board[2][0];
		}
//		printWinList();
		return -1;
	}

	public void restart() {
		frame.dispose();
		new TicTacToe();
	}

	class GamePanel extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(new BasicStroke(5.0f));
			g2d.drawLine(0, panel.getHeight() / 3, panel.getWidth(), panel.getHeight() / 3);
			g2d.drawLine(0, 2 * panel.getHeight() / 3, panel.getWidth(), 2 * panel.getHeight() / 3);

			g2d.drawLine(panel.getWidth() / 3, 0, panel.getWidth() / 3, panel.getHeight());
			g2d.drawLine(2 * panel.getWidth() / 3, 0, 2 * panel.getWidth() / 3, panel.getHeight());

//			System.out.println();
//			if (gameOver) {
//				printWinList();
//			}

//			if (current == X) {
//				g2d.drawLine(m.getMousePos().getX() / panel.getWidth() / 3,
//						m.getMousePos().getY() / panel.getHeight() / 3,
//						m.getMousePos().getX() / panel.getWidth() * (i + 1), panel.getHeight() / 3 * (j + 1));

//				g2d.drawLine(panel.getWidth() / 3 * (i + 1), j * panel.getHeight() / 3, i * panel.getWidth() / 3,
//						panel.getHeight() / 3 * (j + 1));
//			} else {
//				g2d.drawOval(7 + (int) m.getMousePos().getX() * panel.getWidth() / 3,
//						7 + (int) m.getMousePos().getY() * panel.getHeight() / 3, panel.getWidth() / 3 - 15,
//						panel.getHeight() / 3 - 15);
//			}

			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					g2d.setColor(Color.BLACK);
//					System.out.println("i: " + i);
//					System.out.println("j: " + j);
//					System.out.println("row: " + winListY[i]);
//					System.out.println("col: " + winListX[i]);
//					if (i == winListY[i] && j == winListX[i]) {
//						System.out.println("match found at: (" + winListX[i] + ", " + winListY[i] + ")");
//						g2d.setColor(Color.RED);
//					}
					if (board[i][j] == -1) {
						// don't draw anything
					} else if (board[i][j] == 0) {
						// o
						g2d.drawOval(7 + i * panel.getWidth() / 3, 7 + j * panel.getHeight() / 3,
								panel.getWidth() / 3 - 15, panel.getHeight() / 3 - 15); // square width
					} else if (board[i][j] == 1) {
						// x
						g2d.drawLine(i * panel.getWidth() / 3, j * panel.getHeight() / 3,
								panel.getWidth() / 3 * (i + 1), panel.getHeight() / 3 * (j + 1));

						g2d.drawLine(panel.getWidth() / 3 * (i + 1), j * panel.getHeight() / 3,
								i * panel.getWidth() / 3, panel.getHeight() / 3 * (j + 1));
					}
				}
			}
		}
	}

	class Mouse implements MouseListener, MouseMotionListener {
		Object[] options = { "Yes", "No" };
		Point mPos;
		int turns;
		int ans;

		public Mouse() {
			mPos = new Point(0, 0);
			gameOver = false;
			turns = 0;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (turns >= 9 || gameOver) {
				return;
			}
			int x = 3 * e.getX() / panel.getWidth();
			int y = 3 * e.getY() / panel.getHeight();

//			printInfo(e.getPoint());

			if (board[x][y] == -1) {
				board[x][y] = current;
				turns++;
				int winner = checkWin();
				if (winner == X) {
					panel.repaint();
					ans = JOptionPane.showOptionDialog(null, "X WON!\nPlay again?", "Winner!",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]); // option
					gameOver = true;
					if (ans == 0) {
						restart();
					}
				} else if (winner == O) {
					panel.repaint();
					ans = JOptionPane.showOptionDialog(null, "O WON!\nPlay again?", "Winner!",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]); // option
					gameOver = true;
					if (ans == 0) {
						restart();
					}
				} else if (turns == 9) {
					// cat's game
					panel.repaint();
					ans = JOptionPane.showOptionDialog(null, "Cat's game\nPlay again?", "Cat.",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]); // option
					gameOver = true;
					if (ans == 0) {
						restart();
					}
				}
				if (current == O) {
					current = X;
				} else if (current == X) {
					current = O;
				}
			}
//			printBoard();
			panel.repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mPos = e.getPoint();
		}

		public Point getMousePos() {
			return mPos;
		}

	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame.setDefaultLookAndFeelDecorated(true);
			new TicTacToe();
		});
	}
}

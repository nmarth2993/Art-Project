//I could re-code some of this but keep most of the previous code to make it more clean.
//Consider doing it, as it would be easier to work with.
//Do save/save As soon! It should be somewhat simple (have a File reference in memory)
//Opening images also shouldn’t be so bad I think.

/*
 * Nicholas Marthinuss
 * https://github.com/nmarth2993
 * 3/1/19
 * Drawing Application
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

/*
 *
 *
 *
 *
 * Things to include:
 * menu:
 *         in the menu: input size
 *         color picker
 *         animation types
 *             gradients, more types of paints idk
 *             speed
 *             colors (JColorChooser)
 *
 * hide/show menu button
 * save drawing to a file? how do dis?? -- Robot class
 *
 *
 *
 *
 */

/*
 * Main Project:
When the program is run, the user will see a blank canvas, created using java graphics
(JPanel). There will be a menu button in the upper left corner that, when clicked, will show
buttons to control attributes about the brush. Among the buttons will be a color chooser, size
chooser, and opacity control. The user will be able to drag the mouse to draw on the canvas
with the current brush attributes. The user will also be able to scroll to change the size of the
brush. Edit options will also be available to undo the last stroke painted.
Enhancement 1:
Different brush shapes and the ability to add common shapes will be available in the settings
menu.
Enhancement 2:
File functionality will be implemented to allow users to upload an image to draw on. The drawing
may also be saved to an image file
 */

//XXX: REFERENCES TO LOOK INTO:
//https://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html

//TODO list (all of the comments from this line to the first line of code)
//1. BrushStroke class
//2. Different brush shapes
//3. insert shapes
//4. Image handling (in/out)
//5. More stuff (idk)

//TODO: Add text support that allows size, color choices

/*
TODO: setting size:
    pressing the menu item to set size will open a new panel with a textbox and a slider. the textbox and the slider
    are linked (they both update with each other) a button at the bottom will submit the choice of the size.
*/
//TODO: Add eraser:
//		arrayList of marks that are always the same color as the background

//XXX: gradients maybe at the end if I have time

public class ArtTESTSPACE {
	JFrame frame;
	RainbowPanel panel;
	MouseMove mouseTrack;
	SizeListener sizeListener;
	JMenuBar menubar;

	JMenu file;
	JMenuItem newFile;
	JMenuItem open;
	JMenuItem save;
	JMenuItem saveAs;
	JMenuItem exit;

	JMenu options;
	JMenuItem size;
	JMenuItem brushType;
	JMenuItem color;
	JMenuItem bgColor;

	JMenu edit;
	JMenuItem undo;
	JMenuItem redo;

	JMenu about;
	JMenuItem aboutme;

	ArrayList<Mark> markList;

	/*
	 * JMenuItem[] fileMenuItems; JMenuItem[] optionMenuItems; JMenuItem[]
	 * editMenuItems; JMenuItem[] aboutMenuItems;
	 *
	 * JMenu[] allMenus; JMenuItem[][] allMenuItems;
	 */

	/*
	 * JMenuItem[] fileMenuItems = { newFile, open, save, saveAs, exit };
	 * JMenuItem[] optionMenuItems = { size, brushType, color }; JMenuItem[]
	 * editMenuItems = { undo, redo }; JMenuItem[] aboutMenuItems = { aboutme };
	 *
	 * JMenu[] allMenus = { file, option, edit, about }; JMenuItem[][] allMenuItems
	 * = { fileMenuItems, optionMenuItems, editMenuItems, aboutMenuItems };
	 */

	/*
	 * final JMenu[] menus = { file, option, edit, about }; final String[]
	 * menuTitles = { "File", "Option", "Edit", "About" }; final JMenuItem[]
	 * fileMenuItems = { newFile, open, save, saveAs, exit }; final String[]
	 * fileMenuItemTitles = { "New", "Open", "Save", "Save As...", "Exit" }; final
	 * JMenu[] EDITMENUREFERENCES HERE; final String[] editMenuItems = { "Undo",
	 * "Redo" };
	 * 
	 * for (int i = 0; i < menus.length; i++) { // this initializes each menu to
	 * it's name menus[i] = new JMenu(menuTitles[i]); menubar.add(menus[i]); }
	 * because I think just the array references are set to the new object, not the
	 * object at the index, investigate later for (int i = 0; i <
	 * fileMenuItems.length; i++) { fileMenuItems[i] = new
	 * JMenuItem(fileMenuItemTitles[i]); file.add(fileMenuItems[i]); }
	 * 
	 * 
	 * it may have something to do with the declaration of the arrays as final?
	 */

	public ArtTESTSPACE() {

		frame = new JFrame();
		mouseTrack = new MouseMove();
		panel = new RainbowPanel();
		sizeListener = new SizeListener();

		createMenus();
		panel.setBackground(Color.WHITE);
		panel.addMouseMotionListener(mouseTrack);
		panel.addMouseWheelListener(mouseTrack);
		panel.addMouseListener(mouseTrack);

		frame.setPreferredSize(new Dimension(500, 500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		frame.setJMenuBar(menubar);
		frame.pack();
		frame.setVisible(true);

	}

	private void createMenus() {
		menubar = new JMenuBar();

		file = new JMenu("File");
		newFile = new JMenuItem("New");
		open = new JMenuItem("Open");
		save = new JMenuItem("Save");
		saveAs = new JMenuItem("Save As...");
		exit = new JMenuItem("Exit");

		options = new JMenu("Options");
		size = new JMenuItem("Size");
		brushType = new JMenuItem("Brush type");
		color = new JMenuItem("Brush color");
		bgColor = new JMenuItem("Background color");

		edit = new JMenu("Edit");
		undo = new JMenuItem("Undo");
		redo = new JMenuItem("Redo");

		about = new JMenu("About");
		aboutme = new JMenuItem("About");

		// TODO: using one actionListener for each menu like below

		FileMenuListener fmListen = new FileMenuListener();
		newFile.addActionListener(fmListen);
		newFile.setActionCommand("new");
		open.addActionListener(fmListen);
		open.setActionCommand("open");
		save.addActionListener(fmListen);
		save.setActionCommand("save");
		saveAs.addActionListener(fmListen);
		saveAs.setActionCommand("saveAs");
		exit.addActionListener(fmListen);
		exit.setActionCommand("exit");

		OptionsMenuListener optionListen = new OptionsMenuListener();
		size.addActionListener(sizeListener);
		brushType.addActionListener(optionListen);
		brushType.setActionCommand("brush");
		color.addActionListener(optionListen);
		color.setActionCommand("color");
		bgColor.addActionListener(optionListen);
		bgColor.setActionCommand("bgcolor");

//        aboutme.addActionListener(new AboutListener());
//        exit.addActionListener(new ExitListener());
//        for (int i = 0; i < file.getMenuComponentCount(); i++) {
//        
//        }

		// create an array here that contains all of the objects... later

		file.add(newFile);
		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.add(exit);

		options.add(size);
		options.add(brushType);
		options.add(color);
		options.add(bgColor);

		edit.add(undo);
		edit.add(redo);

		about.add(aboutme);

		menubar.add(file);
		menubar.add(options);
		menubar.add(edit);
		menubar.add(about);

//        for (int i = 0; i < fileMenuItems.length; i++) {
//            System.out.println(fileMenuItems[i]);
//        }

		/*
		 * for (int i = 0; i < allMenuItems.length; i++) { for (int j = 0; j <
		 * allMenuItems[i].length; j++) { try { allMenus[i].add(allMenuItems[i][j]); }
		 * catch (Exception e) { e.printStackTrace(); } } }
		 */

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame.setDefaultLookAndFeelDecorated(true);
				new ArtTESTSPACE();
			}
		});
	}

	// TODO: examing having one action listener for all or each one separate or one
	// to handle certain groups that are similar (save/save as)

	class FileMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("new")) {
				mouseTrack.clear();
				panel.setBackground(Color.WHITE);
			}
			System.out.println(command);
		}

	}

	class OptionsMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("brush")) {
				System.out.println("brushType");
			} else if (e.getActionCommand().equals("color")) {
				Color c = JColorChooser.showDialog(panel, "Choose a color", null);
				panel.setBrushColor(c);
			} else if (e.getActionCommand().equals("bgcolor")) { // TODO: add a background color changer to the menu as
				// well
				Color bg = JColorChooser.showDialog(panel, "Choose a background color", null);
				panel.setBackground(new Color(bg.getRGB()));
			}
		}
	}

	/*
	 * class AboutListener implements ActionListener {
	 *
	 * JFrame frame; JPanel panel; JLabel label;
	 *
	 * @Override public void actionPerformed(ActionEvent e) {
	 * System.out.println(e.getSource()); frame = new JFrame("About"); panel = new
	 * JPanel(); label = new JLabel("This is some info"); panel.add(label);
	 * frame.setContentPane(panel); frame.setPreferredSize(new Dimension(150, 150));
	 * frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	 * frame.setVisible(true); frame.pack();
	 *
	 * }
	 *
	 * }
	 *
	 * class ExitListener implements ActionListener {
	 *
	 * @Override public void actionPerformed(ActionEvent e) {
	 * frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); }
	 *
	 * }
	 */

	class SizeListener implements ActionListener {
		JFrame sizeFrame;
		JPanel sizePanel;
		JTextField sizeInput;
		JLabel sizePrompt;
		JSlider slider;
		int size = mouseTrack.getDiameter();

		@Override
		public void actionPerformed(ActionEvent e) {
			if (sizeFrame == null) {
				KeyListener keyListener = new keyListener();
				sizeFrame = new JFrame();
				sizePanel = new JPanel();
				sizeInput = new JTextField("" + size, 5);
				slider = new JSlider(SwingConstants.HORIZONTAL, MouseMove.sizeMin, MouseMove.sizeMax / 2,
						mouseTrack.getDiameter());
				sizePrompt = new JLabel("Enter the size: ");

				slider.addChangeListener(new SliderListener());

				sizeFrame.addWindowListener(new WindowListen());
				sizeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				sizeInput.addKeyListener(keyListener);
				sizeFrame.addKeyListener(keyListener);
				slider.addKeyListener(keyListener);

				sizePanel.add(sizePrompt);
				sizePanel.add(sizeInput);
				sizePanel.add(slider);
				sizeFrame.setContentPane(sizePanel);

				sizeFrame.setLocation(10, 10);

				sizeFrame.setPreferredSize(new Dimension(200, 100));
				sizeFrame.pack();
				sizeFrame.setVisible(true);
//				System.out.println(sizeFrame.getHeight());
//				System.out.println("(" + sizeFrame.getPreferredSize().getWidth() + ", "
//						+ sizeFrame.getPreferredSize().getHeight() + ")");
			}
		}

		class keyListener implements KeyListener {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						int s = Integer.parseInt(sizeInput.getText());
						size = s;
					} catch (NumberFormatException exception) {
						System.out.println("Invalid option");
					}
					mouseTrack.setDiameter(size);
					sizeFrame.dispose();
//					System.out.println(sizeFrame.getContentPane());
//					System.out.println("(" + sizeFrame.getPreferredSize().getWidth() + ", "
//							+ sizeFrame.getPreferredSize().getHeight() + ")");
					sizeFrame = null;
					System.out.println(size);
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

		}

		public int getUserSize() {
			return size;
		}

		class WindowListen implements WindowListener {

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				sizeFrame.dispose();
				sizeFrame = null;
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}

		}

		class SliderListener implements ChangeListener {

			@Override
			public void stateChanged(ChangeEvent e) {
				sizeInput.setText("" + slider.getValue());
			}

		}

	}

	class RainbowPanel extends JPanel {

		final int sleepVal = 5;
		Color c1; // this was used for gradients, may phase out later if gradients aren't included
					// in final
		Color brushColor;
		Color bgColor;
		int x = 0;
		int y = 300;

		int circleWidth = 30;
		int circleHeight = 30;

		Point mousePos;

		int mouseScroll;

		public void setBrushColor(Color c) {
			brushColor = c;
		}

		public Color getBrushColor() {
			return brushColor;
		}

		public RainbowPanel() {
			mousePos = new Point(0, 0);
			mouseScroll = 0;
			RGBColor();
			animate();
			mouseWheel();
			brushColor = new Color(0, 0, 0);
//			gradient();
//            diag();
		}

		public void diag() {
			new Thread() {
				public void run() {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for (;;) {
						System.out.println("cWidth: " + circleWidth);
						System.out.println("diameter: " + mouseTrack.getDiameter());
						System.out.println("userSize: " + sizeListener.getUserSize());
					}
				}
			}.start();
		}

		public void animate() {
			Thread animate = new Thread() {
				public void run() {
					for (;;) {
						try {
							Thread.sleep(10); // REFRESH RATE: 10ms
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						repaint();
					}
				}
			};
			animate.start();
		}

		public void mouseWheel() { // XXX: Added thread names for profiling
			Thread mouseWheel = new Thread() {
				public void run() {
					for (;;) {
						mouseScroll = mouseTrack.getMouseScroll();
//                        System.out.println(mouseScroll);
						if (mouseScroll != 0) {
							if (circleWidth - mouseScroll < 3) {
								circleWidth = 3;
							} else if (circleWidth + mouseScroll > 100) {
								circleWidth = 100;
							} else {
								circleWidth += mouseScroll;
							}
							circleHeight = circleWidth;
						}
					}
				}
			};
			mouseWheel.start();
		}

		public void gradient() {
			Thread gradient = new Thread() {
				public void run() {
					for (;;) {
						for (int i = 0; i < 200; i++) {
							x++;
							y--;
							try {
								Thread.sleep(sleepVal);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						for (int i = 0; i < 200; i++) {
							x++;
							y++;
							try {
								Thread.sleep(sleepVal);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						for (int i = 0; i < 200; i++) {
							x--;
							y++;
							try {
								Thread.sleep(sleepVal);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						for (int i = 0; i < 200; i++) {
							x--;
							y--;
							try {
								Thread.sleep(sleepVal);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}

			};
			gradient.start();
		}

		public void RGBColor() {
			new Thread() {
				public void run() {
					int red = 255;
					int blue = 0;
					int green = 0;
					for (;;) {
						for (int i = 0; i < 255; i++) {
							green++;
							c1 = new Color(red, green, blue);
							try {
								Thread.sleep(sleepVal);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						for (int i = 0; i < 255; i++) {
							red--;
							c1 = new Color(red, green, blue);
							try {
								Thread.sleep(sleepVal);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						for (int i = 0; i < 255; i++) {
							blue++;
							c1 = new Color(red, green, blue);
							try {
								Thread.sleep(sleepVal);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						for (int i = 0; i < 255; i++) {
							green--;
							c1 = new Color(red, green, blue);
							try {
								Thread.sleep(sleepVal);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						for (int i = 0; i < 255; i++) {
							red++;
							c1 = new Color(red, green, blue);
							try {
								Thread.sleep(sleepVal);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						for (int i = 0; i < 255; i++) {
							blue--;
							c1 = new Color(red, green, blue);
							try {
								Thread.sleep(sleepVal);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}.start();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

//			 below is the gradient code, removed due to performance issues

			/*
			 * GradientPaint paint; int colorPattern = mouseTrack.getColorPattern(); if
			 * (colorPattern == 1) { paint = new GradientPaint(x, y, c1, x + 300, y + 100,
			 * Color.BLACK); } else if (colorPattern == 2) { paint = new GradientPaint(x, y,
			 * Color.BLACK, x + 300, y + 100, c1); } else if (colorPattern == 3) { paint =
			 * new GradientPaint(x, y, c1, x + 300, y + 100, Color.WHITE); } else { paint =
			 * new GradientPaint(x, y, Color.WHITE, x + 300, y + 100, c1); }
			 * g2d.setPaint(paint);
			 */

			for (Mark m : markList) {
				g2d.setColor(m.getColor());
				g2d.fillOval(m.getX() - (m.getWidth() / 2), m.getY() - (m.getHeight() / 2), m.getWidth(),
						m.getHeight());
			}

			circleWidth = mouseTrack.getDiameter();
			circleHeight = circleWidth;
			g2d.setColor(brushColor);
			g2d.fillOval((int) mouseTrack.getMouseX() - (circleWidth / 2),
					(int) mouseTrack.getMouseY() - (circleHeight / 2), circleWidth, circleHeight);
		}
	}

	/*
	 * TODO: for gradient painting... take the min/max x and y values and then yeet
	 * it
	 */

	class MouseMove implements MouseMotionListener, MouseWheelListener, MouseListener {
		Point mousePos;
		final static int sizeMin = 2;
		final static int sizeMax = 500;
		int scroll;
		int diameter;
		int colorPattern;

		final int patternNumber = 4;

		public MouseMove() {
			mousePos = new Point(-200, -200); // start offscreen
			scroll = 0;
			diameter = 30;
			colorPattern = 1;

			markList = new ArrayList<Mark>();

//			pointList = new ArrayList<Point>();
//			sizeList = new ArrayList<Integer>();
		}

		public void addMark(MouseEvent e) {
			markList.add(new Mark(e.getPoint(), new Dimension(diameter, diameter), panel.getBrushColor()));
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			mousePos = new Point(e.getX(), e.getY());
			addMark(e);
//			System.out.println(markList.size());
//			mousePos = e.getPoint();
//			markList.add(new Mark(mousePos, new Dimension(diameter, diameter), markColor));
//                    new Point(arg0.getX(), arg0.getY());
//			pointList.add(mousePos);
//			sizeList.add(diameter);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO: examine this line. (first drawn mark when dragged is not re-drawn when
			// the mousePos changed when released)
			mousePos = new Point(e.getX(), e.getY());
		}

		public int getMouseX() {
			return (int) mousePos.getX();
		}

		public int getMouseY() {
			return (int) mousePos.getY();
		}

		public ArrayList<Mark> getMarkList() {
			return markList;
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			scroll = -(e.getWheelRotation());

			if (diameter + scroll < sizeMin) {
				diameter = sizeMin;
			} else {
				diameter += scroll;
			}

		}

		public int getMouseScroll() {
			return scroll;
		}

		public void resetMouseScroll() {
			scroll = 0;
		}

		public void setDiameter(int d) {
			if (d < sizeMin) {
				d = sizeMin;
			}
			diameter = d;
		}

		public int getDiameter() {
			return diameter;
		}

		public void incrementColorPattern() {
			if (colorPattern + 1 > patternNumber) {
				colorPattern = 1;
			} else {
				colorPattern++;
			}
		}

		public int getColorPattern() {
			return colorPattern;
		}

		public void clear() {
			markList.removeAll(markList);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			mousePos = new Point(e.getX(), e.getY());
			if (e.getButton() == MouseEvent.BUTTON1) {
				addMark(e);
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				incrementColorPattern();
			} else if (e.getButton() == MouseEvent.BUTTON2) {
				clear();
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

	}
}

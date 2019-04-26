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

//TODO list
//include option to set background color (fill tool?)
//1. implement color chooser. this will also serve as opacity control (JColorChooser)
//2. Examine data structure and viability of making a BrushStroke class that has an arrayList of points
/*     or... just don't repaint the panel?? is this a good idea? no. it's not. it's jank as hell */
//3. Different brush shapes
//4. insert shapes
//5. Image handling (in/out)
//6. More stuff (idk)

//let's knock some tasks out!

//TODO: JColorChooser transparency doesn't work???

//XXX
//TODO: clean up the code and handle data structure better. brushStroke class with a point, size, and stroke type
//TODO: synchronize the drawing of the shapes to avoid the 'lag' when moving mouse quickly
//		https://docs.oracle.com/javase/tutorial/uiswing/concurrency/
//TODO: Add text support that allows size, color choices

//XXX

/*
setting size:
    pressing the menu item to set size will open a new panel with a textbox and a slider. the textbox and the slider
    are linked (they both update with each other) a button at the bottom will submit the choice of the size.
*/

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

	Color markColor;

//	ArrayList<Point> pointList;
//	ArrayList<Integer> sizeList;

	ArrayList<Mark> markList;
//	XXX: revisit: ArrayList<Stroke> strokeList;

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

//    final JMenu[] menus = { file, option, edit, about };
//    final String[] menuTitles = { "File", "Option", "Edit", "About" };
//    final JMenuItem[] fileMenuItems = { newFile, open, save, saveAs, exit };
//    final String[] fileMenuItemTitles = { "New", "Open", "Save", "Save As...", "Exit" };
//    final JMenu[] EDITMENUREFERENCES HERE;
//    final String[] editMenuItems = { "Undo", "Redo" };

//    for (int i = 0; i < menus.length; i++) { // this initializes each menu to it's name
//    menus[i] = new JMenu(menuTitles[i]);
//    menubar.add(menus[i]);
//}
// beyond this point got real messy, real fast so I just noped out... might
// revisit later
// because I think just the array references are set to the new object, not the
// object at the index
//for (int i = 0; i < fileMenuItems.length; i++) {
//    fileMenuItems[i] = new JMenuItem(fileMenuItemTitles[i]);
//    file.add(fileMenuItems[i]);
//}

	public ArtTESTSPACE() {

		frame = new JFrame();
		mouseTrack = new MouseMove();
		panel = new RainbowPanel();
		sizeListener = new SizeListener();

//		markColor = Color.BLACK;

		createMenus();

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
//                sizeList.removeAll(sizeList);
//                pointList.removeAll(pointList);
			}
			System.out.println(command);
		}

	}

	class OptionsMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("color")) {
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
		int size;

		@Override
		public void actionPerformed(ActionEvent e) { // TODO: clean up this code aaaaaaaaaahhhhhhhhhh
			if (sizeFrame == null) {
				KeyListener keyListener = new keyListener();
				sizeFrame = new JFrame();
				sizePanel = new JPanel();
				sizeInput = new JTextField(5);
				sizePrompt = new JLabel("Enter the size: ");

				sizeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				sizeInput.addKeyListener(keyListener);
				sizeFrame.setContentPane(sizePanel);
				sizePanel.add(sizePrompt);
				sizePanel.add(sizeInput);

				sizeFrame.setVisible(true);
				sizeFrame.pack();
			}

//            System.out.println("button clicked, but i'm tired now...");
		}

		class keyListener implements KeyListener {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						size = Integer.parseInt(sizeInput.getText());

						// need upper limit
						// lower limit is the same as before (3 I think)
						// close
					} catch (NumberFormatException exception) {
//                        exception.printStackTrace();
						System.out.println("Invalid option");
						size = 10;
					}
					mouseTrack.setDiameter(size);
					sizeFrame.dispose();
//					System.out.println(sizeFrame.getContentPane());
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

	}

	class RainbowPanel extends JPanel {

		final int sleepVal = 5;
		Color c1;
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
//            gradient();
			mouseWheel();

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

//            GradientPaint paint;
//            int colorPattern = mouseTrack.getColorPattern();
			// below is the gradient code, removed due to performance issues
			/*
			 * if (colorPattern == 1) { paint = new GradientPaint(x, y, c1, x + 300, y +
			 * 100, Color.BLACK); } else if (colorPattern == 2) { paint = new
			 * GradientPaint(x, y, Color.BLACK, x + 300, y + 100, c1); } else if
			 * (colorPattern == 3) { paint = new GradientPaint(x, y, c1, x + 300, y + 100,
			 * Color.WHITE); } else { paint = new GradientPaint(x, y, Color.WHITE, x + 300,
			 * y + 100, c1); } g2d.setPaint(paint);
			 */
///////////////////////////////
//            g2d.setColor(c1); //XXX: temp. disabled color cycling
///////////////////////////////

//            ArrayList<Integer> sizeList = mouseTrack.getSizeList();
//            ArrayList<Point> pointList = mouseTrack.getPointList();

			g2d.setColor(brushColor); // XXX: ADDING THIS PERMANENTLY TO ALLOW CUSTOM COLORS!
			// XXX:
			// XXX
			// XXX
			// TODO:
			// XXX
			// XXX
			// THIS IS SOOOOOOOOO
			// IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!:
			// NEED TO MAKE A CIRCLE CLASS THAT HAS A SIZE AND A COLOR TO BE STORED AND THEN
			// IT DRAWS BASED ON THE COLOR THAT THE CIRCLE HAS STORED

//			Random r = new Random(); // TODO: FOR NOW, using this random to draw each circle a different color
//			for (int i = 0; i < sizeList.size(); i++) {
			// FIXME:
//				brushColor = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)); // TODO: this line of code is to
			// be changed
//				g2d.setColor(brushColor);
			// TODO: SET THE COLOR TO MARK OBJECT .getColor();
//				g2d.fillOval((int) pointList.get(i).getX() - (sizeList.get(i) / 2),
//						(int) pointList.get(i).getY() - (sizeList.get(i) / 2), sizeList.get(i), sizeList.get(i));
//			}
			System.out.println("Size: " + markList.size());
			for (Mark m : markList) {
				System.out.println("(" + m.getX() + ", " + m.getY() + ")");

			}

			for (Mark m : markList) {
				g2d.fillOval(m.getX() - (m.getWidth() / 2), m.getY() - (m.getHeight() / 2), m.getWidth(),
						m.getHeight());
			}

//            for (Point p : mouseTrack.getPointList()) {
			// g2d.drawString("Rainbow", (int) p.getX() - 20, (int) p.getY() + 10);
//                g2d.fillOval((int) p.getX() - (circleWidth / 2), (int) p.getY() - (circleHeight / 2), circleWidth,
//                        circleHeight);
//            }
			// g2d.drawString("Rainbow", (int) mouseTrack.getMouseX() - 20, (int)
			// mouseTrack.getMouseY() + 10);
			circleWidth = mouseTrack.getDiameter();
			circleHeight = circleWidth;

			g2d.fillOval((int) mouseTrack.getMouseX() - (circleWidth / 2),
					(int) mouseTrack.getMouseY() - (circleHeight / 2), circleWidth, circleHeight);
			// mouseTrack.getMouseY() + 10);
		}
	}

	class MouseMove implements MouseMotionListener, MouseWheelListener, MouseListener {
		Point mousePos;
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
			markList.add(new Mark(e.getPoint(), new Dimension(diameter, diameter), markColor));
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			addMark(e);
			System.out.println(markList.size());
//			mousePos = e.getPoint();
//			markList.add(new Mark(mousePos, new Dimension(diameter, diameter), markColor));
//                    new Point(arg0.getX(), arg0.getY());
//			pointList.add(mousePos);
//			sizeList.add(diameter);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
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

			if (diameter + scroll < 3) {
				diameter = 3;
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
			if (d < 3) {
				d = 3;
			} else if (d > 100) {
				d = 100;
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

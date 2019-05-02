/*
 * Nicholas Marthinuss
 * https://github.com/nmarth2993
 * 3/1/19
 * Drawing Application
 */

//I could re-code some of this but keep most of the previous code to make it more clean.
//Consider doing it, as it would be easier to work with.
//Do save/save As soon! It should be somewhat simple (have a File reference in memory)
//Opening images also shouldnâ€™t be so bad I think.

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

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
//https://pavelfatin.com/low-latency-painting-in-awt-and-swing/

//TODO list (all of the comments from this line to the first line of code)
//1. BrushStroke class
//2. Different brush shapes
//3. insert shapes
//4. Image handling (in/out)
//5. More stuff (idk)

//TODO: allow option to remove background image

//TODO: Add text support that allows size, color choices

//TODO: Add eraser:
//		arrayList of marks that are always the same color as the background
//TODO: add 'pencil' tool --> creates lines that connect point to point (polyLine)

/*TODO: examine efficiency of repainting (avoid lag) look at the second reference
 * for low-latency painting (such as getting the bounds that have changed and only
 * re-draw that area)
 */

//XXX: gradients/ maybe at the end if I have time
/*XXX: note that animation won't be saved, as png doesn't support it --> support different
 * file types: 
 * gif: +animation, -transparency
 * png: +transparency, +higher quality, -no animation
 * jpg: -----don't use jpg
 */

public class Art {
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

	File drawingFile;

	FileMenuListener fmListen;

	ArrayList<Mark> markList;

	// TODO: remove this, temporary
	Image img;

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

	public Art() {
		frame = new JFrame("Untitled");
		mouseTrack = new MouseMove();
		panel = new RainbowPanel();
		sizeListener = new SizeListener();

		frame.addWindowListener(new FrameListener());

		createMenus();
		panel.setBackground(Color.WHITE);
		panel.addMouseMotionListener(mouseTrack);
		panel.addMouseWheelListener(mouseTrack);
		panel.addMouseListener(mouseTrack);

		frame.setPreferredSize(new Dimension(500, 500));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setContentPane(panel);
		frame.setJMenuBar(menubar);
		frame.pack();
		frame.setVisible(true);
	}

	private void createMenus() {
		menubar = new JMenuBar();

		file = new JMenu("File");
		file.setMnemonic('f');
		newFile = new JMenuItem("New");
		newFile.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		newFile.setMnemonic('n');
		open = new JMenuItem("Open");
		open.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		open.setMnemonic('o');
		save = new JMenuItem("Save");
		save.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		save.setMnemonic('s');
		saveAs = new JMenuItem("Save As...");
		saveAs.setMnemonic('a');
		exit = new JMenuItem("Exit");
		exit.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
		exit.setMnemonic('x');

		options = new JMenu("Options");
		options.setMnemonic('o');
		size = new JMenuItem("Size");
		size.setMnemonic('s');
		brushType = new JMenuItem("Brush type");
		brushType.setMnemonic('b');
		color = new JMenuItem("Brush color");
		color.setMnemonic('c');
		bgColor = new JMenuItem("Background color");
		bgColor.setMnemonic('g');

		edit = new JMenu("Edit");
		edit.setMnemonic('e');
		undo = new JMenuItem("Undo");
		undo.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
		undo.setMnemonic('u');
		redo = new JMenuItem("Redo");
		redo.setAccelerator(KeyStroke.getKeyStroke("ctrl Y"));
		redo.setMnemonic('r');

		about = new JMenu("About");
		about.setMnemonic('a');
		aboutme = new JMenuItem("About");
		aboutme.setMnemonic('a');

		// TODO: using one actionListener for each menu like below

		fmListen = new FileMenuListener(); // making this listener class var to allowing saving for all subclasses
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

		EditMenuListener editListener = new EditMenuListener();
		undo.addActionListener(editListener);
		undo.setActionCommand("undo");
		redo.addActionListener(editListener);
		redo.setActionCommand("redo");

		AboutListener aboutListener = new AboutListener();
		aboutme.addActionListener(aboutListener);

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
	}

	// XXX: Main:
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException ex) {
				ex.printStackTrace();
			}
			new Art();
		});
	}

	// TODO: examing having one action listener for all or each one separate or one
	// to handle certain groups that are similar (save/save as)

	class FrameListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			if (mouseTrack.isChanged()) {
				int choice = fmListen.promptSave();
				if (choice == 0) {
					fmListen.save();
				} else if (choice == 1) {

				} else {
					return;
				}
			} else {
				System.exit(1);
			}
			System.exit(1);
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}

	}

	class FileMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("new")) {
				if (mouseTrack.isChanged()) {
					int choice = promptSave();
					if (choice == 0) {
						save();
					} else if (choice == 1) {
						// do nothing, but run the code at the end
					} else {
						return;
					}
				}
				mouseTrack.clear();
				panel.setBackground(Color.WHITE);
				mouseTrack.setChanged(false);
				drawingFile = null;
				img = null;
			}

			else if (command.equals("open")) {
				// TODO: finish this action:

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("C:/Users/" + System.getProperty("user.name") + "/Desktop"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif",
						"wbmp", "bmp", "tif", "tiff");
				fileChooser.setFileFilter(filter);
				fileChooser.showOpenDialog(frame);
				if (mouseTrack.isChanged()) {
					actionPerformed(new ActionEvent(this, 99, "new")); // meh
				}
				try {
					File f = fileChooser.getSelectedFile();
//					if (f != null) {
					try {
						img = ImageIO.read(f);
						drawingFile = fileChooser.getSelectedFile();
					} catch (NullPointerException e1) {
						e1.printStackTrace();
					}
//					}
				} catch (IOException | NullPointerException e2) {
					System.out.println("Error opening file");
				}
				mouseTrack.clear();
			}

			else if (command.equals("save")) {
				// XXX: the next line is for testing, remove in later versions:
				save();

			} else if (command.equals("saveAs")) {
				selectFile();
				if (drawingFile != null) {
					save();
				}

			} else if (command.equals("exit")) {
				if (mouseTrack.isChanged()) {
					int choice = promptSave();
					if (choice == 0) {
						save();
					} else if (choice == 1) {

					} else {
						return;
					}
				}
				System.exit(1);
			}
		}

		public int promptSave() {
			Object[] options = { "Yes", "No", "Cancel" };
			int choice = JOptionPane.showOptionDialog((Component) frame, (Object) "Do you want to save?",
					"Unsaved work will be lost", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
					options, options[0]);
			return choice;
		}

		public void selectFile() {
			JFileChooser fileChooser = new JFileChooser();
			// XXX: using ArtPictures for now to organize, may change in final release (or
			// create that dir)
			fileChooser.setCurrentDirectory(
					new File("C:/Users/" + System.getProperty("user.name") + "/Desktop/ArtPictures"));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG files", "png");
			fileChooser.setFileFilter(filter);
			int choice = fileChooser.showSaveDialog(frame);

			if (choice == JFileChooser.APPROVE_OPTION) {
				File f = fileChooser.getSelectedFile();
				if (!f.getPath().endsWith(".png")) {
					drawingFile = new File(f.getPath() + ".png");
				}
			} else {
				// cancel or window closed: (or error, but we will assume no errors)
			}
			// XXX: file may still be null at this point
		}

		public void save() {
			if (drawingFile == null) {
				selectFile();
				if (drawingFile == null) {
					return;
				}
			}

			Point p = mouseTrack.getMousePos(); // allow resetting position after moving off-screen
			mouseTrack.setMousePos(new Point(-1000, -1000)); // move off-screen
			BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(),
					BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics2D g2d = (Graphics2D) image.getGraphics();
			panel.print(g2d);
			mouseTrack.setMousePos(p); // move back to position

			try {
				ImageIO.write(image, "png", drawingFile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			mouseTrack.setChanged(false);
		}

	}

	class OptionsMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("brush")) {
				System.out.println("brushType");
			} else if (command.equals("color")) {
				Color c = JColorChooser.showDialog(panel, "Choose a color", null);
				panel.setBrushColor(c);
			} else if (command.equals("bgcolor")) { // TODO: add a background color changer to the menu!
				Color bg = JColorChooser.showDialog(panel, "Choose a background color", null);
				panel.setBackground(new Color(bg.getRGB()));
			}
		}
	}

	class EditMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("undo")) {

			} else if (command.equals("redo")) {

			}
			System.out.println(command);
		}

	}

	class AboutListener implements ActionListener {

		JFrame frame;
		JPanel panel;
		JLabel label;

		@Override
		public void actionPerformed(ActionEvent e) {
			frame = new JFrame("About");
			panel = new JPanel();
			label = new JLabel("This is some info");
			panel.add(label);
			frame.setContentPane(panel);
			frame.setPreferredSize(new Dimension(150, 150));
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
			frame.pack();
		}

	}

	class SizeListener implements ActionListener {
		JFrame sizeFrame;
		JPanel sizePanel;
		JTextField sizeInput;
		JLabel sizePrompt;
		JSlider slider;
		int size;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (sizeFrame == null) {
				KeyListener keyListener = new keyListener();
				size = mouseTrack.getDiameter();
				sizeFrame = new JFrame();
				sizePanel = new JPanel();
				sizeInput = new JTextField("" + size, 5); // not displaying correct size
				slider = new JSlider(SwingConstants.HORIZONTAL, MouseMove.sizeMin, MouseMove.sizeMax,
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
				sizeFrame.setPreferredSize(new Dimension(220, 100));
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

		Point mousePos;

		int x = 0;
		int y = 300;

		int yeet1 = 0;
		double yeet2 = 0;

		int diameter; // TODO: phase this out later

//		int mouseScroll;

		boolean changed = false;

		public void setBrushColor(Color c) {
			brushColor = c;
		}

		public Color getBrushColor() {
			return brushColor;
		}

		public RainbowPanel() {
			mousePos = new Point(0, 0);
//			RGBColor();
			animate();
//			mouseWheel();
			brushColor = new Color(0, 0, 0);
//			gradient();
//            diag();

			yeet();
		}

//		public void diag() {
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(10);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					for (;;) {
//						System.out.println("cWidth: " + circleWidth);
//						System.out.println("diameter: " + mouseTrack.getDiameter());
//						System.out.println("userSize: " + sizeListener.getUserSize());
//					}
//				}
//			}.start();
//		}

		public void yeet() {
			new Thread() {
				public void run() {
					double angle = 0;
					for (;;) {
//						System.out.println("tan(" + Math.toDegrees(angle) + "):");
						System.out.println(Math.tan(angle));
						System.out.println((int) Math.tan(angle));
						yeet2 = (int) Math.round(Math.tan(angle));
						angle += Math.toRadians(1);
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}.start();
		}

		public void animate() {
			Thread animate = new Thread() {
				public void run() {
					for (;;) {
						String fileName;
						if (drawingFile == null) {
							fileName = "Untitled";
						} else {
							fileName = drawingFile.getName();
						}
						if (mouseTrack.isChanged()) {
							frame.setTitle("*" + fileName);
						} else {
							frame.setTitle(fileName);
						}
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

		/*
		 * public void gradient() { Thread gradient = new Thread() { public void run() {
		 * for (;;) { for (int i = 0; i < 200; i++) { x++; y--; try {
		 * Thread.sleep(sleepVal); } catch (InterruptedException e) {
		 * e.printStackTrace(); } } for (int i = 0; i < 200; i++) { x++; y++; try {
		 * Thread.sleep(sleepVal); } catch (InterruptedException e) {
		 * e.printStackTrace(); } } for (int i = 0; i < 200; i++) { x--; y++; try {
		 * Thread.sleep(sleepVal); } catch (InterruptedException e) {
		 * e.printStackTrace(); } } for (int i = 0; i < 200; i++) { x--; y--; try {
		 * Thread.sleep(sleepVal); } catch (InterruptedException e) {
		 * e.printStackTrace(); } } } }
		 * 
		 * }; gradient.start(); }
		 * 
		 * public void RGBColor() { new Thread() { public void run() { int red = 255;
		 * int blue = 0; int green = 0; for (;;) { for (int i = 0; i < 255; i++) {
		 * green++; c1 = new Color(red, green, blue); try { Thread.sleep(sleepVal); }
		 * catch (InterruptedException e) { e.printStackTrace(); } } for (int i = 0; i <
		 * 255; i++) { red--; c1 = new Color(red, green, blue); try {
		 * Thread.sleep(sleepVal); } catch (InterruptedException e) {
		 * e.printStackTrace(); } } for (int i = 0; i < 255; i++) { blue++; c1 = new
		 * Color(red, green, blue); try { Thread.sleep(sleepVal); } catch
		 * (InterruptedException e) { e.printStackTrace(); } } for (int i = 0; i < 255;
		 * i++) { green--; c1 = new Color(red, green, blue); try {
		 * Thread.sleep(sleepVal); } catch (InterruptedException e) {
		 * e.printStackTrace(); } } for (int i = 0; i < 255; i++) { red++; c1 = new
		 * Color(red, green, blue); try { Thread.sleep(sleepVal); } catch
		 * (InterruptedException e) { e.printStackTrace(); } } for (int i = 0; i < 255;
		 * i++) { blue--; c1 = new Color(red, green, blue); try {
		 * Thread.sleep(sleepVal); } catch (InterruptedException e) {
		 * e.printStackTrace(); } } } } }.start(); }
		 */

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
//			g2d.setPaint(new GradientPaint(0, 0, Color.RED, 1, (float) yeet2, Color.BLACK, true));
			if (img != null) {
				g2d.drawImage(img, 0, 0, panel.getWidth(), panel.getHeight(), 0, 0, img.getWidth(null),
						img.getHeight(null), panel);
			}

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
//				g2d.setColor(m.getColor()); //XXX: revert this line
				g2d.setPaint(new GradientPaint(0, 0, m.getColor(), 1, (float) yeet2, Color.BLACK, true));
				g2d.fillOval(m.getX() - (m.getWidth() / 2), m.getY() - (m.getHeight() / 2), m.getWidth(),
						m.getHeight());
			}
			diameter = mouseTrack.getDiameter();
			diameter = mouseTrack.getDiameter();
			//
//			g2d.setColor(brushColor); //XXX: revert this line
			g2d.fillOval((int) mouseTrack.getMouseX() - (diameter / 2), (int) mouseTrack.getMouseY() - (diameter / 2),
					diameter, diameter);
		}
	}

	/*
	 * TODO: for gradient painting... take the min/max x and y values and then yeet
	 * it
	 */

	class MouseMove implements MouseMotionListener, MouseWheelListener, MouseListener {
		Point mousePos;
		final static int sizeMin = 2;
		final static int sizeMax = 300;
		final int patterns = 4;

		int scroll;
		int diameter; // change to size and then add shapes with length/width ratios: circle is 1:1,
						// oval 2:1/1:2
		int colorPattern;

		boolean changed;

		public MouseMove() {
			mousePos = new Point(-200, -200); // start offscreen
			scroll = 0;
			diameter = 30;
			colorPattern = 1;
			changed = false;
			markList = new ArrayList<Mark>();

//			pointList = new ArrayList<Point>();
//			sizeList = new ArrayList<Integer>();
		}

		public void addMark(MouseEvent e) {
			markList.add(new Mark(e.getPoint(), new Dimension(diameter, diameter), panel.getBrushColor()));
			changed = true;
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

		public void setMousePos(Point pos) {
			mousePos = pos;
		}

		public Point getMousePos() {
			return mousePos;
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
			} else if (diameter + scroll > sizeMax) {
				diameter = sizeMax;
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
			} else if (d > sizeMax) {
				d = sizeMax;
			} else {
				diameter = d;
			}
		}

		public int getDiameter() {
			return diameter;
		}

		public void incrementColorPattern() {
			if (colorPattern + 1 > patterns) {
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

		public boolean isChanged() {
			return changed;
		}

		public void setChanged(boolean c) {
			changed = c;
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

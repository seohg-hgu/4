package project4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JMenuItem;
import javax.swing.JSpinner;
import javax.swing.JSlider;
import java.awt.Font;

public class Frame extends JFrame {

	private static JPanel Main;
	static BufferedImage img = null; // 버퍼처리된 이미지 객체
	static BufferedImage copyImg = null; // 버퍼처리된 이미지 객체
	static BufferedImage combinedImg = null; // 버퍼처리된 이미지 객체
	static Image newImage;
	static Image changedImage;
	static Color pixelColor;
	static int width = 290;
	static int height = 485;

	static File newFile;
	static FileOutputStream outFileStream;
	static FileInputStream inFileStream;
	static byte[] buffer;
	static int length1;

	static double[][] FILTER = { { -1, -1, -1 }, { -1, 8, -1 }, { -1, -1, -1 } };
	
	static int redGreen = new Color(170,170,85).getRGB();
	static int redBlue = new Color(170,85,170).getRGB();
	static int greenBlue = new Color(85,170,170).getRGB();
	static int black = new Color(0,0,0).getRGB();
	static int white = new Color(255,255,255).getRGB();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 739, 542);
		Main = new JPanel();
		Main.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(Main);
		Main.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(12, 10, 105, 485);
		Main.add(panel);
		panel.setLayout(null);

		JButton save = new JButton("저장");
		save.setBounds(0, 46, 105, 40);
		panel.add(save);

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});

		JButton btnGray = new JButton("Grayscale");
		btnGray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeGray(copyImg);
				Main.repaint();
			}
		});
		btnGray.setBounds(0, 92, 105, 40);
		panel.add(btnGray);

		JButton load = new JButton("불러오기");
		load.setBounds(0, 0, 105, 40);
		panel.add(load);

		JButton btnbrightP = new JButton("밝기+");
		btnbrightP.setBounds(0, 142, 105, 29);
		btnbrightP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				brightPlus();
				Main.repaint();
			}
		});
		panel.add(btnbrightP);

		JButton btnbrightN = new JButton("밝기-");
		btnbrightN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				brightMinus();
				Main.repaint();
			}
		});
		btnbrightN.setBounds(0, 169, 105, 23);
		panel.add(btnbrightN);

		JButton detect = new JButton("edge");
		detect.setBounds(0, 262, 105, 40);
		detect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				detectEdge();
				Main.repaint();
			}
		});
		panel.add(detect);
		
		JButton contrastP = new JButton("대비+");
		contrastP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contrastPlus();
				Main.repaint();
			}
		});
		contrastP.setBounds(0, 202, 105, 29);
		panel.add(contrastP);
		
		JButton contrastN = new JButton("대비-");
		contrastN.setBounds(0, 229, 105, 23);
		contrastN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contrastMinus();
				Main.repaint();
			}
		});
		panel.add(contrastN);
		
		JButton Blue = new JButton("B");
		Blue.setBounds(73, 322, 32, 40);
		Blue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getBlue(copyImg);
				Main.repaint();
			}
		});
		panel.add(Blue);
		
		JButton combine = new JButton("combine");
		combine.setBounds(0, 383, 105, 40);
		combine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				combine();
			}
		});
		panel.add(combine);
		
		JButton btnR = new JButton("R");
		btnR.setFont(new Font("굴림", Font.PLAIN, 9));
		btnR.setBounds(36, 322, 32, 40);
		panel.add(btnR);
		
		JButton Blue_1_1 = new JButton("R");
		Blue_1_1.setFont(new Font("굴림", Font.PLAIN, 7));
		Blue_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		Blue_1_1.setForeground(Color.RED);
		Blue_1_1.setBounds(0, 322, 32, 40);
		panel.add(Blue_1_1);

		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				load();
			}
		});

		DrawImage imagepenel = new DrawImage();
		imagepenel.setBounds(123, 10, 590, 485);
		Main.add(imagepenel);

	}

	void combine() {
		combinedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < copyImg.getWidth(); i++) {
			for (int j = 0; j < copyImg.getHeight(); j++) {
				Color c = new Color(copyImg.getRGB(i, j));
				int red = (int) c.getRed();
				int green = (int) c.getGreen();
				int blue = (int) c.getBlue();
				if(red==255&&green==255&&green==255)
					copyImg.setRGB(i, j, img.getRGB(i, j));
			}
		}
		//newImage = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		Main.repaint();
		//paint both images, preserving the alpha channels
		Graphics g = combinedImg.getGraphics();
		//g.drawImage(img, 0, 0, null);
		//g.drawImage(copyImg, 0, 0, null);

		g.dispose();

		// Save as new image
		try {
			ImageIO.write(combinedImg, "jpg", new File("D:\\new\\new_image.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//newImage =img.getScaledInstance(Frame.width, Frame.height, Image.SCALE_DEFAULT);
		changedImage = copyImg.getScaledInstance(Frame.width, Frame.height, Image.SCALE_DEFAULT);
	}
	public void getRed(BufferedImage image) {
		int width=image.getWidth();
		int height=image.getHeight();
		
		for(int w=0;w<width;w++) {
			for(int h=0;h<width;h++) {
				if(new Color(image.getRGB(w, h)).getGreen()>170) {
					image.setRGB(w, h, white);
				}else if(new Color(image.getRGB(w, h)).getBlue()>170) {
					image.setRGB(w, h, white);
				}else if(image.getRGB(w, h)==greenBlue) {
					image.setRGB(w, h, white);
				}else if(image.getRGB(w, h)!=white) {
					image.setRGB(w, h, black);
				}
			}
		}
	}
	public void getBlue(BufferedImage image) {
		int width=image.getWidth();
		int height=image.getHeight();
		
		for(int w=0;w<width;w++) {
			for(int h=0;h<height;h++) {
				if(new Color(image.getRGB(w, h)).getRed()>170) {
					image.setRGB(w, h, white);
				}else if(new Color(image.getRGB(w, h)).getGreen()>170) {
					image.setRGB(w, h, white);
				}else if(image.getRGB(w, h)==redGreen) {
					image.setRGB(w, h, white);
				}else if(image.getRGB(w, h)!=white) {
					//image.setRGB(w, h, black);
				}
			}
		}
		try {
			ImageIO.write(copyImg, "jpg", newFile);
		}catch(IOException e) {
			e.printStackTrace();
		}
		changedImage = copyImg.getScaledInstance(Frame.width, Frame.height, Image.SCALE_DEFAULT);
		Main.repaint();
	}
	// 이미지 배열로 만들기
	public static double[][] im2ar(BufferedImage bi) {
		double[][] output = new double[bi.getHeight()][bi.getWidth()];
		for (int y = 0; y < bi.getHeight(); y++) {
			for (int x = 0; x < bi.getWidth(); x++) {
				Color c = new Color(bi.getRGB(x, y));
				output[y][x] += c.getRed();
				output[y][x] += c.getGreen();
				output[y][x] += c.getBlue();
				output[y][x] /= 3.0;
			}
		}
		return output;
	}

	public static double[][] getFilter(int size) {
		int x = size / 2;
		double[][] output = new double[size][size];
		double sum = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				output[i][j] = 1.0 / (1 + Math.pow(Math.pow(i - x, 2) + Math.pow(j - x, 2), 0.5));
				sum += output[i][j];
			}
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				output[i][j] /= sum;
			}
		}
		return output;
	}

	public static double[][] convolution(double[][] map, double[][] filter) {
		int c = 0;
		if (filter.length % 2 == 1) {
			int w = filter.length / 2;
			double[][] output = new double[map.length][map[0].length];
			for (int y = 0; y < map.length; y++) {
				for (int x = 0; x < map[y].length; x++) {
					for (int i = 0; i < filter.length; i++) {
						for (int j = 0; j < filter[i].length; j++) {
							try {
								output[y][x] += map[y - i + w][x - j + w] * filter[i][j];
								c++;
							} catch (ArrayIndexOutOfBoundsException e) {

							}
						}
					}
				}
			}
			System.out.println(c + "번의 연산을 하였습니다.");
			return output;
		} else {
			// 필터 크기가 짝수인 경우 무시
			return null;
		}
	}

	public static double[][] arrayInColorBound(double[][] ar) {
		for (int i = 0; i < ar.length; i++) {
			for (int j = 0; j < ar[i].length; j++) {
				ar[i][j] = Math.max(0, ar[i][j]);
				ar[i][j] = Math.min(225, ar[i][j]);
			}
		}
		return ar;
	}

	public static double[][] arrayColorInverse(double[][] ar) {
		for (int i = 0; i < ar.length; i++) {
			for (int j = 0; j < ar[i].length; j++) {
				ar[i][j] = 255 - ar[i][j];
			}
		}
		return ar;
	}

	public static BufferedImage ar2im(double[][] ar) {
		BufferedImage output = new BufferedImage(ar[0].length, ar.length, BufferedImage.TYPE_INT_BGR);
		for (int y = 0; y < ar.length; y++) {
			for (int x = 0; x < ar[y].length; x++) {
				output.setRGB(x, y, new Color((int) ar[y][x], (int) ar[y][x], (int) ar[y][x]).getRGB());
			}
		}
		return output;
	}

	void detectEdge() {
		double[][] ar = im2ar(copyImg);
		double[][] filterBlur = getFilter(3);
		ar = convolution(ar, filterBlur);
		ar = convolution(ar, FILTER);
		ar = arrayInColorBound(ar);
		ar = arrayColorInverse(ar);
		copyImg = ar2im(ar);
		changedImage = copyImg.getScaledInstance(width, height ,Image.SCALE_DEFAULT);
	}

	void contrastPlus() {
		int contrast =2; 
		for (int i = 0; i < copyImg.getWidth(); i++) {
			for (int j = 0; j < copyImg.getHeight(); j++) {
				Color c = new Color(copyImg.getRGB(i, j));
				int red = (int) c.getRed()*contrast;
				int green = (int) c.getGreen()*contrast;
				int blue = (int) c.getBlue()*contrast;

				if (red > 255) { // the values of the color components must be between 0-255
					red = 255;
				} else if (red < 0) {
					red = 0;
				}
				if (green > 255) {
					green = 255;
				} else if (green < 0) {
					green = 0;
				}
				if (blue > 255) {
					blue = 255;
				} else if (blue < 0) {
					blue = 0;
				}
				copyImg.setRGB(i, j, new Color(red, green, blue).getRGB());
			}
		}
		changedImage = copyImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	}
	void contrastMinus() {
		//int brightness = 10; // values from 150 to 200
		int contrast =2; //values from 1.5 to 5.0
		for (int i = 0; i < copyImg.getWidth(); i++) {
			for (int j = 0; j < copyImg.getHeight(); j++) {
				Color c = new Color(copyImg.getRGB(i, j));
				int red = (int) c.getRed()/contrast;
				int green = (int) c.getGreen() /contrast;
				int blue = (int) c.getBlue() /contrast;

				if (red > 255) { // the values of the color components must be between 0-255
					red = 255;
				} else if (red < 0) {
					red = 0;
				}
				if (green > 255) {
					green = 255;
				} else if (green < 0) {
					green = 0;
				}
				if (blue > 255) {
					blue = 255;
				} else if (blue < 0) {
					blue = 0;
				}
				copyImg.setRGB(i, j, new Color(red, green, blue).getRGB());
			}
		}
		changedImage = copyImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	}
	void brightPlus() {
		int brightness = 10; // values from 150 to 200
		// int contrast =2; //values from 1.5 to 5.0
		for (int i = 0; i < copyImg.getWidth(); i++) {
			for (int j = 0; j < copyImg.getHeight(); j++) {
				Color c = new Color(copyImg.getRGB(i, j));
				int red = (int) c.getRed() + brightness;
				int green = (int) c.getGreen() + brightness;
				int blue = (int) c.getBlue() + brightness;

				if (red > 255) { // the values of the color components must be between 0-255
					red = 255;
				} else if (red < 0) {
					red = 0;
				}
				if (green > 255) {
					green = 255;
				} else if (green < 0) {
					green = 0;
				}
				if (blue > 255) {
					blue = 255;
				} else if (blue < 0) {
					blue = 0;
				}
				copyImg.setRGB(i, j, new Color(red, green, blue).getRGB());
			}
		}
		changedImage = copyImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	}

	void brightMinus() {
		int brightness = 10; // values from 150 to 200
		// int contrast =2; //values from 1.5 to 5.0
		for (int i = 0; i < copyImg.getWidth(); i++) {
			for (int j = 0; j < copyImg.getHeight(); j++) {
				Color c = new Color(copyImg.getRGB(i, j));
				int red = (int) c.getRed() - brightness;
				int green = (int) c.getGreen() - brightness;
				int blue = (int) c.getBlue() - brightness;

				if (red > 255) { // the values of the color components must be between 0-255
					red = 255;
				} else if (red < 0) {
					red = 0;
				}
				if (green > 255) {
					green = 255;
				} else if (green < 0) {
					green = 0;
				}
				if (blue > 255) {
					blue = 255;
				} else if (blue < 0) {
					blue = 0;
				}
				copyImg.setRGB(i, j, new Color(red, green, blue).getRGB());
			}
		}
		changedImage = copyImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	}

	void load() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("파일 불러오기");
		fileChooser.setFileFilter(new FileNameExtensionFilter("JPG&GIF Images", "jpg", "gif", "jpeg")); // 파일필터
		fileChooser.setMultiSelectionEnabled(false);// 다중 선택 불가
		int returnVal = fileChooser.showOpenDialog(null); // show openDialog
		if (returnVal == JFileChooser.APPROVE_OPTION) { // 파일을 선택하였을 때
			try {
				loadImage(fileChooser.getSelectedFile().toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void save() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("파일 저장");
		fileChooser.setFileFilter(new FileNameExtensionFilter("JPG&GIF Images", "jpg", "gif", "jpeg")); // 파일필터
		fileChooser.setMultiSelectionEnabled(false); // 다중 선택 불가
		int returnVal = fileChooser.showSaveDialog(this); // show saveDialog
		if (returnVal == JFileChooser.APPROVE_OPTION) { // 파일을 선택하였을 때
			try {
				saveImage(fileChooser.getSelectedFile().toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	static void loadImage(String path) throws Exception {
		// 이미지
		File inFile;
		inFile = new File(path);

		inFileStream = new FileInputStream(inFile.getPath());

		buffer = new byte[1024];
		if(outFileStream==null) {
			newFile = new File("D:\\new\\new_image.jpg");
			outFileStream = new FileOutputStream(newFile);
			while ((length1 = inFileStream.read(buffer)) > 0) {
				outFileStream.write(buffer, 0, length1);
			}
			copyImg = ImageIO.read(newFile);
			Main.repaint();
		}
			

		try {
			img = ImageIO.read(inFile);
			//copyImg = ImageIO.read(newFile);
			Main.repaint();
			/*
			 * imgWidth =img.getWidth(); imgHeight=img.getHeight();
			 */

		} catch (Exception e) {
			System.out.println("no image");
			System.exit(1);// 프로그램 강제 종료
		}

		newImage = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		changedImage = copyImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);

		// displayImage();
	}

	static void saveImage(String path) throws Exception {
		// 이미지
		// File outFile;
		// outFile = new File(path);

		// 파일 스트림
		// FileOutputStream outFileStream;
		outFileStream = new FileOutputStream(newFile);

		// close
		ImageIO.write(copyImg, "jpg", newFile);
		outFileStream.close();

		// messageDialog
		JOptionPane.showMessageDialog(null, "파일 저장 성공", "파일 저장", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void makeGray(BufferedImage img) {
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color c = new Color(img.getRGB(j, i));
				int red = (int) (c.getRed() * 0.299);
				int green = (int) (c.getGreen() * 0.587);
				int blue = (int) (c.getBlue() * 0.114);
				Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
				img.setRGB(j, i, newColor.getRGB());

			}
		}
		changedImage = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	}

	class DrawImage extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(newImage, 0, 0, null);
			g.drawImage(changedImage, 300, 0, null);
		}
	}
}

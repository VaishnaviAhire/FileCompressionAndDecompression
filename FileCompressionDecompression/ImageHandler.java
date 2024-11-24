import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageHandler {
	
	BufferedImage loadImage(String imagePath) {
		BufferedImage img = null;
		try {
	        img = ImageIO.read(new File(imagePath));
	    } 
		catch (IOException e) {
	    	System.out.println("Error: " + e);
	    }
		return img;
	}
	
	BufferedImage createImage(BufferedImage img) {
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		return newImg;
	}
	
	void displayImage(BufferedImage img) {
		JFrame frame = new JFrame();
		JLabel label = new JLabel();
		frame.setSize(img.getWidth(), img.getHeight());
		label.setIcon(new ImageIcon(img));
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	long getImageSize(String imgName) {
		Path path = Paths.get(imgName);
		long bytes = 0;
		try {
			bytes = Files.size(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
}

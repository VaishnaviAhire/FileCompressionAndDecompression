import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;

class Node {
	Color color;
	boolean isLeaf;
	int size;
	int x, y;
	Node next[] = new Node[4];

	Node(int x, int y, int size) {
		color = new Color(0, 0, 0, 0);
		this.x = x;
		this.y = y;
		this.size = size;
		isLeaf = true;
		for(int i = 0; i < 4; i++) {
			next[i] = null;
		}
	}
}

public class QuadTree {
	Node root;
	int colorPercentage;
		
	QuadTree(int size, int colorPercentage) {
		root = new Node(0, 0, size);
		this.colorPercentage = colorPercentage;
	}
	
	// to find average RBG values
	Color getNodeColor(Node node, BufferedImage image) {
		int red = 0;
		int blue = 0;
		int green = 0;
		int alpha = 0;
		int pixelCount = 0;
		Color pixelColor;

		for(int i = node.x; i < node.x + node.size; i++) {
			for(int j = node.y; j < node.y + node.size; j++) {
				pixelColor = new Color(image.getRGB(i, j));
				red += pixelColor.getRed();
				blue += pixelColor.getBlue();
				green += pixelColor.getGreen();
				alpha += pixelColor.getAlpha();
				pixelCount++;
			}
		}
		
		return new Color(red / pixelCount, green / pixelCount, blue / pixelCount, alpha / pixelCount);
	}
	
	//comparing with threshold percentage
	boolean toSplit(Color rootColor, Color pixelColor) {
		boolean retVal = false;
		retVal |= (Math.abs(rootColor.getRed() - pixelColor.getRed()) > (rootColor.getRed() * colorPercentage) / 100);
		retVal |= (Math.abs(rootColor.getBlue() - pixelColor.getBlue()) > (rootColor.getBlue() * colorPercentage) / 100);
		retVal |= (Math.abs(rootColor.getGreen() - pixelColor.getGreen()) > (rootColor.getGreen() * colorPercentage) / 100);
		retVal |= (Math.abs(rootColor.getAlpha() - pixelColor.getAlpha()) > (rootColor.getAlpha() * colorPercentage) / 100);
		return retVal;
	}
	
	//create quadtree
	void create(int x, int y, int dim, Node current, BufferedImage img) {
		current.color = getNodeColor(current, img);
		
		for (int i = x; i < x + dim; i++) {
			for (int j = y; j < y + dim; j++) {
				Color pixelColor = new Color(img.getRGB(i, j));
				if (toSplit(current.color, pixelColor) == true) {
					current.isLeaf = false;
					break;
				}
			}
		}
		
		if (current.isLeaf == true) {
			return;
		}
		
		current.next[0] = new Node(x, y, dim / 2);
		current.next[1] = new Node(x, y + dim / 2, dim / 2);
		current.next[2] = new Node(x + dim / 2, y, dim / 2);
		current.next[3] = new Node(x + dim / 2, y + dim / 2, dim / 2);
		
		create(x, y, dim / 2, current.next[0], img);
		create(x, y + dim / 2, dim / 2, current.next[1], img);
		create(x + dim / 2, y, dim / 2, current.next[2], img);
		create(x + dim / 2, y + dim / 2, dim / 2, current.next[3], img);
	}
	
	//quadtree to image
	void postOrderToImage(Node node, BufferedImage img) {
		if (node == null) {
			return;
		}

		postOrderToImage(node.next[1], img);
		postOrderToImage(node.next[0], img);
		postOrderToImage(node.next[2], img);
		postOrderToImage(node.next[3], img);

		if (node.isLeaf) {
			int red = node.color.getRed();
			int blue = node.color.getBlue();
			int green = node.color.getGreen();
			int alpha = node.color.getAlpha();
			int c = alpha << 24 | red << 16 | green << 8 | blue;
			for(int i = node.x; i < node.x + node.size; i++) {
				for(int j = node.y; j < node.y + node.size; j++) {
					img.setRGB(i, j, c);
				}
			}
		}
	}
	
	// saving compressed image
	BufferedImage storeCompressedImage(BufferedImage img, String imagePath) {
		ImageHandler imgHandler = new ImageHandler();
		BufferedImage compressedImage = null;
		String compressedFileName = null;
		compressedImage = imgHandler.createImage(img);
		postOrderToImage(root, compressedImage);

	    try {
	    	compressedFileName = imagePath.substring(0, imagePath.lastIndexOf('.')) + "_compressed.jpg";
		    File outputfile = new File(compressedFileName);
			ImageIO.write(compressedImage, "jpg", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		System.out.println("Size of compressed image: " + imgHandler.getImageSize(compressedFileName) / 1024 + " KB");
	    return compressedImage;
	}
}
	


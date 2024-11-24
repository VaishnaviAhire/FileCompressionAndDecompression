import java.awt.image.BufferedImage;
import java.util.Scanner;

public class QtreeCompression {
	Scanner sc = new Scanner(System.in);
	void compressImage() {
		System.out.print("Enter path of the file to be compressed: ");
		String imagePath = sc.next();
		System.out.print("Enter percentage of approximation: ");
		int colorPercent = sc.nextInt();
		
		ImageHandler imgHandler = new ImageHandler();
		BufferedImage image = imgHandler.loadImage(imagePath);
		imgHandler.displayImage(image);
		System.out.println("Size of Original image: " + imgHandler.getImageSize(imagePath) / 1024 + " KB");
		
		QuadTree tree = new QuadTree(image.getHeight(), colorPercent);
		tree.create(0, 0, image.getHeight(), tree.root, image);
		BufferedImage compressedImg = tree.storeCompressedImage(image, imagePath);
		if(compressedImg != null) {
			System.out.println("Image compressed successfully!");
			imgHandler.displayImage(compressedImg);
			}
		else {
			System.out.println("Image compression failed!");
		}
	}
}

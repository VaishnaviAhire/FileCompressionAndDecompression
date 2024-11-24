import java.awt.image.BufferedImage;
import java.util.Scanner;

public class Compression {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println();
			System.out.print("Menu:\n1: Huffman algorithm\n2: LZW compression algorithm\n3: QuadTree image compression\n4: Exit\nChoose an option: ");
			int ch = sc.nextInt();
			System.out.println();
			
			switch(ch) {
			case 1:
				HuffmanCompressionDecompression huffmanComp = new HuffmanCompressionDecompression();
				huffmanComp.huffmanCompression();
				break;
				
			case 2:
				LZWCompressionDecompression LZWcomp = new LZWCompressionDecompression();
				LZWcomp.LZWCompression();
				break;
				
			case 3:
				QtreeCompression imgCompression = new QtreeCompression();
				imgCompression.compressImage();
				break;
				
			case 4:
				System.exit(0);
			}
		}
		while(true);
	}
}
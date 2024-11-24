
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class HuffmanCompressionDecompression {

    // Node class to represent each node of the Huffman Tree
    static class Node implements Comparable<Node> {
        char character; // Character data
        int frequency;  // Frequency of the character
        Node left, right;

        Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        Node(int frequency, Node left, Node right) {
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node other) {
            return this.frequency - other.frequency; // Compare based on frequency
        }
    }

    // Build the Huffman tree based on character frequencies
    private Node buildHuffmanTree(Map<Character, Integer> freqMap) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        // Create a node for each character and add to the priority queue
        for (var entry : freqMap.entrySet()) {
            queue.add(new Node(entry.getKey(), entry.getValue()));
        }

        // Build the tree by combining nodes with lowest frequency
        while (queue.size() > 1) {
            Node left = queue.poll();
            Node right = queue.poll();
            Node parent = new Node(left.frequency + right.frequency, left, right);
            queue.add(parent);
        }

        return queue.poll(); // Root of the Huffman Tree
    }

    // Generate the Huffman codes by traversing the tree
    private void generateCodes(Node root, String code, Map<Character, String> huffmanCodes) {
        if (root == null) return;

        // If it's a leaf node, store the Huffman code for that character
        if (root.left == null && root.right == null) {
            huffmanCodes.put(root.character, code);
        }

        // Traverse left and right branches of the tree
        generateCodes(root.left, code + "0", huffmanCodes);
        generateCodes(root.right, code + "1", huffmanCodes);
    }

    // Compress the input text using the Huffman codes
 // Modify the compress method to skip compression if the file is too small
    public String compress(String input, String outputFileName) throws IOException {
        if (input.length() < 100) { // Skip compression for files smaller than 100 characters
            System.out.println("File is too small for compression, saving as-is.");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                writer.write(input);
            }
            return outputFileName;
        }

        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        Node root = buildHuffmanTree(freqMap);
        Map<Character, String> huffmanCodes = new HashMap<>();
        generateCodes(root, "", huffmanCodes);

        // Compress data by writing actual binary bytes
        try (FileOutputStream out = new FileOutputStream(outputFileName);
             ObjectOutputStream metaOut = new ObjectOutputStream(out)) {
            
            metaOut.writeObject(huffmanCodes);  // Write Huffman codes once

            BitSet bitSet = new BitSet();
            int bitIndex = 0;

            // Encode each character in the input using Huffman codes
            for (char c : input.toCharArray()) {
                String code = huffmanCodes.get(c);
                for (char bit : code.toCharArray()) {
                    bitSet.set(bitIndex, bit == '1');
                    bitIndex++;
                }
            }

            byte[] compressedBytes = bitSet.toByteArray();
            out.write(compressedBytes);
        }

        return outputFileName;
    }

   

    // Prints file sizes and compression ratio
    public void printCompressionRatio(String originalFilePath, String compressedFilePath) {
        File originalFile = new File(originalFilePath);
        File compressedFile = new File(compressedFilePath);

        long originalSize = originalFile.length();
        long compressedSize = compressedFile.length();
        double compressionRatio = ((double) compressedSize / originalSize) * 100;

        System.out.println("Original File Size: " + originalSize + " bytes");
        System.out.println("Compressed File Size: " + compressedSize + " bytes");
        System.out.printf("Compression Ratio: %.2f%%\n", compressionRatio);
    }

    // method for handling user input
    public  void huffmanCompression() {
        HuffmanCompressionDecompression huffman = new HuffmanCompressionDecompression();
        Scanner scanner = new Scanner(System.in);

        	System.out.print("Enter the path of the file: ");
        	String filePath = scanner.nextLine();

        	try {
        	        String inputData = new String(Files.readAllBytes(Paths.get(filePath)));
        	        String compressedFileName = filePath.substring(0, filePath.lastIndexOf('.')) + ".huff";
        	        huffman.compress(inputData, compressedFileName);
        	        huffman.printCompressionRatio(filePath, compressedFileName);
        	        System.out.println("File compressed successfully as: " + compressedFileName);
        	    } 
    catch (IOException e) {
        	    System.out.println("File operation error: " + e.getMessage());
        	}
    }
}




/*
OUTPUT 


Enter the path of the file: C:\\Users\\VAISHNAVI AHIRE\\OneDrive\\Desktop\\ESEProject\\DSAProject\\Image1.png
Choose operation - Compress (C) or Decompress (D): c
Original File Size: 22162 bytes
Compressed File Size: 126156 bytes
Compression Ratio: 569.24%
File compressed successfully as: C:\\Users\\VAISHNAVI AHIRE\\OneDrive\\Desktop\\ESEProject\\DSAProject\\Image1.huff


Enter the path of the file: C:\Users\VAISHNAVI AHIRE\OneDrive\Desktop\ESEProject\ClgAss\src\Assignments\Input1.txt
Choose operation - Compress (C) or Decompress (D): c
Original File Size: 27 bytes
Compressed File Size: 393 bytes
Compression Ratio: 1455.56%
File compressed successfully as: C:\Users\VAISHNAVI AHIRE\OneDrive\Desktop\ESEProject\ClgAss\src\Assignments\Input1.huff


Enter the path of the file: C:\Users\VAISHNAVI AHIRE\OneDrive\Desktop\ESEProject\ClgAss\src\Assignments\Input1.huff
Choose operation - Compress (C) or Decompress (D): d
File decompressed successfully as: C:\Users\VAISHNAVI AHIRE\OneDrive\Desktop\ESEProject\ClgAss\src\Assignments\Input1_decompressed.txt





*/


/*
OUTPUT 


EEnter the path of the file: C:\Users\VAISHNAVI AHIRE\OneDrive\Desktop\ESEProject\ClgAss\src\Assignments\Input1.txt
Choose operation - Compress (C) or Decompress (D): c
Original File Size: 2115 bytes
Compressed File Size: 1986 bytes
Compression Ratio: 93.90%
File compressed successfully as: C:\Users\VAISHNAVI AHIRE\OneDrive\Desktop\ESEProject\ClgAss\src\Assignments\Input1.huff



Enter the path of the file: C:\Users\VAISHNAVI AHIRE\OneDrive\Desktop\ESEProject\ClgAss\src\Assignments\Input1.huff
Choose operation - Compress (C) or Decompress (D): d
File decompressed successfully as: C:\Users\VAISHNAVI AHIRE\OneDrive\Desktop\ESEProject\ClgAss\src\Assignments\Input1_decompressed.txt






Enter the path of the file: C:\Users\VAISHNAVI AHIRE\OneDrive\Desktop\ESEProject\ClgAss\src\Assignments\Input1.txt
Choose operation - Compress (C) or Decompress (D): c
File is too small for compression, saving as-is.
Original File Size: 27 bytes
Compressed File Size: 27 bytes
Compression Ratio: 100.00%
File compressed successfully as: C:\Users\VAISHNAVI AHIRE\OneDrive\Desktop\ESEProject\ClgAss\src\Assignments\Input1.huff
Enter the path of the file: 



*/
//APPLICATION OF BOTH DS and File Handling in JAVA
//COMPRESSES TEXT FILES ONLY

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class LZWCompressionDecompression {

    // Compresses the input string data using LZW algorithm and returns an ArrayList of integer codes
    public ArrayList<Integer> compress(String input) {
        HashMap<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dictionary.put("" + (char) i, i);
        }

        String currentPattern = "";
        ArrayList<Integer> compressedData = new ArrayList<>();
        int dictSize = 256;

        for (char symbol : input.toCharArray()) {
            String patternWithSymbol = currentPattern + symbol;
            if (dictionary.containsKey(patternWithSymbol)) {
                currentPattern = patternWithSymbol;
            } else {
                compressedData.add(dictionary.get(currentPattern));
                if (dictSize < 4096) {
                    dictionary.put(patternWithSymbol, dictSize++);
                }
                currentPattern = "" + symbol;
            }
        }

        if (!currentPattern.isEmpty()) {
            compressedData.add(dictionary.get(currentPattern));
        }

        return compressedData;
    }

    // Decompresses the ArrayList of integer codes to retrieve the original string data
    public String decompress(ArrayList<Integer> compressedData) {
        HashMap<Integer, String> dictionary = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dictionary.put(i, "" + (char) i);
        }

        StringBuilder decompressedData = new StringBuilder();
        int dictSize = 256;
        String previousPattern = "" + (char) (int) compressedData.remove(0);
        decompressedData.append(previousPattern);

        for (int code : compressedData) {
            String currentPattern;
            if (dictionary.containsKey(code)) {
                currentPattern = dictionary.get(code);
            } else if (code == dictSize) {
                currentPattern = previousPattern + previousPattern.charAt(0);
            } else {
                throw new IllegalArgumentException("Invalid compressed code: " + code);
            }

            decompressedData.append(currentPattern);

            if (dictSize < 4096) {
                dictionary.put(dictSize++, previousPattern + currentPattern.charAt(0));
            }

            previousPattern = currentPattern;
        }

        return decompressedData.toString();
    }

    // Writes compressed data to a .lzw file
    public void writeCompressedFile(ArrayList<Integer> compressedData, String outputFileName) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(outputFileName))) {
            for (int code : compressedData) {
                out.writeInt(code);
            }
        }
    }

    // Reads compressed data from a .lzw file
    public ArrayList<Integer> readCompressedFile(String inputFileName) throws IOException {
        ArrayList<Integer> compressedData = new ArrayList<>();
        try (DataInputStream in = new DataInputStream(new FileInputStream(inputFileName))) {
            while (in.available() > 0) {
                compressedData.add(in.readInt());
            }
        }
        return compressedData;
    }

    // Reads data from a .txt file
    public String readFile(String inputFileName) throws IOException {
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line).append("\n");
            }
        }
        return data.toString();
    }

    // Writes decompressed data to a .txt file
    public void writeFile(String data, String outputFileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            writer.write(data);
        }
    }

    // Prints the file sizes and compression ratio
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

    // Prints the file sizes and decompression ratio
    public void printDecompressionRatio(String originalFilePath, String decompressedFilePath) {
        File originalFile = new File(originalFilePath);
        File decompressedFile = new File(decompressedFilePath);

        long originalSize = originalFile.length();
        long decompressedSize = decompressedFile.length();
        double decompressionRatio = ((double) decompressedSize / originalSize) * 100;

        System.out.println("Original File Size: " + originalSize + " bytes");
        System.out.println("Decompressed File Size: " + decompressedSize + " bytes");
        System.out.printf("Decompression Ratio: %.2f%%\n", decompressionRatio);
    }

    // Main method to handle user input and perform compression/decompression
    public void LZWCompression() {
        LZWCompressionDecompression lzw = new LZWCompressionDecompression();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the path of the file: ");
        String filePath = scanner.nextLine();

        System.out.print("Choose operation - Compress (C) or Decompress (D): ");
        String choice = scanner.nextLine().toUpperCase();

        try {
            if (choice.equals("C")) {
                // Compression
                String inputData = lzw.readFile(filePath);
                ArrayList<Integer> compressedData = lzw.compress(inputData);

                String compressedFileName = filePath.substring(0, filePath.lastIndexOf('.')) + ".lzw";
                lzw.writeCompressedFile(compressedData, compressedFileName);

                // Print compression ratio
                lzw.printCompressionRatio(filePath, compressedFileName);
                System.out.println("File compressed successfully as: " + compressedFileName);

            } else if (choice.equals("D")) {
                // Decompression
                File fileToDecompress = new File(filePath);
                if (!fileToDecompress.exists() || !filePath.endsWith(".lzw")) {
                    throw new FileNotFoundException("The file does not exist or is not a valid .lzw file.");
                }

                ArrayList<Integer> compressedData = lzw.readCompressedFile(filePath);
                String decompressedData = lzw.decompress(compressedData);

                String decompressedFileName = filePath.substring(0, filePath.lastIndexOf('.')) + "_decompressed.txt";
                lzw.writeFile(decompressedData, decompressedFileName);

                // Print decompression ratio
                lzw.printDecompressionRatio(filePath, decompressedFileName);
                System.out.println("File decompressed successfully as: " + decompressedFileName);

            } else {
                System.out.println("Invalid choice. Please enter 'C' for compress or 'D' for decompress.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("File operation error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("File operation error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Decompression error: " + e.getMessage());
        } 
//            finally {
//            scanner.close();
//        }
    }
}
/*OUTPUT
Enter the path of the file: C:\\Users\\SIMRAN\\Desktop\\417_DBMS-Assignment 2.txt
Choose operation - Compress (C) or Decompress (D): C
Original File Size: 37827 bytes
Compressed File Size: 30708 bytes
Compression Ratio: 81.18%
File compressed successfully as: C:\\Users\\SIMRAN\\Desktop\\417_DBMS-Assignment 2.lzw
Enter the path of the file: C:\\Users\\SIMRAN\\Desktop\\417_DBMS-Assignment 2.lzw
Choose operation - Compress (C) or Decompress (D): D
Original File Size: 30708 bytes
Decompressed File Size: 37005 bytes
Decompression Ratio: 120.51%
File decompressed successfully as: C:\\Users\\SIMRAN\\Desktop\\417_DBMS-Assignment 2_decopressed.txt
*/
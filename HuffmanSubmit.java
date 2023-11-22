import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

public class HuffmanSubmit implements Huffman {
	
	//general node class with a method to detect leaves
	public class Node {
		public Node left;
		public Node right;
		public char character;
		public int freq;
		public boolean isLeaf() {
			return (left == null) && (right == null);
		}
	}
	
	//class for comparing two nodes
	class nodeComparer implements Comparator<encoderNode> {
	    public int compare(encoderNode x, encoderNode y) {
	        return x.value - y.value;
	    }
	}
	
	//class for special nodes used for decoding
	static class decoderNode {
	    char c = '\u0000';	    
	    decoderNode parent;
	    decoderNode left = null;
	    decoderNode right = null;
	    boolean leaf = true;
	}
	
	//class for special nodes used for encoding
	class encoderNode {
	    int value;
	    char c;
	    encoderNode left;
	    encoderNode right;
	}
	
	//Takes a file's data and begins reading it, converting into a binary string
    public static String fileToBinaryString(String fileName) {
        String encodedData  = "";

        BinaryIn bi = new BinaryIn(fileName);
        int length = bi.readInt();

        for (int i = 0; i < length; i++) {
            Boolean bool = bi.readBoolean();
            if (bool == false) {
                encodedData = encodedData + "0";
            } 
            if (bool == true) {
                encodedData = encodedData + "1";
            }
        }
        return encodedData;
    }
    
    public static void transcribeBinary(String fileName, String data, int dataLength) {
		BinaryOut output = new BinaryOut(fileName);
        output.write(dataLength);
        for (int j = 0; j < data.length(); j++) {
            if (data.charAt(j) == '0') {
                output.write(false);
            } else if (data.charAt(j) == '1') {
                output.write(true);
            } else
                throw new IllegalStateException("Illegal state");
        }
		output.close();
    }

    public static String fileToString(String fileName) {
        String binaryData = "";

        BinaryIn fileContent = new BinaryIn(fileName);
        while (!fileContent.isEmpty()) {
            binaryData = binaryData + fileContent.readChar();
        }
        return binaryData;
    }
    
    //write down the content in a file and its binary sequence, store it in a byte array
    public static byte[] transcribeByte(String fileName, int fileLength) {
        byte[] fileContent = new byte[fileLength];
        BinaryIn binaryFile = new BinaryIn(fileName);
        for (int i = 0; i < fileLength; i++) {
            fileContent[i] = binaryFile.readByte();
        }
        return fileContent;
    }

    //Takes the data inserted and tries to put it into a new file. Hopefully it works.
    public static void transcribeData(String fileName, String data) {
        new File(fileName);
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Writes a tree
    private static void writeTree(Node x) {
    	BinaryOut out = null;
		if (x.isLeaf()) {
			out.write(true);
			out.write(x.character, 8);
			return;
		}
		else {
		out.write(false);
		writeTree(x.left);
		writeTree(x.right);
		}
	}
    
    //Searches a hashmap of all the characters and begins marking its frequency
    static Map<Character, Integer> frequencyParser(String data){
        HashMap<Character, Integer> freq = new HashMap<>();
        for (char x: data.toCharArray()) {
            if(freq.get(x) != null){
                freq.put(x, freq.get(x) + 1);
            }            
            else freq.put(x ,1);
        }
        return freq;
    }
    
  //method to transcribe an image with the appropriate data and reconstruct the image
  	public static void encodeIMG(String decodedFile) {
          File file = new File(decodedFile);
  		byte[] array = new byte[(int) file.length()];
  		try {
  			FileInputStream fileInputStream = new FileInputStream(file);
  			fileInputStream.read(array);
              fileInputStream.close();
  		} catch (IOException e1) {
  			e1.printStackTrace();
  		}		
  		try {
              File imgFile = new File(decodedFile); 
              BufferedImage img = ImageIO.read(imgFile);
              File output = new File("test.jpg");
              ImageIO.write(img, "JPG", output);
  		} catch (IOException a) {
  			a.printStackTrace();
  		} catch (IllegalArgumentException a) {
  			a.getMessage();
  		}
      }
  	
    //tree for mapping out a file into nodes and sorting it into 1's and 0's
    static void encodeTree(encoderNode node, String current, Map<String, Character> map){
        if(node.c == '\u0000'){
            if(node.left != null) {
                encodeTree((node.left), current + "0", map);
            }
            if(node.right != null) {
                encodeTree((node.right), current + "1", map);
            }
        }
        else {
            map.put(current, node.c);
        }
    }
    
    //utility function used to take a string array and transcribe it onto a primitive string for easy iteration
    public static String converter(String[] array) {
        String stringData = "";
        for (int i = 0; i < array.length; i++) {
            stringData = stringData + array[i];
            if (i != array.length - 1) {
                stringData = stringData + "	\n";
            }
        }
        return stringData;
    }
    
    //does the opposite, takes a string and starts it up into a string array. overloaded the converter method
    public static String[] converter(String string) {
        String[] returnArray = string.split("	\n");
        return returnArray;
    }

    //attach a character node to a decode tree in a direction
    static decoderNode appendNode(decoderNode guideline, String direction, char c) {
    	decoderNode current = guideline;
    	decoderNode output = guideline;
        decoderNode parent = current;

        for (int i = 0; i < direction.length(); i++) {
            decoderNode newNode = new decoderNode();

            if (direction.charAt(i) == '0') {
                if (current.left == null) {
                    current.left = new decoderNode();
                }
                
                if (current.left.leaf) {
                    newNode.leaf = true;
                    current.leaf = false;

                    current.left = newNode;
                    current.left.parent = parent;
                    current = current.left;
                    parent = current;
                } else {
                    current = current.left;
                    parent = current;
                }
            }

            if (direction.charAt(i) == '1') {
                if (current.right == null) {
                    current.right = new decoderNode();
                }
                
                if (current.right.leaf) {
                    newNode.leaf = true;
                    current.leaf = false;

                    current.right = newNode;
                    current.right.parent = parent;
                    current = current.right;
                    parent = current;
                } else {
                    current = current.right;
                    parent = current;
                }
            }

            if (i == direction.length() - 1) {
                current.c = c;
            }
        }
        
        return output;
    }

    //builds a tree based on the input data in order to begin decoding the encrypted data
    static decoderNode decoderTree(String inputData) {

        decoderNode decodeTree = new decoderNode();
        decodeTree.parent = null;
        decodeTree.leaf = true;

        String[] charCodes = converter(inputData);

        for (int i = 0; i < charCodes.length; i++) {
            
            char c;
            if (charCodes[i].length() > 0) {
                int parseInt = Integer.parseInt(charCodes[i].split(":")[0], 2);
                c = (char)parseInt;

                String directionData = charCodes[i].split(":")[1];
                appendNode(decodeTree, directionData, c);
            } else {
                continue;
            }
        }
        return decodeTree;
    }
    
    //encode a file's data utilizing the Huffman Algorithim in order to compress it by building shorter pathways to more frequently used chars.
    public void encode (String inputFile, String encodedFile, String frequencyFile) {
    	Map<String, Character> charMap = new HashMap<>();
    	String encodedData = "";
        String inputData =  fileToString(inputFile);
        Map<Character, Integer> frequency =  frequencyParser(inputData);
        PriorityQueue<encoderNode> queue = new PriorityQueue<encoderNode>(frequency.size(), new nodeComparer());

        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            encoderNode charNode = new encoderNode();

            Character key = entry.getKey();
            Integer value = entry.getValue();

            charNode.c = key; 
            charNode.value = value;

            charNode.left = null;
            charNode.right = null;

            queue.add(charNode);
        }
        
        encoderNode root = null;
        while(queue.size() > 1) {
            encoderNode x,y;
            x = queue.peek();
            queue.poll();
            y = queue.peek();
            queue.poll();
            encoderNode node = new encoderNode();
            node.left = x;
            node.right = y;
            node.value = x.value+y.value;
            node.c = '\u0000';
            root = node;
            queue.add(node);
        }

        String[] freq = new String[frequency.entrySet().size()];
        int num = 0;

        encodeTree(root, "", charMap);
        for (Map.Entry<String, Character> entry : charMap.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();

            freq[num] = Integer.toBinaryString(value) + ":" + key;
            num++;
        }

        if (freq[frequency.entrySet().size()-1] == null) {
            String[] tempArray = new String[frequency.entrySet().size()-1];
            for (int i = 0; i < frequency.entrySet().size() - 1; i++) {
                tempArray[i] = freq[i];
            }
            freq = tempArray;
        }

        for (int i = 0; i < inputData.length(); i++) {
            for(Map.Entry<String, Character> entry : charMap.entrySet()) {
                if(entry.getValue() == inputData.charAt(i)) {
                    encodedData = encodedData + entry.getKey();
                }
            }
        }
         transcribeBinary(encodedFile, encodedData, encodedData.length());
         transcribeData(frequencyFile,  converter(freq));
    }
    
    //Take encrypted data and decrypt it using Huffman's algorithim and the frequency file as a key to do so.
    public void decode(String encodedFile, String decodedFile, String frequencyFile) {
        String encodedData =  fileToBinaryString(encodedFile);;
        String returnData = "";

        String frequencyData =  fileToString(frequencyFile);

        decoderNode dn = decoderTree(frequencyData);
        decoderNode current = dn;
        for (int i = 0; i < encodedData.length(); i++) {
        	if (encodedData.charAt(i) == '1') {
                current = current.right;
                if (current.c != '\u0000') {
                    returnData = returnData + current.c;
                    current = dn;
                }
            }
            if (encodedData.charAt(i) == '0') {
                current = current.left;
                if (current.c != '\u0000') {
                returnData = returnData + current.c;
                current = dn;
                }
            }
        }
         transcribeData(decodedFile, returnData);
    }

   public static void main(String[] args) {
      Huffman  huffman = new HuffmanSubmit();
		huffman.encode("alice30.txt", "ur.enc", "freq.txt");
		huffman.decode("ur.enc", "ur_dec.txt", "freq.txt");
		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same.
   }

}

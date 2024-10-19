# project-huffman
Simple algorithm that, following Huffman prefix coding, allows for optimal lossless data compression. Utilizes code provided by Robert Sadgewick and Kevin Wayne of Princeton. Created in collaboration with Alan Jimenez & Matthew Fortes.

## Files: 
    --- Huffman.java 
        Functions:
            1. encode (String inputFile, String encodedFile, String frequencyFile) / Return Type: void
                Explanation:
                    Encodes an input file, outputs the data to an encoded file and generates a frequency file which will be used for decoding.

            2. decode(String encodedFile, String decodedFile, String frequencyFile) / Return Type: void

                Explanation:
                    Decodes an input file through the use of a frequency file.

    --- utilities.java ---

        Functions:
            1. frequencyParser(String data) / Return Type: Map<Character, Integer>

                Explanation:
                    Creates a hashmap for each character in the input data along with its frequency

                Upper Level Psuedo Code:
                    - for each character in the input data
                        - append the character and its frequency into a hashmap
                    - return frequency

            2. stringToArray(String data) / Return Type: String[]

                Explanation:
                    Converts a string to an array

                Upper Level Psuedo Code:
                    - Split the string by a certain character and return the created array


            3. stringToArray(String string) / Return Type: String[]

                Explanation:
                    Converts and array to a string

                Upper Level Psuedo Code:
                    - for each item in the array, append it to the return string
                    - return the return string


    --- BinaryIn.java ---

        Functions:
           "This library is for reading binary data from an input stream, 
           providing methods for reading in bits from a binary input stream, 
           either one-bit boolean, 8-bits char/byte, 16-bits short, 32-bits int/float, 
           or 64-bits double/long." - https://algs4.cs.princeton.edu/11model/BinarySearch.java.html


    --- BinaryOut.java ---

        Functions:
           "Write binary data to an output stream, 
           either one 1-bit boolean, one 8-bit char, one 32-bit int, one 64-bit double, one 32-bit float, 
           or one 64-bit long at a time. The output stream can be standard output, a file, an OutputStream or a Socket."
           - https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/BinaryOut.java.html


    --- encoderNode.java ---
        Functions:
            None


    --- decoderNode.java ---
        Functions:
            None

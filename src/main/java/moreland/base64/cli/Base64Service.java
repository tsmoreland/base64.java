//
// Copyright © 2020 Terry Moreland
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
// to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
// and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//

package moreland.base64.cli;

import java.util.stream.Stream;

public interface Base64Service {
    
    /**
     * Encode a Byte array to base64 encoded string
     * @param source
     * @return base64 encoded String
     */
    String encode(byte[] source);

    /**
     * Encode a Stream of Bytes to base64 encoded string
     * @param source
     * @return base64 encoded String
     */
    String encode(Stream<Byte> source);

    /**
     * Encode a String of a base64 encoded string
     * @param source
     * @return base64 encoded String
     */
    String encode(String source);

    /**
     * Decode a Bas64 encoded String to byte array
     * @param source
     * @return byte array of decoded {@code source}
     */
    byte[] decode(String source);
    /**
     * Decode a Bas64 encoded String to String
     * @param source
     * @return String of decoded {@code source}
     */
    String decodeToString(String source);
}

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

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import moreland.base64.cli.internal.GuardAgainst;

@Service("base64Service")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class SimpleBase64Service implements Base64Service {

    private static final String SOURCE_ARGUMENT_NAME = "source";
    private Logger logger = LoggerFactory.getLogger(SimpleBase64Service.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String encode(byte[] source) {
        GuardAgainst.argumentBeingNull(source, SOURCE_ARGUMENT_NAME);

        return Base64.getEncoder().encodeToString(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encode(Stream<Byte> source) {
        GuardAgainst.argumentBeingNull(source, SOURCE_ARGUMENT_NAME);

        var sourceArray = source.toArray(Byte[]::new);
        var convertedSourceArray = new byte[sourceArray.length];
        System.arraycopy(sourceArray, 0, convertedSourceArray, 0, sourceArray.length);

        return encode(convertedSourceArray);
    }

    /**
     * Encode a Stream of Bytes to base64 encoded string
     * @param streamSource 
     * @return base64 encoded String
     */
    public String encode(InputStream streamSource) {
        GuardAgainst.argumentBeingNull(streamSource, "streamSource");

        var buffer = new byte[3];
        var builder = new StringBuilder();

        try {
            while (streamSource.available() > 0) {
                int read;
                if ((read = streamSource.read(buffer)) <= 0)
                    break;
                builder.append(new String(buffer, 0, read, StandardCharsets.UTF_8));
            }

            return builder.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encode(String source) {
        GuardAgainst.argumentBeingNull(source, SOURCE_ARGUMENT_NAME);
        return encode(source.getBytes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] decode(String source) {
        GuardAgainst.argumentBeingNull(source, SOURCE_ARGUMENT_NAME);
        return Base64.getDecoder().decode(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String decodeToString(String source) {
        GuardAgainst.argumentBeingNull(source, SOURCE_ARGUMENT_NAME);
        return new String(decode(source), StandardCharsets.UTF_8);
    }
    
}

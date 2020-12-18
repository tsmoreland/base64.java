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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import moreland.base64.cli.internal.GuardAgainst;

@Service("fileEncoderService")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class SimpleFileEncoderService implements FileEncoderService {

    private EncoderService encoderService;
    private Logger logger = LoggerFactory.getLogger(SimpleFileEncoderService.class);
    private static final String FILE_NOT_FOUND = "File not found";

    @FunctionalInterface
    private interface EncodeDecodeProcessor {
        public <T> T process(InputStream source);
    }

    @Autowired
    public SimpleFileEncoderService(EncoderService encoderService) {
        GuardAgainst.argumentBeingNull(encoderService, "encoderService");
        this.encoderService = encoderService;
    }

    @Override
    public Optional<String> encode(File file) {
        if (!file.exists()) {
            logger.error(FILE_NOT_FOUND);
            return Optional.empty();
        }

        try (var fileStream = new FileInputStream(file);
             var bufferedInputStream = new BufferedInputStream(fileStream)) {

            return Optional.of(encoderService.encode(bufferedInputStream));

        } catch (IOException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<byte[]> decode(File file) {
        if (!file.exists()) {
            logger.error(FILE_NOT_FOUND);
            return Optional.empty();
        }

        try (var fileStream = new FileInputStream(file);
             var bufferedInputStream = new BufferedInputStream(fileStream)) {

            return Optional.of(encoderService.decode(bufferedInputStream));

        } catch (IOException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean encode(File inputFile, File outputFile) {
        return false;
    }

    @Override
    public boolean decode(File inputFile, File outputFile) {
        return false;
    }
}

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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.function.Function;

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

    @Autowired
    public SimpleFileEncoderService(EncoderService encoderService) {
        GuardAgainst.argumentBeingNull(encoderService, "encoderService");
        this.encoderService = encoderService;
    }

    @FunctionalInterface
    private interface StreamToStreamProcessor {
        boolean process(InputStream source, OutputStream destination);
    }

    private Optional<byte[]> processFile(File source, Function<InputStream, byte[]> processor) {
        if (!source.exists()) {
            logger.error(FILE_NOT_FOUND);
            return Optional.empty();
        }

        try (   var fileStream = new FileInputStream(source);
                var bufferedInputStream = new BufferedInputStream(fileStream)) {

            return Optional.of(processor.apply(bufferedInputStream));

        } catch (IOException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }
    private boolean processFileToFile(File source, File destinatation, StreamToStreamProcessor processor) {
        if (!source.exists()) {
            logger.error(FILE_NOT_FOUND);
            return false;
        }

        try (   var inputFileStream = new FileInputStream(source);
                var bufferedInputStream = new BufferedInputStream(inputFileStream);) {

            return processStreamToFile(bufferedInputStream, destinatation, processor);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
    private boolean processStreamToFile(InputStream source, File destination, StreamToStreamProcessor processor) {
        GuardAgainst.argumentBeingNull(source, "source");
        GuardAgainst.argumentBeingNull(destination, "destination");

        try (var bufferedInputStream = new BufferedInputStream(System.in);
             var outputFileStream = new FileOutputStream(destination);
             var bufferedOutputStream = new BufferedOutputStream(outputFileStream);) {

            return processor.process(bufferedInputStream, bufferedOutputStream);

        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<byte[]> encode(File file) {
        return processFile(file, encoderService::encode);
    }

    @Override
    public Optional<byte[]> decode(File file) {
        return processFile(file, encoderService::decode);
    }

    @Override
    public boolean encode(File inputFile, File outputFile) {
        return processFileToFile(inputFile, outputFile, encoderService::encode);
    }

    @Override
    public boolean decode(File inputFile, File outputFile) {
        return processFileToFile(inputFile, outputFile, encoderService::decode);
    }

    @Override
    public boolean encode(InputStream source, File destination) {
        return processStreamToFile(source, destination, encoderService::encode);
    }

    @Override
    public boolean decode(InputStream source, File destination) {
        return processStreamToFile(source, destination, encoderService::decode);
    }

}

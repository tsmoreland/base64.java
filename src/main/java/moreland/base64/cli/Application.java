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

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import moreland.base64.cli.internal.FileEncodingConverter;
import moreland.base64.cli.internal.FileToFileEncodingConverter;
import moreland.base64.cli.internal.Operation;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    private FileEncoderService fileEncoderService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    @SuppressWarnings({"java:S2589"}) // warning doesn't yet see result from switch expression
    public void run(String... args) throws Exception {
        logger.info("provided arguments: {}", args.length);

        if (fileEncoderService == null) {
            logger.error("error injecting service");
        }

        if (args.length < 1) {
            logger.error("Insufficient arguments");
            return;
        }

        var operation = Operation.fromString(args[0]).orElse(Operation.UNSUPPORTED);

        Optional<String> inputFilename = Optional.empty();
        Optional<String> outputFilename = Optional.empty();

        if (args.length > 1)
            inputFilename = Optional.of(args[1]);
        if (args.length > 2)
            outputFilename = Optional.of(args[2]);


        boolean result = switch(operation) {
            case ENCODE -> encode(inputFilename, outputFilename);
            case DECODE -> decode(inputFilename, outputFilename);
            case UNSUPPORTED -> false;
            default -> throw new IllegalStateException("Unsupported Operation");
        };

        if (!result) {
            logger.error("{} for {} failed.", operation, inputFilename);
        }
    }

    private boolean encode(final Optional<String> inputFilename, final Optional<String> outputFilename) {
        return process(inputFilename, outputFilename, this::encodeFromFileToFile, this::encodeFromFile);
    }
    private boolean decode(final Optional<String> inputFilename, final Optional<String> outputFilename) {
        return process(inputFilename, outputFilename, this::decodeFromFileToFile, this::decodeFromFile);
    }

    private boolean process(final Optional<String> inputFilename, final Optional<String> outputFilename, 
        FileToFileEncodingConverter fileToFile, FileEncodingConverter fileEncodingConverter) {

        if (inputFilename.isPresent()) {
            if (outputFilename.isPresent()) {
                return fileToFile.process(inputFilename.get(), outputFilename.get());
            } else {
               return fileEncodingConverter.process(inputFilename.get());
            }
        } else {
            // TODO: add support for in/out when filenames not given, either allow pipe from stdin/stdout or clipboard
            return false;
        }
    }


    @SuppressWarnings({"java:S106"})
    private boolean encodeFromFile(final String inputFilename) {
        var encoded = fileEncoderService.encode(new File(inputFilename));
        if (!encoded.isPresent())
            return false;

        System.out.println(encoded.get());
        return encoded.isPresent();
    }
    private boolean encodeFromFileToFile(final String inputFilename, final String outputFilename) {
        return fileEncoderService.encode(new File(inputFilename), new File(outputFilename));
    }

    @SuppressWarnings({"java:S106"})
    private boolean decodeFromFile(final String inputFilename) {
        var decoded = fileEncoderService.decode(new File(inputFilename));
        if (!decoded.isPresent())
            return false;

        var decodedString = new String(decoded.get(), StandardCharsets.UTF_8);
        System.out.println(decodedString);
        return true;
    }
    private boolean decodeFromFileToFile(final String inputFilename, final String outputFilename) {
        return fileEncoderService.decode(new File(inputFilename), new File(outputFilename));
    }

}

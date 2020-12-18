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
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import moreland.base64.cli.internal.Operation;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	private Base64Service base64Service;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	@SuppressWarnings({"java:S2589"}) // warning doesn't yet see result from switch expression
	public void run(String... args) throws Exception {
		logger.info("provided arguments: {}", args.length);

		if (base64Service == null) {
			logger.error("error injecting service");
		}

		if (args.length < 2) {
			logger.error("Insufficient arguments");
			return;
		}

		var operation = Operation.fromString(args[0]).orElse(Operation.UNSUPPORTED);
		final var inputFilename = args[1];

		boolean result = switch(operation) {
			case ENCODE -> encode(inputFilename);
			case DECODE -> decode(inputFilename);
			case UNSUPPORTED -> false;
			default -> throw new IllegalStateException("Unsupported Operation");
		};

		if (!result) {
			logger.error("{} for {} failed.", operation, inputFilename);
		}
	}

	private boolean encode(final String filename) {
		var file = new File(filename);
		if (!file.exists()) {
			return false;
		}

		try (var fileStream = new FileInputStream(file);
			 var bufferedInputStream = new BufferedInputStream(fileStream)) {

			var encoded = base64Service.encode(fileStream);

			logger.info("{}{}", System.lineSeparator(), encoded);

			return true;

		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		}

	}
	private boolean decode(final String filename) {
		var file = new File(filename);
		if (!file.exists()) {
			return false;
		}

		return true;
	}

}

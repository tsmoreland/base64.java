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
package moreland.base64.cli.internal;

import java.util.Arrays;
import java.util.List;

/**
 * CLI Operations
 */
public enum Operation {
    UNSUPPORTED,
    ENCODE,
    DECODE;

    public static Operation fromArguments(List<String> arguments) {
        GuardAgainst.argumentBeingNull(arguments, "arguments");

        if (arguments.isEmpty()) {
            return Operation.UNSUPPORTED;
        }
        
        var firstArgument = arguments.get(0).toUpperCase();
        var operation = Arrays.stream(Operation.class.getEnumConstants())
            .filter(op -> op.toString().equals(firstArgument))
            .findFirst();

        return operation.orElse(Operation.UNSUPPORTED);
    }

}

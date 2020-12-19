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

import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

@Service("clipboardService")
@Scope(value= BeanDefinition.SCOPE_SINGLETON)
public class SimpleClipboardService implements ClipboardService {
    
    private Logger logger = LoggerFactory.getLogger(SimpleClipboardService.class);
    private Optional<Clipboard> clipboard;

    public SimpleClipboardService() {
        try {
            clipboard = Optional.of(Toolkit.getDefaultToolkit().getSystemClipboard());

        } catch (Exception e) {
            logger.error(e.getMessage());
            clipboard = Optional.empty();
        }
    }


    @Override
    public boolean write(String source) {
        if (source == null || !clipboard.isPresent()) {
            return false;
        }

        try {
            var contents = new StringSelection(source);
            clipboard.get().setContents(contents, null);
            return true;
        } catch (IllegalStateException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean write(byte[] source) {
        if (source == null || !clipboard.isPresent()) {
            return false;
        }
        return write(new String(source, StandardCharsets.UTF_8));
    }

    @Override
    public boolean write(InputStream source) {
        if (source == null || !clipboard.isPresent()) {
            return false;
        }
        try {
            return write(new String(source.readAllBytes(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<String> readAsString() {
        if (!clipboard.isPresent()) {
            return Optional.empty();
        }

        var contents = clipboard.get().getContents(null);
        if (!contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return Optional.empty();
        }
        try {
            var data = contents.getTransferData(DataFlavor.stringFlavor);
            return data instanceof String
                ? Optional.of((String)data)
                : Optional.empty();
        } catch (IOException | UnsupportedFlavorException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<byte[]> readAsByteArray() {
        return readAsString().map(String::getBytes);
    }
}

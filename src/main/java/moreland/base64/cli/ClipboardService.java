package moreland.base64.cli;

import java.io.InputStream;
import java.util.Optional;

public interface ClipboardService {
    boolean write(String source);

    boolean write(byte[] source);

    boolean write(InputStream source);

    Optional<String> readAsString();

    Optional<byte[]> readAsByteArray();
}

package moreland.base64.cli.internal;

@FunctionalInterface
public interface FileEncodingConverter {
    boolean process(final String filename);    
}

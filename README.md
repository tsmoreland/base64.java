# Base64 CLI

[![Open in Visual Studio Code](https://open.vscode.dev/badges/open-in-vscode.svg)](https://open.vscode.dev/tsmoreland/base64.java)
![Java CI with Maven](https://github.com/tsmoreland/base64.java/workflows/Java%20CI%20with%20Maven/badge.svg)

Simple command line utility intended to take a file source and encode to or decode from Base64

The general idea is to eventually support the following usage:

- ```<command> encode (input file) (output file)``` 
- ```<command> decode (input file) (output file)```

where ```(input file)``` and ```(output file)``` are optional, 
if output file isn't provided the write to stdout and maybe copy to clipboard.
If input file isn't provided then either read from stdin or may clipboard.

It's likely there will be minimal checks at best for file size, it is attempting to stream for files so that may work out but for anything else the file is read in chunks but the encoded or decoded result will be in memory

## Maven Wrapper

maven wrapper path .mvn is no longer in source control, it can be re-created using mvn wrapper:wrapper

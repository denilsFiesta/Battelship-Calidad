package com;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class CustomInputStream extends InputStream {
    private List<ByteArrayInputStream> inputStreams;
    private int currentIndex;

    public CustomInputStream(List<String> inputs) {
        this.inputStreams = inputs.stream()
                                  .map(String::getBytes)
                                  .map(ByteArrayInputStream::new)
                                  .toList(); 
        this.currentIndex = 0;
    }

    @Override
    public int read() throws IOException {
        if (currentIndex >= inputStreams.size()) {
            return -1;
        }

        ByteArrayInputStream currentStream = inputStreams.get(currentIndex);
        int result = currentStream.read();

        if (result == -1) { 
            currentIndex++;
            return read();
        }

        return result;
    }

    public void resetToFirstInput() {
        currentIndex = 0; 
        for (ByteArrayInputStream stream : inputStreams) {
            stream.reset();
        }
    }
}

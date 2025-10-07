package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final String fileName;
    private final ObjectMapper mapper;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.mapper = new ObjectMapper();
    }

    @Override
    public List<Measurement> load() throws IOException {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new FileProcessException("FileNotFound: " + fileName);
            }
            return mapper.readValue(is, new TypeReference<>() {
            });
        }
    }


}

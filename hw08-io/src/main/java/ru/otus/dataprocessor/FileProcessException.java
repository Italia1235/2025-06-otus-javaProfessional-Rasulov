package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FileProcessException extends RuntimeException {

    public FileProcessException(Exception ex) {
        super(ex);
    }

    public FileProcessException(String msg) {
        super(msg);
    }
}

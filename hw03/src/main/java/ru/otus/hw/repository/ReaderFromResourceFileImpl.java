package ru.otus.hw.repository;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

@Component
public class ReaderFromResourceFileImpl implements Reader {
    @Override
    public BufferedReader getData(String fileName) {
        return new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass()
                        .getClassLoader()
                        .getResourceAsStream(fileName))));
    }
}

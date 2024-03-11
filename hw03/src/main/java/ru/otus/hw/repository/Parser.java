package ru.otus.hw.repository;

import java.io.BufferedReader;
import java.util.List;

public interface Parser {
    <T> List<T> parse(BufferedReader bufferedReader);
}

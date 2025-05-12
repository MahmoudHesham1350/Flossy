package service;

import java.util.Dictionary;

public interface IService<T> {
    T validate(Dictionary<String, String> data) throws IllegalArgumentException;
}

package service;

import java.io.Serializable;
import java.util.Dictionary;

public interface IService <T extends Serializable> {
    public T validate(Dictionary<String, String> data) throws IllegalArgumentException;
}

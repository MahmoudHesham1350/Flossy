package controller;

import java.util.Dictionary;
import java.util.List;

public interface IController<T> {
    public void create(Dictionary <String, String> data);
    public List<T> getAll();
}

package controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.List;

import service.IService;
import storage.UltraSimpleStorage;

public class Controller <T extends Serializable> implements IController<T> {
    protected IService<T> service;
    private UltraSimpleStorage<T> storage;

    public Controller(IService<T> service, UltraSimpleStorage<T> storage) {
        this.service = service;
        this.storage = storage;
    }

    public void create(Dictionary <String, String> data) throws IllegalArgumentException {
        T object = service.validate(data);
        storage.add(object);

    }

    @Override
    public List<T> getAll() {
        return storage.getAll();
    }

    public void remove(T object) {
        storage.remove(object);
    }

    public void save() {
        try {
            storage.save();
        } catch (IOException e) {
            throw new RuntimeException("Error saving data", e);
        } catch (Exception e) {
            throw new RuntimeException("Error saving data", e);
        }
    }

    
}

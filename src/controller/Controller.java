package controller;

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

    public void create(Dictionary <String, String> data) {
        try {
            T object = service.validate(data);
            storage.add(object);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    public List<T> getAll() {
        return storage.getAll();
    }

    
}

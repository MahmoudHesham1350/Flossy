package storage;

import java.util.ArrayList;
import java.io.*;
import java.util.List;

public abstract class UltraSimpleStorage<T extends Serializable> {
    private final String filename;
    protected List<T> storage;

    public UltraSimpleStorage(String filename) {
        this.filename = filename;
        try {
            this.storage = this.load();
        } catch (Exception e) {
            System.out.println("No previous data found. Starting fresh.");
            this.storage = new ArrayList<>();
        }
    }

    public void save(List<T> objectsToSave) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(objectsToSave);
        }
        System.out.println("Objects saved successfully to file: " + filename);
    }

    public List<T> load() throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<T>) ois.readObject();
        }
    }

    public void add(T object) {
        storage.add(object);
    }

    public void remove(T object) {
        storage.remove(object);
    }

    public List<T> getAll() {
        return storage;
    }

}
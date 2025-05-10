package storage;

import java.io.*;
import java.util.List;

public abstract class UltraSimpleStorage<T extends Serializable> {
    private final String filename;

    public UltraSimpleStorage(String filename) {
        this.filename = filename;
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
}
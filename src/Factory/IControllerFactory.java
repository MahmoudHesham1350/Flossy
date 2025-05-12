package Factory;

import java.io.IOException;

import controller.IController;

public interface IControllerFactory<T> {
    public IController<T> createController() throws ClassNotFoundException, IOException, Exception;
}
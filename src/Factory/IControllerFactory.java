package Factory;

import java.io.IOException;

import controller.IController;

public interface IControllerFactory<T> {
    IController<T> createController() throws ClassNotFoundException, IOException, Exception;
}
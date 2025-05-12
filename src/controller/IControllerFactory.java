package controller;

import java.io.IOException;

public interface IControllerFactory<T> {
    IController<T> createController() throws ClassNotFoundException, IOException, Exception;
}
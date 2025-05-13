package Factory;

import java.io.IOException;

import controller.IController;

/**
 * Interface for controller factories in Flossy.
 * Provides a method to create controllers for a specific type.
 * @param <T> The type of model the controller manages
 */
public interface IControllerFactory<T> {
    /**
     * Creates a controller for the specified type.
     * @return A new controller instance
     * @throws ClassNotFoundException if storage loading fails
     * @throws IOException if storage loading fails
     * @throws Exception for other errors
     */
    public IController<T> createController() throws ClassNotFoundException, IOException, Exception;
}
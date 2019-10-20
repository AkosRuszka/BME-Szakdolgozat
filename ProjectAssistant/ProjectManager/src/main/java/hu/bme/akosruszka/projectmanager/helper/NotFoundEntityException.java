package hu.bme.akosruszka.projectmanager.helper;

public class NotFoundEntityException extends RuntimeException {

    public NotFoundEntityException(String message) {
        super(message);
    }

}

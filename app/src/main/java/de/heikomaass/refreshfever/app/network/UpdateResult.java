package de.heikomaass.refreshfever.app.network;

/**
 * Created by hmaass on 08.04.14.
 */
public class UpdateResult {
    private boolean successful;
    private String errorMessage;

    public UpdateResult(boolean successful, String errorMessage) {
        this.successful = successful;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccessful() {
        return successful;
    }
}

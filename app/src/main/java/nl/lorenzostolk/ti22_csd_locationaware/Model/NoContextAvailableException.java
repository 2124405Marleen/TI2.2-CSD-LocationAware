package nl.lorenzostolk.ti22_csd_locationaware.Model;

public class NoContextAvailableException extends Exception {
    public NoContextAvailableException() {
        super("Context is not set or not available. Invoke 'initPreferences(Context)' before invoking 'GetInstance()'.");
    }
}

package de.horizont.metawearunity;

public interface IBoardConnectionHandler {
    void OnConnected ();
    void OnFailed ();
    void OnUnexpectedDisconnect ();
}

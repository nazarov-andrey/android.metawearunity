package de.horizont.metawearunity;

public interface IFusionSensorHandler {
    void OnNewValue(float heading, float pitch, float roll, float yaw);
}

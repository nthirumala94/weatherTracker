public interface MeasurementService {
    void addMeasurement(LocalDateTime timestamp, Metrics metrics);
}
import java.time.LocalDateTime;
public interface MeasurementService {
    void addMeasurement(LocalDateTime timestamp, Metrics metrics);
}
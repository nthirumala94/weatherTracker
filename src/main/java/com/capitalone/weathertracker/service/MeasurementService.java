import java.time.LocalDateTime;
import com.capitalone.weathertracker.model.Metrics;
public interface MeasurementService {
    void addMeasurement(LocalDateTime timestamp, Metrics metrics);
}
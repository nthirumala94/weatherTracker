import java.time.LocalDateTime;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Measurements implements Serializable {
    private LocalDateTime timestamp;
    private float temperature;
    private float dewPoint;
    private float precipation;
    
    public Measurements(
        LocalDateTime timestamp,
        float temperature,
        float dewPoint,
        float precipation
        ) {
            this.timestamp = timestamp;
            this.temperature = temperature;
            this.dewPoint = dewPoint;
            this.precipation = precipation;
        }
    
    public Measurements() {
        
    }
    
    public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public float getDewPoint() {
		return dewPoint;
	}
	public void setDewPoint(float dewPoint) {
		this.dewPoint = dewPoint;
	}
	public float getPrecipation() {
		return precipation;
	}
	public void setPrecipation(float precipation) {
		this.precipation = precipation;
	}
}
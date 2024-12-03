package br.com.wrtecnologia.pgsql4mongo.domain.mongodb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "sensor_data")
public class SensorDataDocument {

    @Id
    private String id;

    private Double temperaturaCelsius;
    private Double temperaturaFahrenheit;
    private Double umidade;

    private LocalDateTime dataHora;

    private String uuid;
}

package br.com.wrtecnologia.pgsql4mongo.domain.mongodb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "sensor_data")
public class SensorDataDocument implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Indexed(unique = true)
    private Long idPg;

    private Double temperaturaCelsius;
    private Double temperaturaFahrenheit;
    private Double umidade;
    private LocalDateTime dataHora;
    private String uuid;

}

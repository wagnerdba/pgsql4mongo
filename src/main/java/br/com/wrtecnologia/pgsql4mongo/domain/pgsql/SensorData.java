package br.com.wrtecnologia.pgsql4mongo.domain.pgsql;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table
public class SensorData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double temperaturaCelsius;
    private Double temperaturaFahrenheit;
    private Double umidade;
    private LocalDateTime dataHora;
    private String uuid;
}
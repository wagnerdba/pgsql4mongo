package br.com.wrtecnologia.pgsql4mongo.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double temperaturaCelsius;
    private Double temperaturaFahrenheit;
    private Double umidade;

    private LocalDateTime dataHora;

    private String uuid;

}

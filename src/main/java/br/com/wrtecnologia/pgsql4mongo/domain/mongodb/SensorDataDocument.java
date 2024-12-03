package br.com.wrtecnologia.pgsql4mongo.domain.mongodb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sensor_data")
public class SensorDataDocument {

    @Id
    private String id;

    private Double temperaturaCelsius;
    private Double temperaturaFahrenheit;
    private Double umidade;

    private LocalDateTime dataHora;

    private String uuid;

    public String getId() {
        return id;
    }

    public Double getTemperaturaCelsius() {
        return temperaturaCelsius;
    }

    public Double getTemperaturaFahrenheit() {
        return temperaturaFahrenheit;
    }

    public Double getUmidade() {
        return umidade;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getUuid() {
        return uuid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTemperaturaCelsius(Double temperaturaCelsius) {
        this.temperaturaCelsius = temperaturaCelsius;
    }

    public void setTemperaturaFahrenheit(Double temperaturaFahrenheit) {
        this.temperaturaFahrenheit = temperaturaFahrenheit;
    }

    public void setUmidade(Double umidade) {
        this.umidade = umidade;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

package dev.brewlog.coffee.model;

import jakarta.persistence.*;

import java.math.*;
import java.time.*;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BrewMethod method;

    @Column(name = "coffee_dose_g" , nullable = false)
    private BigDecimal coffeeDoseGrams;

    @Column(name = "water_dose_g" , nullable = false)
    private BigDecimal waterDoseGrams;

    @Column(name = "extraction_time_seconds")
    private Integer extractionTimeSeconds;

    @Column(name = "temperature_celsius")
    private Integer temperatureCelsius;

    @Column(name = "grind_size")
    private String grindSize;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Transient
    public BigDecimal getRatio() {
        // if coffee is null or 0 → return BigDecimal.ZERO
        // else return water.divide(coffee, 2, RoundingMode.HALF_UP)
        if (coffeeDoseGrams == null || coffeeDoseGrams.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return waterDoseGrams.divide(coffeeDoseGrams, 2, RoundingMode.HALF_UP);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BrewMethod getMethod() {
        return method;
    }

    public void setMethod(BrewMethod method) {
        this.method = method;
    }

    public BigDecimal getCoffeeDoseGrams() {
        return coffeeDoseGrams;
    }

    public void setCoffeeDoseGrams(BigDecimal coffeeDoseGrams) {
        this.coffeeDoseGrams = coffeeDoseGrams;
    }

    public BigDecimal getWaterDoseGrams() {
        return waterDoseGrams;
    }

    public void setWaterDoseGrams(BigDecimal waterDoseGrams) {
        this.waterDoseGrams = waterDoseGrams;
    }

    public Integer getExtractionTimeSeconds() {
        return extractionTimeSeconds;
    }

    public void setExtractionTimeSeconds(Integer extractionTimeSeconds) {
        this.extractionTimeSeconds = extractionTimeSeconds;
    }

    public Integer getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(Integer temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }

    public String getGrindSize() {
        return grindSize;
    }

    public void setGrindSize(String grindSize) {
        this.grindSize = grindSize;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

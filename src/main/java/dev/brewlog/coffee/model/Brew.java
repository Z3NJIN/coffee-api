package dev.brewlog.coffee.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "brews")
public class Brew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id" , nullable = false)
    private Recipe recipe;

    @Column(name = "brewed_at" , nullable = false)
    private LocalDateTime brewedAt;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Embedded
    private TastingNote tastingNote;

    @Column(name = "created_at" , updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public LocalDateTime getBrewedAt() {
        return brewedAt;
    }

    public void setBrewedAt(LocalDateTime brewedAt) {
        this.brewedAt = brewedAt;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public TastingNote getTastingNote() {
        return tastingNote;
    }

    public void setTastingNote(TastingNote tastingNote) {
        this.tastingNote = tastingNote;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

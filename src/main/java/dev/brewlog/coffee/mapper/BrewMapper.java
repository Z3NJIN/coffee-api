package dev.brewlog.coffee.mapper;

import dev.brewlog.coffee.dto.TastingNoteDto;
import dev.brewlog.coffee.dto.request.BrewRequest;
import dev.brewlog.coffee.dto.response.BrewResponse;
import dev.brewlog.coffee.model.Brew;
import dev.brewlog.coffee.model.TastingNote;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BrewMapper {

    public Brew toEntity(BrewRequest request) {
        Brew brew = new Brew();
        brew.setBrewedAt(request.brewedAt());
        brew.setRating(request.rating());
        brew.setNotes(request.notes());
        brew.setTastingNote(toTastingNote(request.tastingNote()));
        return brew;
    }

    public BrewResponse toResponse(Brew brew) {
        return new BrewResponse(
                brew.getId(),
                brew.getRecipe().getId(),
                brew.getRecipe().getName(),
                brew.getRecipe().getMethod(),
                brew.getBrewedAt(),
                brew.getRating(),
                brew.getNotes(),
                toTastingNoteDto(brew.getTastingNote()),
                brew.getCreatedAt()
        );
    }

    private TastingNote toTastingNote(TastingNoteDto dto) {
        if (dto == null) return null;
        TastingNote note = new TastingNote();
        note.setAcidity(dto.acidity());
        note.setSweetness(dto.sweetness());
        note.setBody(dto.body());
        note.setTags(dto.tags());
        return note;
    }

    private TastingNoteDto toTastingNoteDto(TastingNote note) {
        if (note == null) return null;
        return new TastingNoteDto(
                note.getAcidity(),
                note.getSweetness(),
                note.getBody(),
                note.getTags()
        );
    }
}

package dev.brewlog.coffee.model;

import jakarta.persistence.*;

import java.util.List;

@Embeddable
public class TastingNote {

    private Integer acidity;    //1 to 5

    private Integer sweetness;  //1 to 5

    private Integer body;       //1 to 5

    @ElementCollection
    @CollectionTable(name = "brew_tags" , joinColumns = @JoinColumn(name = "brew_id"))
    @Column(name = "tag")
    private List<String> tags;  //ej: brown_sugar, citrus, chocolate, bitter

    public Integer getAcidity() {
        return acidity;
    }

    public void setAcidity(Integer acidity) {
        this.acidity = acidity;
    }

    public Integer getSweetness() {
        return sweetness;
    }

    public void setSweetness(Integer sweetness) {
        this.sweetness = sweetness;
    }

    public Integer getBody() {
        return body;
    }

    public void setBody(Integer body) {
        this.body = body;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}

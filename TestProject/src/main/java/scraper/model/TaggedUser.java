package scraper.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class TaggedUser {
    public String username;
    public Float x;
    public Float y;
}

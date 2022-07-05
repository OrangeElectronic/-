package scraper.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Data
public class Location {
    @Id
    public long id;
    public Boolean hasPublicPage;
    public String name;
    public String slug;
    public Double lat;
    public Double lng;

    public Integer count;
    @Transient
    public MediaRating mediaRating;
}

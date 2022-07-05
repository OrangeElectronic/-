package scraper.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tag_statistics")
@Data
public class Tag {
    @Id
    public String name;
    public Integer count;
    @Transient
    public MediaRating mediaRating;
}

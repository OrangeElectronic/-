package scraper.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class DisplayResource {
    public String src;
    public Integer width;
    public Integer height;
}

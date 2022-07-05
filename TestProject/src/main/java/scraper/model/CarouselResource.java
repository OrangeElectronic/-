package scraper.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class CarouselResource{
    @Id
    public String shortcode;
    @ManyToOne
    @JoinColumn(name="parent_media_id")
    public Media parentMedia;

    protected Integer height;
    protected Integer width;
    protected String displayUrl;
    protected String videoUrl;
    @ElementCollection
    protected List<DisplayResource> displayResources;
    protected Boolean isVideo;
    public Integer videoViewCount;
    @Transient
    protected Boolean shouldLogClientEvent;
    @Transient
    protected String trackingToken;
    @ElementCollection
    protected Collection<TaggedUser> taggedUser;
}

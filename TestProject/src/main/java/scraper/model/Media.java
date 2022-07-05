package scraper.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Media {
    protected Integer height;
    protected Integer width;
    protected String displayUrl;
    protected String videoUrl;
    @ElementCollection
    protected List<DisplayResource> displayResources;
    protected Boolean isVideo;
    @Transient
    protected Boolean shouldLogClientEvent;
    @Transient
    protected String trackingToken;
    public MediaType mediaType;
    @Id
    public long id;
    public String shortcode;
    @Transient
    public String gatingInfo;
    @Column(name = "caption", length = 4096)
    public String caption;
    public Integer commentCount;
    @Transient
    public PageObject<Comment> commentPreview;
    @ManyToMany
    public List<Comment> firstComments;
    public Boolean commentsDisabled;
    public Boolean captionIsEdited;
    public Long takenAtTimestamp;
    public Integer likeCount;
    public Integer videoViewCount;
    @ManyToMany
    public List<Account> firstLikes;
    @ManyToOne
    @JoinColumn(name="location_id")
    public Location location;
    @ManyToOne
    @JoinColumn(name="owner_id")
    public Account owner;
    public Boolean viewerHasLiked;
    public Boolean viewerHasSaved;
    public Boolean viewerHasSavedToCollection;
    public Boolean isAdvertising;
    @OneToMany(mappedBy = "parentMedia")
    public Collection<CarouselResource> carouselMedia;
    @ElementCollection
    public Collection<TaggedUser> taggedUser;
    public Date lastUpdated = new Date();

    @Transient
    public Date getCreated(){
        return takenAtTimestamp != null ? new Date(takenAtTimestamp) : null;
    }
}

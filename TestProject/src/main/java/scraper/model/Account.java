package scraper.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Data
public class Account {
    @Id
    public long id;
    @Column(name = "username", nullable = false)
    public String username;
    public String fullName;
    public Boolean ispublic;
    public Boolean isVerified;
    @Column(name = "biography", length = 4096)
    public String biography;
    @Column(name = "external_url", length = 4096)
    public String externalUrl;
    public Integer followedBy;
    public Integer follows;
    public String blockedByViewer;
    public Boolean countryBlock;
    @Transient
    public String externalUrlLinkshimmed;
    public Boolean followedByViewer;
    public Boolean followsViewer;
    public Boolean hasBlockedViewer;
    public Boolean hasRequestedViewer;
    public String profilePicUrl;
    public String profilePicUrlHd;
    public Boolean requestedByViewer;
    public String connectedFbPage;
    public Boolean isUnpublished;
    @Transient
    public PageObject<Media> media;
    public Date lastUpdated = new Date();
    public Boolean isBusinessAccount;
}

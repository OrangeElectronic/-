package scraper.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Comment {
    @Id
    public Long id;
    @Column(name = "text", length = 4096)
    public String text;
    public Long createdAt;
    @ManyToOne
    @JoinColumn(name="owner_id")
    public Account owner;

    @Transient
    public Date getCreated(){
        return createdAt != null ? new Date(createdAt) : null;
    }
}

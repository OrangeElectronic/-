package scraper.model;

import lombok.Data;

import java.util.List;

@Data
public class PageObject<T> {

    public List<T> nodes;
    public Integer count;
    public PageInfo pageInfo;
}

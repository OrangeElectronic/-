package scraper.request.parameters;

import lombok.Value;

@Value
public class UsernameParameter implements RequestParameter{
    public String username;
}

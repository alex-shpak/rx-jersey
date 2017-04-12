package net.winterly.rxjersey.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubRepository {

    public long id;
    public String name;
    public String full_name;
    public String description;
    public String html_url;

    @JsonProperty("private")
    public boolean isPrivate;

}

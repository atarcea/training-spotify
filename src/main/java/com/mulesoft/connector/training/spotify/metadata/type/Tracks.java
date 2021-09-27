package com.mulesoft.connector.training.spotify.metadata.type;

import java.util.List;
import java.util.Map;

public class Tracks {
    private List<Artist> artists;
    private List<String> available_markets;
    private Long disc_number;
    private Long duration_ms;
    private Boolean explicit;
    private Map<String,String> externalUrls;
    private String href;
    private Long id;
    private Boolean is_local;
    private String name;
    private String preview_url;
    private Double track_number;
    private String type;
    private String uri;

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<String> getAvailable_markets() {
        return available_markets;
    }

    public void setAvailable_markets(List<String> available_markets) {
        this.available_markets = available_markets;
    }

    public Long getDisc_number() {
        return disc_number;
    }

    public void setDisc_number(Long disc_number) {
        this.disc_number = disc_number;
    }

    public Long getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(Long duration_ms) {
        this.duration_ms = duration_ms;
    }

    public Boolean getExplicit() {
        return explicit;
    }

    public void setExplicit(Boolean explicit) {
        this.explicit = explicit;
    }


    public Map<String, String> getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(Map<String, String> externalUrls) {
        this.externalUrls = externalUrls;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIs_local() {
        return is_local;
    }

    public void setIs_local(Boolean is_local) {
        this.is_local = is_local;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public Double getTrack_number() {
        return track_number;
    }

    public void setTrack_number(Double track_number) {
        this.track_number = track_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

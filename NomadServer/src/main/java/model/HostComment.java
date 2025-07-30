package model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class HostComment extends Comment{
    @ManyToOne
    private AppUser host;

    public HostComment() {}

    public HostComment(String text, AppUser appUser, AppUser host) {
        super(text, appUser);
        this.host = host;
    }

    public AppUser getHost() {
        return host;
    }

    public void setHost(AppUser host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "HostComment{" +
                "host=" + host +
                '}';
    }
}

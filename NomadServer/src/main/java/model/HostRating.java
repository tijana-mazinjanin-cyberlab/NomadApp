package model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class HostRating extends Rating{
    @ManyToOne
    private Host host;

    public HostRating () {}

    public HostRating(int rating, AppUser appUser, Host host) {
        super(rating, appUser, "");
        this.host = host;
    }

    public AppUser getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "HostRating{" +
                "host=" + host +
                '}';
    }

}

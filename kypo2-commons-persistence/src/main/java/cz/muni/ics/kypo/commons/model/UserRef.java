package cz.muni.ics.kypo.commons.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

public class UserRef {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "USER_ID", unique = true, nullable = false)
    private long idmUserRef;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getIdmUserRef() {
        return idmUserRef;
    }

    public void setIdmUserRef(long idmUserRef) {
        this.idmUserRef = idmUserRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRef userRef = (UserRef) o;

        if (idmUserRef != userRef.idmUserRef) return false;
        return id != null ? id.equals(userRef.id) : userRef.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (idmUserRef ^ (idmUserRef >>> 32));
        return result;
    }
}

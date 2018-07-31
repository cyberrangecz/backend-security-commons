package cz.muni.ics.kypo.commons.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "USER_REF")
public class UserRef {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "USER_ID", unique = true, nullable = false)
    private long idmUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getIdmUserId() {
        return idmUserId;
    }

    public void setIdmUserId(long idmUserRef) {
        this.idmUserId = idmUserRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRef userRef = (UserRef) o;

        if (idmUserId != userRef.idmUserId) return false;
        return id != null ? id.equals(userRef.id) : userRef.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (idmUserId ^ (idmUserId >>> 32));
        return result;
    }
}

package cz.muni.ics.kypo.commons.model;


import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "IDM_GROUP_REF")
public class IDMGroupRef {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "GROUP_ID", unique = true, nullable = false)
    private long idmGroupRef;

    @ManyToMany
    @JoinTable(name = "USER_IDM_GROUP", joinColumns = {@JoinColumn(name = "IDM_GROUP_REF_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_REF_ID")})
    private List<UserRef> users = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "IDM_GROUP_ROLE", joinColumns = @JoinColumn(name = "IDM_GROUP_REF_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roleTypes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IDMGroupRef that = (IDMGroupRef) o;

        if (idmGroupRef != that.idmGroupRef) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (users != null ? !users.equals(that.users) : that.users != null) return false;
        return roleTypes != null ? roleTypes.equals(that.roleTypes) : that.roleTypes == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (idmGroupRef ^ (idmGroupRef >>> 32));
        result = 31 * result + (users != null ? users.hashCode() : 0);
        result = 31 * result + (roleTypes != null ? roleTypes.hashCode() : 0);
        return result;
    }
}

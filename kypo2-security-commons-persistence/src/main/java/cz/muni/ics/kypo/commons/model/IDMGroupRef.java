package cz.muni.ics.kypo.commons.model;


import org.springframework.util.Assert;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
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
    private long idmGroupId;

    @ManyToMany
    @JoinTable(name = "USER_IDM_GROUP", joinColumns = {@JoinColumn(name = "IDM_GROUP_REF_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_REF_ID")})
    private List<UserRef> users = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "IDM_GROUP_ROLE", joinColumns = @JoinColumn(name = "IDM_GROUP_REF_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roles = new HashSet<>();;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getIdmGroupId() {
        return idmGroupId;
    }

    public void setIdmGroupId(long idmGroupId) {
        this.idmGroupId = idmGroupId;
    }

    public List<UserRef> getUsers() {
        return users;
    }

    public void setUsers(List<UserRef> users) {
        this.users = users;
    }

    public void addUser(UserRef user) {
        this.users.add(user);
    }

    public void removeUser(UserRef user) {
        this.users.remove(user);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IDMGroupRef that = (IDMGroupRef) o;

        if (idmGroupId != that.idmGroupId) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (users != null ? !users.equals(that.users) : that.users != null) return false;
        return roles != null ? roles.equals(that.roles) : that.roles == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (idmGroupId ^ (idmGroupId >>> 32));
        result = 31 * result + (users != null ? users.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }
}

package cz.muni.ics.kypo.commons.model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "USER_REF")
public class UserRef {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "USER_LOGIN", unique = true, nullable = false)
    private String login;

    @ManyToMany
    @JoinTable(name = "USER_IDM_GROUP", joinColumns = {@JoinColumn(name = "USER_REF_ID")},
            inverseJoinColumns = {@JoinColumn(name = "IDM_GROUP_REF_ID")})
    private List<IDMGroupRef> groups = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<IDMGroupRef> getGroups() {
        return groups;
    }

    public void setGroups(List<IDMGroupRef> groups) {
        this.groups = groups;
    }

    public void addGroup(IDMGroupRef group) {
        this.groups.add(group);
    }

    public void removeGroup(IDMGroupRef group) {
        this.groups.remove(group);
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRef userRef = (UserRef) o;

        if (id != null ? !id.equals(userRef.id) : userRef.id != null) return false;
        return login != null ? login.equals(userRef.login) : userRef.login == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }
}

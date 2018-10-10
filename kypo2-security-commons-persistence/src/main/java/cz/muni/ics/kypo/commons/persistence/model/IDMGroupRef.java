package cz.muni.ics.kypo.commons.persistence.model;

import javax.persistence.*;
import java.util.HashSet;
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

		IDMGroupRef groupRef = (IDMGroupRef) o;

		if (idmGroupId != groupRef.idmGroupId) return false;
		if (id != null ? !id.equals(groupRef.id) : groupRef.id != null) return false;
		return roles != null ? roles.equals(groupRef.roles) : groupRef.roles == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (int) (idmGroupId ^ (idmGroupId >>> 32));
		result = 31 * result + (roles != null ? roles.hashCode() : 0);
		return result;
	}
}

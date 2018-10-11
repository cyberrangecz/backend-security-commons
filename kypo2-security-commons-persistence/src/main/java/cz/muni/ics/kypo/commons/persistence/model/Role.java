package cz.muni.ics.kypo.commons.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "ROLE")
public class Role {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@JsonIgnoreProperties({"id"})
	private Long id;

	@Column(name = "ROLE_TYPE", unique = true, nullable = false)
	private String roleType;

	public Long getId() {
			return id;
	}

	public void setId(Long id) {
			this.id = id;
	}

	public String getRoleType() {
			return roleType;
	}

	public void setRoleType(String roleType) {
			this.roleType = roleType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Role role = (Role) o;

		if (!id.equals(role.id)) return false;
		return roleType.equals(role.roleType);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + roleType.hashCode();
		return result;
	}
}

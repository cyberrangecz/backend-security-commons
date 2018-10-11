package cz.muni.ics.kypo.commons.security.mapping;

import java.util.Objects;

public class RoleDTO {
	private Long id;

	private String role_type;

	private String name_of_microservice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole_type() {
		return role_type;
	}

	public void setRole_type(String role_type) {
		this.role_type = role_type;
	}

	public String getName_of_microservice() {
		return name_of_microservice;
	}

	public void setName_of_microservice(String name_of_microservice) {
		this.name_of_microservice = name_of_microservice;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RoleDTO roleDTO = (RoleDTO) o;
		return Objects.equals(getId(), roleDTO.getId()) &&
				Objects.equals(getRole_type(), roleDTO.getRole_type()) &&
				Objects.equals(getName_of_microservice(), roleDTO.getName_of_microservice());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getId(), getRole_type(), getName_of_microservice());
	}

	@Override public String toString() {
		return "RoleDTO{" + "id=" + id + ", role_type='" + role_type + '\'' + ", name_of_microservice='" + name_of_microservice + '\''
				+ '}';
	}
}

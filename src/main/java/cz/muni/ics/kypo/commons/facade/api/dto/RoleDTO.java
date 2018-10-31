package cz.muni.ics.kypo.commons.facade.api.dto;

public class RoleDTO {

	private Long id;
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

		RoleDTO roleDTO = (RoleDTO) o;

		if (id != null ? !id.equals(roleDTO.id) : roleDTO.id != null) return false;
		return roleType != null ? roleType.equals(roleDTO.roleType) : roleDTO.roleType == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (roleType != null ? roleType.hashCode() : 0);
		return result;
	}
}

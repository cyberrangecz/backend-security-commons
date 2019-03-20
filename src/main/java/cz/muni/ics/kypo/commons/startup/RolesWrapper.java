package cz.muni.ics.kypo.commons.startup;

public class RolesWrapper {

    private String role;
    private boolean isDefault;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return "RolesWrapper{" +
                "role='" + role + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}

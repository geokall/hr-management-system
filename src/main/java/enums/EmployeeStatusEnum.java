package enums;

public enum EmployeeStatusEnum {

    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String label;

    EmployeeStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static EmployeeStatusEnum getEmployeeStatusEnum(String type) {

        for (EmployeeStatusEnum gender : EmployeeStatusEnum.values()) {
            if (gender.getLabel().equals(type)) {
                return gender;
            }
        }

        return null;
    }
}

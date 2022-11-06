package enums;

public enum MaritalStatusEnum {

    SINGLE("Single"),
    MARRIED("Married"),
    COMMON_LAW("Common Law"),
    DOMESTIC_PARTNERSHIP("Domestic Partnership");

    private final String label;

    MaritalStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static MaritalStatusEnum getMaritalStatusEnum(String type) {

        for (MaritalStatusEnum gender : MaritalStatusEnum.values()) {
            if (gender.getLabel().equals(type)) {
                return gender;
            }
        }

        return null;
    }
}

package enums;

public enum EthnicityEnum {

    BLACK_OR_AFRICAN_AMERICAN("Black or African American"),
    ASIAN("Asian"),
    WHITE("White");

    private final String label;

    EthnicityEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static EthnicityEnum getEthnicityEnum(String type) {

        for (EthnicityEnum gender : EthnicityEnum.values()) {
            if (gender.getLabel().equals(type)) {
                return gender;
            }
        }

        return null;
    }
}

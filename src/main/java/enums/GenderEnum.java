package enums;

public enum GenderEnum {

    MALE("Male"),
    FEMALE("Female");

    private final String label;

    GenderEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static GenderEnum gerGenderEnum(String type) {

        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.getLabel().equals(type)) {
                return gender;
            }
        }

        return null;
    }
}

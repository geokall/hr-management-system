package enums;

public enum DegreeEnum {

    BACHELOR("Bachelor's"),
    MASTER("Master's"),
    PHD("PhD");

    private final String label;

    DegreeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static DegreeEnum getDegreeStatusEnum(String type) {

        for (DegreeEnum degree : DegreeEnum.values()) {
            if (degree.getLabel().equals(type)) {
                return degree;
            }
        }

        return null;
    }
}

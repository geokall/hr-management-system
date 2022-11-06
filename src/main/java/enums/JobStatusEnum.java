package enums;

public enum JobStatusEnum {

    FULL_TIME("Full-Time"),
    PART_TIME("Part-Time"),
    CONTRACT("Contract");

    private final String label;

    JobStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static JobStatusEnum gerJobStatusEnum(String type) {

        for (JobStatusEnum gender : JobStatusEnum.values()) {
            if (gender.getLabel().equals(type)) {
                return gender;
            }
        }

        return null;
    }
}

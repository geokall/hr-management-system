package enums;

public enum JobCategoryEnum {

    SERVICE_WORKERS("Service Workers"),
    OPERATIVES("Operatives"),
    EXECUTIVE_SENIOR_LEVEL_OFFICIALS_MANAGERS("Executive/Senior Level Officials and Managers");

    private final String label;

    JobCategoryEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static JobCategoryEnum getJobCategoryEnum(String type) {

        for (JobCategoryEnum gender : JobCategoryEnum.values()) {
            if (gender.getLabel().equals(type)) {
                return gender;
            }
        }

        return null;
    }
}

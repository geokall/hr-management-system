package enums;

public enum PayTypeEnum {

    SALARY("Salary"),
    HOURLY("Hourly"),
    COMMISSION_ONLY("Commission only");

    private final String label;

    PayTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static PayTypeEnum getPayRateEnum(String type) {

        for (PayTypeEnum payRate : PayTypeEnum.values()) {
            if (payRate.getLabel().equals(type)) {
                return payRate;
            }
        }

        return null;
    }
}

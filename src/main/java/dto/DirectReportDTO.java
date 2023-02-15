package dto;

import lombok.Data;

import java.util.Objects;

@Data
public class DirectReportDTO {

    private Long id;

    private Long directReportUserId;

    private String name;

    private String surname;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectReportDTO that = (DirectReportDTO) o;
        return Objects.equals(directReportUserId, that.directReportUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(directReportUserId);
    }
}

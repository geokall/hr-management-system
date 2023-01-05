package entity;

import enums.JobStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "HUA_WORK_INFORMATION")
@Getter
@Setter
@NoArgsConstructor
public class HuaWorkInformation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "effective_date")
    private Date effectiveDate;

    @Column(name = "job_title")
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_status")
    private JobStatusEnum jobStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private HuaLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id", referencedColumnName = "id")
    private HuaDivision division;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private HuaUser manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private HuaUser user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HuaWorkInformation that = (HuaWorkInformation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

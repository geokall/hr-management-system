package dto;

public class IdNameProjectionDTO {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Number id) {
        this.id = id.longValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

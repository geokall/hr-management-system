package dto;

import lombok.Data;

@Data
public class AddressDTO {

    private String street1;

    private String street2;

    private String city;

    private String province;

    private String postalCode;

    private String country;
}

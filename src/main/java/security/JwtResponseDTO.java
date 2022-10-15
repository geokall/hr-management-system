package security;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonDeserialize(builder = JwtResponseDTO.JwtResponseDTOBuilder.class)
public class JwtResponseDTO {

    private Long id;

    private String token;

    private String username;

    private String email;

    private String role;
}

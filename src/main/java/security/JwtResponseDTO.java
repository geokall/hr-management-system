package security;

import lombok.Data;

@Data
public class JwtResponseDTO {

    private Long id;

    private String token;

    private String username;

    private String email;

    private String role;
}

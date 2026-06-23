package com.example.purchase_service.dto.response;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

	private String token;

	private String username;

	private Set<String> roles;
}

package com.induce.profitability_calculation_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
  @NotBlank(message = "The name cannot be empty")
  private String firstname;

  @NotBlank(message = "The last name cannot be empty")
  private String lastname;

  @NotBlank(message = "Email cannot be empty")
  @Email(message = "Incorrect email format")
  private String email;

  @NotBlank(message = "The password cannot be empty")
  private String password;
}

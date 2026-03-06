package com.induce.profitability_calculation_service.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.induce.profitability_calculation_service.model.User;
import com.induce.profitability_calculation_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserGraphQLController {

  private final UserRepository userRepository;

  @QueryMapping
  public User getUserById(@Argument Long id) {
    return userRepository.findById(id).orElse(null);
  }
}

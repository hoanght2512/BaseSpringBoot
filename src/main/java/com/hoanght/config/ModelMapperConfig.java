package com.hoanght.config;

import com.hoanght.entity.Role;
import com.hoanght.entity.User;
import com.hoanght.payload.response.UserResponse;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Converter<Set<Role>, Set<String>> rolesConverter = context -> context.getSource().stream().map(
                Role::getName).collect(Collectors.toSet());

        modelMapper.typeMap(User.class, UserResponse.class).addMappings(
                mapper -> mapper.using(rolesConverter).map(User::getRoles, UserResponse::setRoles));

        return modelMapper;
    }
}

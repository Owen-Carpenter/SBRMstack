package dev.carpenter.sbrmstack.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import dev.carpenter.sbrmstack.models.Role;
import dev.carpenter.sbrmstack.models.ERole;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
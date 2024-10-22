package tech.collabboard.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.collabboard.springsecurity.entities.Role;
import tech.collabboard.springsecurity.entities.User;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}

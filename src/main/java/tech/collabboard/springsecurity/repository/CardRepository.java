package tech.collabboard.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.collabboard.springsecurity.entities.Card;


@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}

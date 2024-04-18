package org.konradchrzanowski.email.repository;


import org.konradchrzanowski.email.domain.SentEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SentEmailRepository extends JpaRepository<SentEmail, Long> {
}

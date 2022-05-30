package eu.cec.digit.comref.interview.persistent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.cec.digit.comref.interview.persistent.domain.Watch;

import java.util.Optional;

public interface WatchRepository extends JpaRepository<Watch, String>{
    boolean existsByName(String name);
    Optional<Watch> findByName(String name);

    void deleteByName(String name);
}

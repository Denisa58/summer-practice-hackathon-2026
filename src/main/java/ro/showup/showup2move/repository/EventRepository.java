package ro.showup.showup2move.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.showup.showup2move.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    
}
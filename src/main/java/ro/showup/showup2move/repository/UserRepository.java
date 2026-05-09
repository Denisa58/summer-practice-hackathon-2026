package ro.showup.showup2move.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ro.showup.showup2move.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
        List<User> findByIsAvailableTrueAndSportsContaining(String sport);
}
package ro.showup.showup2move;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Permite accesul de la alte aplicații (util pentru Frontend mai târziu)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Acest punct (Endpoint) returnează toți utilizatorii din baza de date
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Acest punct permite adăugarea unui utilizator nou prin cod/cerere POST
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
package ro.showup.showup2move.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.showup.showup2move.model.Event;
import ro.showup.showup2move.model.User;
import ro.showup.showup2move.repository.EventRepository;
import ro.showup.showup2move.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}/toggle-status")
    public Map<String, Object> toggleStatus(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setAvailable(!user.isAvailable());
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);

        // LOGICA SMART MATCHING
        if (user.isAvailable() && user.getSports() != null && !user.getSports().isEmpty()) {
            String mainSport = user.getSports().get(0);

            // A. Verificăm dacă există deja un meci creat pentru acest sport care are locuri libere
            List<Event> existingEvents = eventRepository.findAll().stream()
                .filter(e -> e.getSport().equalsIgnoreCase(mainSport))
                .filter(e -> e.getParticipants().size() < (e.getMaxParticipants() > 0 ? e.getMaxParticipants() : 10))
                .collect(Collectors.toList());

            if (!existingEvents.isEmpty()) {
                Event match = existingEvents.get(0);
                response.put("suggestion", "📍 Există deja un meci de " + mainSport + " la " + match.getLocation() + ". Vrei să te alături?");
                response.put("type", "JOIN_EXISTING");
            } else {
                // B. Dacă nu sunt meciuri, căutăm alți jucători disponibili
                List<User> availablePlayers = userRepository.findByIsAvailableTrueAndSportsContaining(mainSport);
                
                // Luăm numele celorlalți (fără userul curent)
                String otherPlayers = availablePlayers.stream()
                    .filter(p -> !p.getId().equals(user.getId()))
                    .map(User::getName)
                    .limit(2)
                    .collect(Collectors.joining(", "));

                if (availablePlayers.size() >= 3) {
                    response.put("suggestion", "🔥 " + otherPlayers + " și alții sunt gata de " + mainSport + ". Vrei să organizezi un meci nou?");
                    response.put("type", "CREATE_NEW");
                }
            }
        }
        return response;
    }
   @PostMapping("/analyze-bio")
public List<String> analyzeBio(@RequestBody Map<String, String> request) {
    String bio = request.get("bio").toLowerCase();
    
    // Dicționar inteligent de cuvinte cheie
    Map<String, List<String>> keywords = new HashMap<>();
    keywords.put("Football", List.of("football", "soccer", "pitch", "ball", "goal", "kick"));
    keywords.put("Basketball", List.of("basketball", "hoops", "dunk", "court", "basket"));
    keywords.put("Tennis", List.of("tennis", "racket", "court", "match", "serve"));
    keywords.put("Swimming", List.of("swimming", "pool", "laps", "water", "swim"));
    keywords.put("Volleyball", List.of("volleyball", "spike", "net", "serve", "beach volley"));

    return keywords.entrySet().stream()
            .filter(entry -> entry.getValue().stream().anyMatch(bio::contains))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
}
@PostMapping("/{userId}/invite/{eventId}")
public ResponseEntity<String> inviteUser(@PathVariable Long userId, @PathVariable Long eventId) {
   
    return ResponseEntity.ok("Invitation sent successfully!");
}
}
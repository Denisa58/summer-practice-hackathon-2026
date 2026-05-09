package ro.showup.showup2move.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Importă modelul User
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // Importă repository-ul User
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.showup.showup2move.model.Event;
import ro.showup.showup2move.repository.EventRepository;
import ro.showup.showup2move.repository.UserRepository;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository; // <--- Avem nevoie de asta pentru Join

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @PostMapping("/{eventId}/message")
    public ResponseEntity<Event> addMessage(@PathVariable Long eventId, @RequestBody Map<String, String> payload) {
        return eventRepository.findById(eventId).map(event -> {
            String userName = payload.get("userName");
            String text = payload.get("text");
            
            if (userName != null && !text.trim().isEmpty()) {
                event.getDiscussions().add(userName + ": " + text);
                eventRepository.save(event);
                return ResponseEntity.ok(event);
            }
            return ResponseEntity.badRequest().<Event>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // --- METODA DE JOIN REPARATĂ ---
    @PutMapping("/{eventId}/join/{userId}")
    public ResponseEntity<Event> joinMatch(@PathVariable Long eventId, @PathVariable Long userId) {
        return eventRepository.findById(eventId).map(event -> {
            return userRepository.findById(userId).map(user -> {
                // Verificăm să nu fie deja în listă
                if (!event.getParticipants().contains(user)) {
                    event.getParticipants().add(user);
                    eventRepository.save(event); // SALVĂM MODIFICAREA
                }
                return ResponseEntity.ok(event);
            }).orElse(ResponseEntity.notFound().build());
        }).orElse(ResponseEntity.notFound().build());
    }
}
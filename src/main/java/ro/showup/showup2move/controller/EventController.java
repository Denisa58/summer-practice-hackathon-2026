package ro.showup.showup2move.controller;

import java.util.List;

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
@RequestMapping("/api/events")
@CrossOrigin("*") // Permite cererile de la frontend
public class EventController {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    // Injectăm ambele repository-uri prin constructor
    public EventController(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    // 1. Obține toate evenimentele (meciurile)
    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // 2. Creează un eveniment nou
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    // 3. Înregistrează un utilizator la un eveniment (Join Match)
    @PutMapping("/{eventId}/join/{userId}")
    public Event joinEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        // Căutăm evenimentul în baza de date
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        // Căutăm utilizatorul care vrea să se înscrie
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verificăm dacă utilizatorul nu este deja înscris ca să nu îl adăugăm de două ori
        if (!event.getParticipants().contains(user)) {
            event.getParticipants().add(user);
        }

        // Salvăm modificarea
        return eventRepository.save(event);
    }
}
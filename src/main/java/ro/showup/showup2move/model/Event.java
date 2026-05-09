package ro.showup.showup2move.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sport;
    private String location;
    private String eventTime;
    private String description;
    private int maxParticipants = 10; 

    // Această adnotare creează automat un tabel secundar pentru mesaje
    @ElementCollection
    @CollectionTable(name = "event_discussions", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "message")
    private List<String> discussions = new ArrayList<>();

    @ManyToMany
    private List<User> participants;

    public Event() {}

    // Getters și Setters pentru Chat
    public List<String> getDiscussions() { return discussions; }
    public void setDiscussions(List<String> discussions) { this.discussions = discussions; }

    // Restul de Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSport() { return sport; }
    public void setSport(String sport) { this.sport = sport; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<User> getParticipants() { return participants; }
    public void setParticipants(List<User> participants) { this.participants = participants; }

    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }
}
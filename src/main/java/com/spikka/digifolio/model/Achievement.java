// src/main/java/com/spikka/digifolio/model/Achievement.java
package com.spikka.digifolio.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "achievements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Achievement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User student;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private AchievementType type;

    private LocalDate date;

    private String tags;      // CSV

    @Column(name = "file_path", length = 255)
    private String filePath;

    @OneToMany(mappedBy = "achievement", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "achievement", cascade = CascadeType.ALL)
    private List<Rating> ratings;
}

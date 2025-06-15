package com.spikka.digifolio.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ratings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Rating {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Achievement achievement;

    @ManyToOne(optional = false)
    private User author;   // TEACHER

    private int stars;     // 1–5

    private LocalDate createdAt;
}
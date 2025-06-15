package com.spikka.digifolio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Interest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User student;

    private String name;
}
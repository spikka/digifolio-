package com.spikka.digifolio.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "work_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Work {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User student;

    private String company;
    private String position;
    private LocalDate fromDate;
    private LocalDate toDate;
}
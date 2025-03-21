package sn.school.examenfx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "emargements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Emargement {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull(message = "La date d'émargement est obligatoire")
  private LocalDate date;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Statut est requis")
  @Column(nullable = false)
  private Statut statut;

  @ManyToOne
  @JoinColumn(name = "professeur_id", nullable = false)
  @NotNull(message = "L'émargement doit être associé à un professeur")
  private User professeur;

  @ManyToOne
  @JoinColumn(name = "cours_id", nullable = false)
  @NotNull(message = "L'émargement doit être associé à un cours")
  private Cours cours;
}


package sn.school.examenfx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "cours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Cours {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank(message = "Le nom du cours est obligatoire")
  private String nom;

  @NotBlank(message = "La description du cours est obligatoire")
  private String description;

  @NotNull(message = "L'heure de début est obligatoire")
  private LocalTime heureDebut;

  @NotNull(message = "L'heure de fin est obligatoire")
  private LocalTime heureFin;

  @ManyToOne
  @JoinColumn(name = "salle_id", nullable = false)
  @NotNull(message = "Le cours doit être associé à une salle")
  private Salle salle;

  @ManyToOne
  @NotNull(message = "Le cours doit être associé à un professeur")
  @JoinColumn(name = "professeur_id", nullable = false)
  private User professeur;
}
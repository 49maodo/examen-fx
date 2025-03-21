package sn.school.examenfx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "salles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Salle {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank(message = "Le libellé de la salle est obligatoire")
  @Column(unique = true, nullable = false)
  private String libelle;

  @Override
  public String toString() {
    return this.libelle;
  }
}
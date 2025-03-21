package sn.school.examenfx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Notification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank(message = "Le message ne peut pas être vide")
  private String message;

  @NotNull(message = "La date d'envoi est obligatoire")
  private LocalDateTime dateEnvoi;

  @ManyToOne
  @JoinColumn(name = "destinataire_id", nullable = false)
  @NotNull(message = "La notification doit avoir un destinataire")
  private User destinataire;
}
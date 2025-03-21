package sn.school.examenfx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank(message = "Le nom est obligatoire")
  @Size(min = 2, max = 50, message = "Le nom doit avoir entre 2 et 50 caractères")
  private String nom;

  @NotBlank(message = "Le prénom est obligatoire")
  @Size(min = 2, max = 50, message = "Le prénom doit avoir entre 2 et 50 caractères")
  private String prenom;

  @Email(message = "L'email doit être valide")
  @NotBlank(message = "L'email est obligatoire")
  @Column(unique = true, nullable = false)
  private String email;

  @NotBlank(message = "Le mot de passe est obligatoire")
  @Size(min = 6, message = "Le mot de passe doit avoir au moins 6 caractères")
  private String password;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Le role est obligatoire")
  @Column(nullable = false)
  private Role role;

  @OneToMany(mappedBy = "professeur")
  private Set<Cours> cours;

  @Override
  public String toString() {
    return this.prenom + " " + this.nom;
  }
}


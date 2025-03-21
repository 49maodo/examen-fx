package sn.school.examenfx.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sn.school.examenfx.JPAUtil;
import sn.school.examenfx.entities.Salle;

import java.util.List;

public class SalleImpl implements ISalle{
  private final EntityManager entityManager;
  public SalleImpl() {
    this.entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
  }
  @Override
  public void add(Salle salle) {
    try {
      TypedQuery<Salle> query = entityManager.createQuery("SELECT s FROM Salle s WHERE s.libelle = :libelle", Salle.class);
      query.setParameter("libelle", salle.getLibelle());
      if (!query.getResultList().isEmpty()) {
        throw new RuntimeException("Ce libellé est déjà utilisé !");
      }
      entityManager.getTransaction().begin();
      entityManager.persist(salle);  // Persiste la nouvelle salle
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback(); // Annule la transaction en cas d'erreur
      }
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public void update(Salle salle) {
    try {
      // Vérification de l'unicité du libellé en excluant la salle actuelle
      TypedQuery<Salle> query = entityManager.createQuery("SELECT s FROM Salle s WHERE s.libelle = :libelle AND s.id != :id", Salle.class);
      query.setParameter("libelle", salle.getLibelle());
      query.setParameter("id", salle.getId());  // Exclure la salle actuelle
      if (!query.getResultList().isEmpty()) {
        throw new RuntimeException("Ce libellé est déjà utilisé !");
      }

      // Si l'unicité est respectée, on met à jour la salle
      entityManager.getTransaction().begin();
      entityManager.merge(salle);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();  // Annule la transaction en cas d'erreur
      }
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public void delete(int id) {
    try {
      entityManager.getTransaction().begin();
      Salle salle = entityManager.find(Salle.class, id);  // Recherche la salle par son ID
      if (salle != null) {
        entityManager.remove(salle);  // Supprime la salle si elle existe
      }
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();  // Annule la transaction en cas d'erreur
      }
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public ObservableList<Salle> getAll() {
    try {
      entityManager.getTransaction().begin();
      List<Salle> salles = entityManager.createQuery("SELECT s FROM Salle s", Salle.class).getResultList();  // Récupère toutes les salles
      entityManager.getTransaction().commit();
      return FXCollections.observableArrayList(salles);  // Convertit la liste en ObservableList pour JavaFX
    } catch (Exception e) {
      e.printStackTrace();
      return FXCollections.observableArrayList();  // Retourne une liste vide en cas d'erreur
    }
  }

  @Override
  public Salle get(int id) {
    try {
      entityManager.getTransaction().begin();
      Salle salle = entityManager.find(Salle.class, id);  // Recherche la salle par son ID
      entityManager.getTransaction().commit();
      return salle;
    } catch (Exception e) {
      e.printStackTrace();
      return null;  // Retourne null en cas d'erreur
    }
  }
}

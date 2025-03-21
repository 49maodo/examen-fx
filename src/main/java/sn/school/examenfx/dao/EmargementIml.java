package sn.school.examenfx.dao;

import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sn.school.examenfx.JPAUtil;
import sn.school.examenfx.entities.Emargement;

import java.util.List;

public class EmargementIml implements IEmargement{
  private final EntityManager entityManager;
  public EmargementIml() {
    this.entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
  }

  @Override
  public void add(Emargement emargement) {
    try {
      entityManager.getTransaction().begin();
      entityManager.persist(emargement); // Persiste l'émargement dans la base de données
      entityManager.getTransaction().commit(); // Commit de la transaction
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback(); // Rollback en cas d'erreur
      }
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public void update(Emargement emargement) {
    try {
      entityManager.getTransaction().begin();
      entityManager.merge(emargement); // Utilise `merge` pour mettre à jour un émargement existant
      entityManager.getTransaction().commit(); // Commit de la transaction
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback(); // Rollback en cas d'erreur
      }
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public void delete(int id) {
    try {
      entityManager.getTransaction().begin();
      Emargement emargement = entityManager.find(Emargement.class, id); // Trouve l'émargement par son ID
      if (emargement != null) {
        entityManager.remove(emargement); // Supprime l'émargement
      }
      entityManager.getTransaction().commit(); // Commit de la transaction
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback(); // Rollback en cas d'erreur
      }
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public ObservableList<Emargement> getAll() {
    try {
      List<Emargement> emargements = entityManager.createQuery("SELECT e FROM Emargement e", Emargement.class)
              .getResultList(); // Récupère tous les émargements
      return FXCollections.observableArrayList(emargements); // Convertit la liste en ObservableList
    } catch (Exception e) {
      e.printStackTrace();
    }
    return FXCollections.observableArrayList();
  }

  @Override
  public Emargement get(int id) {
    try {
      return entityManager.find(Emargement.class, id); // Recherche l'émargement par son ID
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}

package sn.school.examenfx.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sn.school.examenfx.JPAUtil;
import sn.school.examenfx.entities.Cours;
import sn.school.examenfx.entities.Salle;
import sn.school.examenfx.entities.User;

import java.util.List;

public class CoursImpl implements ICours{
  private final EntityManager entityManager;
  public CoursImpl() {
    this.entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
  }

  @Override
  public void add(Cours cours) {
    try {
      entityManager.getTransaction().begin();
      entityManager.persist(cours);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public void update(Cours cours) {
    try {
      entityManager.getTransaction().begin();
      entityManager.merge(cours);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public void delete(int id) {
    try {
      entityManager.getTransaction().begin();
      Cours cours = entityManager.find(Cours.class, id);
      if (cours != null) {
        entityManager.remove(cours);
      }
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public ObservableList<Cours> getAll() {
    try {
      entityManager.getTransaction().begin();
      List<Cours> coursList = entityManager.createQuery("SELECT c FROM Cours c", Cours.class).getResultList();
      entityManager.getTransaction().commit();
      return FXCollections.observableArrayList(coursList); // retourne une ObservableList pour être utilisé dans la vue
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      e.printStackTrace();
    }
    return FXCollections.observableArrayList();
  }

  @Override
  public Cours get(int id) {
    try {
      entityManager.getTransaction().begin();
      Cours cours = entityManager.find(Cours.class, id);
      entityManager.getTransaction().commit();
      return cours;
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      e.printStackTrace();
    }
    return null;
  }

  public List<Cours> getAllByProfesseur(User professeur) {
    TypedQuery<Cours> query = entityManager.createQuery(
            "SELECT c FROM Cours c WHERE c.professeur = :professeur", Cours.class
    );
    query.setParameter("professeur", professeur);
    return query.getResultList();
  }

  public List<Cours> getAllBySalle(Salle salle) {
    TypedQuery<Cours> query = entityManager.createQuery(
            "SELECT c FROM Cours c WHERE c.salle = :salle", Cours.class
    );
    query.setParameter("salle", salle);
    return query.getResultList();
  }
}

package sn.school.examenfx.dao;

import jakarta.persistence.TypedQuery;
import jakarta.validation.ConstraintViolationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sn.school.examenfx.JPAUtil;
import sn.school.examenfx.entities.User;
import jakarta.persistence.EntityManager;

import java.util.List;


public class UserImpl implements IUser{
  private final EntityManager entityManager;

  public UserImpl() {
    // Assure-toi de ne créer qu'une instance d'EntityManager.
    this.entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
  }

  public boolean emailExists(String email) {
    TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
    query.setParameter("email", email);
    return query.getSingleResult() > 0;
  }

  @Override
  public void add(User entity) {
    try {
      entityManager.getTransaction().begin();
      TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
      query.setParameter("email", entity.getEmail());
      List<User> existingUsers = query.getResultList();

      if (!existingUsers.isEmpty()) {
        throw new RuntimeException("Cet email est déjà utilisé !");
      }
      entityManager.persist(entity);
      entityManager.getTransaction().commit();
    } catch (Exception e){
      if(entityManager.getTransaction().isActive()){
        entityManager.getTransaction().rollback();
      }
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public void update(User user) {
    try {
      entityManager.getTransaction().begin();
      TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u " +
              "WHERE u.email = :email and u.id!= :idU", User.class);
      query.setParameter("email", user.getEmail());
      query.setParameter("idU", user.getId());
      List<User> existingUsers = query.getResultList();

      if (!existingUsers.isEmpty()) {
        throw new RuntimeException("Cet email est déjà utilisé !");
      }
      entityManager.merge(user);
      entityManager.getTransaction().commit();
    } catch (Exception e){
      if(entityManager.getTransaction().isActive()){
        entityManager.getTransaction().rollback();
      }
      e.printStackTrace();
      throw e;
    }
  }

  public void addOrUpdate(User user) {
    try {
      if (emailExists(user.getEmail())) {
        throw new RuntimeException("Cet email est déjà utilisé !");
      }
      entityManager.getTransaction().begin();
      if (user.getId()== null) {
        entityManager.persist(user); // Ajout d'un nouvel utilisateur
      } else {
        entityManager.merge(user);   // Mise à jour de l'utilisateur existant
      }
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      e.printStackTrace();
    }
  }


  @Override
  public void delete(int id) {
    try {
      entityManager.getTransaction().begin();
      User user = entityManager.find(User.class, id);
      entityManager.remove(user);
      entityManager.getTransaction().commit();
    } catch (Exception e){
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public ObservableList<User> getAll() {
    List<User> users = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    return FXCollections.observableArrayList(users);
  }

  @Override
  public User get(int id) {
    return entityManager.find(User.class, id);
  }

  @Override
  public void login(String email, String password) {

  }

  public List<User> getAllProfessors() {
    return entityManager.createQuery("SELECT u FROM User u WHERE u.role = 'PROFESSEUR'", User.class)
            .getResultList();
  }
}

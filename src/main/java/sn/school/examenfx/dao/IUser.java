package sn.school.examenfx.dao;

import sn.school.examenfx.entities.User;

public interface IUser extends IRepository<User> {
  void login(String email, String password);
}

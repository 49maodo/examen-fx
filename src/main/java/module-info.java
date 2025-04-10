module sn.school.examenfx {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.xml;
  requires jakarta.persistence;
  requires static lombok;
  requires jakarta.validation;
  requires org.hibernate.validator;
  requires org.hibernate.commons.annotations;
  requires org.hibernate.orm.core;
  requires de.jensd.fx.glyphs.commons;
  requires de.jensd.fx.glyphs.fontawesome;
  requires jbcrypt;
  requires jakarta.mail;


  opens sn.school.examenfx to javafx.fxml;
  exports sn.school.examenfx;

  opens sn.school.examenfx.dao to javafx.fxml;
  exports sn.school.examenfx.dao;

  opens sn.school.examenfx.entities  to org.hibernate.orm.core,org.hibernate.validator, jakarta.persistence;
  exports sn.school.examenfx.entities;

  opens sn.school.examenfx.controllers to javafx.fxml;
  exports sn.school.examenfx.controllers;
}
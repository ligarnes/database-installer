package net.alteiar.db.installer;

public interface SqlScript {

  String getName();

  String getDescription();

  String getQuery();
}

package net.alteiar.db.installer.xml.location.resolver;

import java.util.ArrayList;
import java.util.List;

import net.alteiar.db.installer.SqlScript;
import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.sql.LocationType;

public class LocationTypeResolverComposite implements ScriptResolver {

  private final List<ScriptResolver> resolvers;

  public LocationTypeResolverComposite() {

    this.resolvers = new ArrayList<ScriptResolver>();
  }

  public void addResolver(ScriptResolver resolver) {

    this.resolvers.add(resolver);
  }

  private ScriptResolver getResolver(LocationType location) {

    ScriptResolver found = null;
    for (ScriptResolver resolver : resolvers) {

      if (resolver.accept(location)) {

        found = resolver;
        break;
      }
    }

    return found;
  }

  @Override
  public boolean accept(LocationType location) {

    return getResolver(location) != null;
  }

  @Override
  public SqlScript getScript(LocationType location, String scriptName) throws DbScriptException {

    ScriptResolver resolver = getResolver(location);

    if (resolver == null) {

      throw new IllegalArgumentException(String.format("The location type %s is not supported.%n"
                                                       + "You should use accept before to ensure that the resolver support the provided type",
                                                       location.getClass().getName()));
    }

    return resolver.getScript(location, scriptName);
  }
}

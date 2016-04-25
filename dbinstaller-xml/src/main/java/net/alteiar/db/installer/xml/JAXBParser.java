package net.alteiar.db.installer.xml;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;

import net.alteiar.db.installer.exception.ParsingException;

class JAXBParser {

  private static final JAXBParser INSTANCE = new JAXBParser();

  private JAXBContext jaxbContext;

  private JAXBParser() {

    try {

      jaxbContext = JAXBContext.newInstance("net.alteiar.sql");
    } catch (JAXBException ex) {

      LogManager.getLogger(getClass()).error("Fail to create the JAXBContext", ex);
    }
  }

  public static JAXBParser getInstance() {

    return INSTANCE;
  }

  @SuppressWarnings("unchecked")
  public <E> E unmarshall(InputStream inputStream) throws ParsingException {

    E value = null;

    try {

      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

      JAXBElement<E> jElement = (JAXBElement<E>) unmarshaller.unmarshal(inputStream);

      value = jElement.getValue();

    } catch (JAXBException e) {
      throw new ParsingException("Fail to unmarshall the input", e);
    }

    return value;
  }

  public <E> void marshall(OutputStream out, JAXBElement<E> element) throws ParsingException {

    try {
      Marshaller marshaller = jaxbContext.createMarshaller();

      marshaller.marshal(element, out);

    } catch (JAXBException e) {
      throw new ParsingException(String.format("Fail to marshall the element %s", element.getValue()), e);
    }
  }
}

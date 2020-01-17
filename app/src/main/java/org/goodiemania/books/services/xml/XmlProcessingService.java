package org.goodiemania.books.services.xml;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlProcessingService {
    private final XPath xpathFactory;
    private final DocumentBuilder builder;

    /**
     * Constructs a new instance of the XML processing service.
     */
    public XmlProcessingService() {
        try {
            this.xpathFactory = XPathFactory.newInstance().newXPath();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setCoalescing(true);
            this.builder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Given a String XML document, returns the XML document representation of the string.
     *
     * @param xmlDocument The string XML document
     * @return The XML Document representing the string
     */
    public XmlDocument parse(final String xmlDocument) {
        try {
            Document document = builder.parse(new InputSource(new StringReader(xmlDocument)));
            return new XmlDocument(document, xpathFactory);
        } catch (SAXException | IOException e) {
            throw new IllegalStateException(e);
        }
    }
}

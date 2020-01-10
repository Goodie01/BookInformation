package org.goodiemania.books.services.misc.xml;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XmlDocument {
    private final Document document;
    private final XPath xpathFactory;

    public XmlDocument(final Document document, final XPath xpathFactory) {
        this.document = document;
        this.xpathFactory = xpathFactory;
    }

    /**
     * Takes a xpath value and returns the string value found by the xpath.
     *
     * @param xpath xpath to search with
     * @return the found string value
     */
    public String getValueAsString(final String xpath) {
        try {
            return (String) this.xpathFactory.compile(xpath)
                    .evaluate(this.document, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Takes a xpath value and returns a node list found by the xpath.
     *
     * @param xpath Xpath to search with
     * @return the found nodelist.
     */
    public NodeList getValue(final String xpath) {
        try {
            return (NodeList) this.xpathFactory.compile(xpath)
                    .evaluate(this.document, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }
}

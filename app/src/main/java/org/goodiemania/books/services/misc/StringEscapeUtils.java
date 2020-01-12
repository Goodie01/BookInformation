package org.goodiemania.books.services.misc;

import java.util.Map;

public class StringEscapeUtils {
    private static final Map<String, String> ESCAPE_MAP;

    static {
        // Mapping to escape ISO-8859-1 characters to their named HTML 3.x equivalents.
        ESCAPE_MAP = Map.<String, String>ofEntries(
                Map.entry("\u00A0", "nbsp"), // non-breaking space
                Map.entry("\u00A1", "iexcl"), // inverted exclamation mark
                Map.entry("\u00A2", "cent"), // cent sign
                Map.entry("\u00A3", "pound"), // pound sign
                Map.entry("\u00A4", "curren"), // currency sign
                Map.entry("\u00A5", "yen"), // yen sign = yuan sign
                Map.entry("\u00A6", "brvbar"), // broken bar = broken vertical bar
                Map.entry("\u00A7", "sect"), // section sign
                Map.entry("\u00A8", "uml"), // diaeresis = spacing diaeresis
                Map.entry("\u00A9", "copy"), // © - copyright sign
                Map.entry("\u00AA", "ordf"), // feminine ordinal indicator
                Map.entry("\u00AB", "laquo"), // left-pointing double angle quotation mark
                Map.entry("\u00AC", "not"), // not sign
                Map.entry("\u00AD", "shy"), // soft hyphen = discretionary hyphen
                Map.entry("\u00AE", "reg"), // ® - registered trademark sign
                Map.entry("\u00AF", "macr"), // macron = spacing macron = overline = APL overbar
                Map.entry("\u00B0", "deg"), // degree sign
                Map.entry("\u00B1", "plusmn"), // plus-minus sign = plus-or-minus sign
                Map.entry("\u00B2", "sup2"), // superscript two = superscript digit two = squared
                Map.entry("\u00B3", "sup3"), // superscript three = superscript digit three = cubed
                Map.entry("\u00B4", "acute"), // acute accent = spacing acute
                Map.entry("\u00B5", "micro"), // micro sign
                Map.entry("\u00B6", "para"), // pilcrow sign = paragraph sign
                Map.entry("\u00B7", "middot"), // middle dot = Georgian comma = Greek middle dot
                Map.entry("\u00B8", "cedil"), // cedilla = spacing cedilla
                Map.entry("\u00B9", "sup1"), // superscript one = superscript digit one
                Map.entry("\u00BA", "ordm"), // masculine ordinal indicator
                Map.entry("\u00BB", "raquo"), // right-pointing double angle quotation mark
                Map.entry("\u00BC", "frac14"), // vulgar fraction one quarter = fraction one quarter
                Map.entry("\u00BD", "frac12"), // vulgar fraction one half = fraction one half
                Map.entry("\u00BE", "frac34"), // vulgar fraction three quarter
                Map.entry("\u00BF", "iquest"), // inverted question mark = turned question mark
                Map.entry("\u00C0", "Agrave"), // А - uppercase A, grave accent
                Map.entry("\u00C1", "Aacute"), // Б - uppercase A, acute accent
                Map.entry("\u00C2", "Acirc"), // В - uppercase A, circumflex accent
                Map.entry("\u00C3", "Atilde"), // Г - uppercase A, tilde
                Map.entry("\u00C4", "Auml"), // Д - uppercase A, umlaut
                Map.entry("\u00C5", "Aring"), // Е - uppercase A, ring
                Map.entry("\u00C6", "AElig"), // Ж - uppercase AE
                Map.entry("\u00C7", "Ccedil"), // З - uppercase C, cedilla
                Map.entry("\u00C8", "Egrave"), // И - uppercase E, grave accent
                Map.entry("\u00C9", "Eacute"), // Й - uppercase E, acute accent
                Map.entry("\u00CA", "Ecirc"), // К - uppercase E, circumflex accent
                Map.entry("\u00CB", "Euml"), // Л - uppercase E, umlaut
                Map.entry("\u00CC", "Igrave"), // М - uppercase I, grave accent
                Map.entry("\u00CD", "Iacute"), // Н - uppercase I, acute accent
                Map.entry("\u00CE", "Icirc"), // О - uppercase I, circumflex accent
                Map.entry("\u00CF", "Iuml"), // П - uppercase I, umlaut
                Map.entry("\u00D0", "ETH"), // Р - uppercase Eth, Icelandic
                Map.entry("\u00D1", "Ntilde"), // С - uppercase N, tilde
                Map.entry("\u00D2", "Ograve"), // Т - uppercase O, grave accent
                Map.entry("\u00D3", "Oacute"), // У - uppercase O, acute accent
                Map.entry("\u00D4", "Ocirc"), // Ф - uppercase O, circumflex accent
                Map.entry("\u00D5", "Otilde"), // Х - uppercase O, tilde
                Map.entry("\u00D6", "Ouml"), // Ц - uppercase O, umlaut
                Map.entry("\u00D7", "times"), // multiplication sign
                Map.entry("\u00D8", "Oslash"), // Ш - uppercase O, slash
                Map.entry("\u00D9", "Ugrave"), // Щ - uppercase U, grave accent
                Map.entry("\u00DA", "Uacute"), // Ъ - uppercase U, acute accent
                Map.entry("\u00DB", "Ucirc"), // Ы - uppercase U, circumflex accent
                Map.entry("\u00DC", "Uuml"), // Ь - uppercase U, umlaut
                Map.entry("\u00DD", "Yacute"), // Э - uppercase Y, acute accent
                Map.entry("\u00DE", "THORN"), // Ю - uppercase THORN, Icelandic
                Map.entry("\u00DF", "szlig"), // Я - lowercase sharps, German
                Map.entry("\u00E0", "agrave"), // а - lowercase a, grave accent
                Map.entry("\u00E1", "aacute"), // б - lowercase a, acute accent
                Map.entry("\u00E2", "acirc"), // в - lowercase a, circumflex accent
                Map.entry("\u00E3", "atilde"), // г - lowercase a, tilde
                Map.entry("\u00E4", "auml"), // д - lowercase a, umlaut
                Map.entry("\u00E5", "aring"), // е - lowercase a, ring
                Map.entry("\u00E6", "aelig"), // ж - lowercase ae
                Map.entry("\u00E7", "ccedil"), // з - lowercase c, cedilla
                Map.entry("\u00E8", "egrave"), // и - lowercase e, grave accent
                Map.entry("\u00E9", "eacute"), // й - lowercase e, acute accent
                Map.entry("\u00EA", "ecirc"), // к - lowercase e, circumflex accent
                Map.entry("\u00EB", "euml"), // л - lowercase e, umlaut
                Map.entry("\u00EC", "igrave"), // м - lowercase i, grave accent
                Map.entry("\u00ED", "iacute"), // н - lowercase i, acute accent
                Map.entry("\u00EE", "icirc"), // о - lowercase i, circumflex accent
                Map.entry("\u00EF", "iuml"), // п - lowercase i, umlaut
                Map.entry("\u00F0", "eth"), // р - lowercase eth, Icelandic
                Map.entry("\u00F1", "ntilde"), // с - lowercase n, tilde
                Map.entry("\u00F2", "ograve"), // т - lowercase o, grave accent
                Map.entry("\u00F3", "oacute"), // у - lowercase o, acute accent
                Map.entry("\u00F4", "ocirc"), // ф - lowercase o, circumflex accent
                Map.entry("\u00F5", "otilde"), // х - lowercase o, tilde
                Map.entry("\u00F6", "ouml"), // ц - lowercase o, umlaut
                Map.entry("\u00F7", "divide"), // division sign
                Map.entry("\u00F8", "oslash"), // ш - lowercase o, slash
                Map.entry("\u00F9", "ugrave"), // щ - lowercase u, grave accent
                Map.entry("\u00FA", "uacute"), // ъ - lowercase u, acute accent
                Map.entry("\u00FB", "ucirc"), // ы - lowercase u, circumflex accent
                Map.entry("\u00FC", "uuml"), // ь - lowercase u, umlaut
                Map.entry("\u00FD", "yacute"), // э - lowercase y, acute accent
                Map.entry("\u00FE", "thorn"), // ю - lowercase thorn, Icelandic
                Map.entry("\u00FF", "yuml")); // я - lowercase y, umlaut
    }

    /**
     * Escapes non XML entities inside of a XML doc.
     *
     * @param string String to be escaped
     * @return the escaped string
     */
    public String escapeHtmlEntitiesInXml(final String string) {
        String escapeString = string;
        for (Map.Entry<String, String> entry : ESCAPE_MAP.entrySet()) {
            String escapedString = "&" + entry.getValue() + ";";
            String replacement = entry.getKey();
            escapeString = escapeString.replace(escapedString, replacement);
        }
        return escapeString;
    }
}

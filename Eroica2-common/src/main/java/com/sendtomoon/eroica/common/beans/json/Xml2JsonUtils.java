package com.sendtomoon.eroica.common.beans.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Xml2JsonUtils {

	Document document = null;

	Set<String> arrayNodes = null;

	private StringBuilder sb = new StringBuilder(512);

	public Xml2JsonUtils(Reader reader, String... arrayNodes) {
		if (arrayNodes != null && arrayNodes.length > 0) {
			this.arrayNodes = new HashSet<String>(arrayNodes.length);
			for (int i = 0; i < arrayNodes.length; i++) {
				this.arrayNodes.add(arrayNodes[i]);
			}
		}

		SAXReader saxReader = new SAXReader();
		saxReader.setIgnoreComments(true);
		saxReader.setValidation(false);
		document = null;
		try {
			document = saxReader.read(reader);
		} catch (DocumentException e) {
			throw new java.lang.IllegalArgumentException("Parse xml error:\n" + e.getMessage(), e);
		}
	}

	public Xml2JsonUtils(InputStream xml, String... arrayNodes) {
		this(new InputStreamReader(xml), arrayNodes);
	}

	public Xml2JsonUtils(String xml, String... arrayNodes) {
		this(new StringReader(xml), arrayNodes);
	}

	public String toJSONString() {
		Element root = document.getRootElement();
		getElementJSON(root);
		return sb.toString();
	}

	private void getElementJSON(Element e) {
		// --------------
		List<?> elements = e.elements();
		List<?> attrs = e.attributes();
		if ((elements == null || elements.size() == 0) && (attrs == null || attrs.size() == 0)) {
			String temp = e.getTextTrim();
			if (temp.length() > 0) {
				boolean hasSper = false;
				if (temp.charAt(0) == '[' || temp.charAt(0) == '{') {
					hasSper = true;
				}
				if (hasSper) {
					sb.append(temp);
				} else {
					sb.append('"');
					filtString(temp);
					sb.append('"');
				}
			} else {
				sb.append("null");
			}
		} else {
			sb.append('{');
			boolean flag = false;
			//
			if (attrs != null && attrs.size() > 0) {
				for (int i = 0; i < attrs.size(); i++) {
					Attribute attr = (Attribute) attrs.get(i);
					if (flag) {
						sb.append(',');
					} else {
						flag = true;
					}
					sb.append('"').append(attr.getName()).append('"');
					sb.append(":\"");
					filtString(attr.getValue());
					sb.append('"');
				}
			}
			if (elements != null && elements.size() > 0) {
				HashSet<String> names = new HashSet<String>(elements.size());
				for (int i = 0; i < elements.size(); i++) {
					names.add(((Element) elements.get(i)).getName());
				}
				Iterator<String> iterator = names.iterator();
				while (iterator.hasNext()) {
					String name = (String) iterator.next();
					List<?> children = e.elements(name);
					boolean isArray = children.size() > 1 || (arrayNodes != null && arrayNodes.contains(name));
					if (isArray) {
						if (flag) {
							sb.append(',');
						} else {
							flag = true;
						}
						sb.append('"').append(name).append('"');
						sb.append(":[");
						// --------------------------------
						for (int i = 0; i < children.size(); i++) {
							Element child = (Element) children.get(i);
							if (i != 0) {
								sb.append(',');
							}
							getElementJSON(child);
						}
						// ----------------------------------
						sb.append("]");
					} else {
						Element child = (Element) children.get(0);
						if (flag) {
							sb.append(',');
						} else {
							flag = true;
						}
						sb.append('"').append(name).append("\":");
						getElementJSON(child);
					}
				}
			}
			String text = e.getTextTrim();
			if (text != null && text.length() > 0) {
				if (flag) {
					sb.append(',');
				} else {
					flag = true;
				}
				sb.append("\"value\":");
				boolean hasSper = false;
				if (text.charAt(0) == '[' || text.charAt(0) == '{') {
					hasSper = true;
				}
				if (hasSper) {
					sb.append(text);
				} else {
					sb.append('"');
					filtString(text);
					sb.append('"');
				}
			}
			sb.append('}');
		}
	}

	public void filtString(String s) {
		if (s == null || s.length() == 0)
			return;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '"') {
				sb.append("\\\"");
			} else if (c == '\\') {
				sb.append("\\\\");
			} else {
				sb.append(c);
			}
		}
	}

}

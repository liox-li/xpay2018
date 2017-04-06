package com.xpay.pay.util;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class XmlUtils {
	public static Map<String, String> fromXml(byte[] xmlBytes, String charset)
			throws Exception {
		SAXReader reader = new SAXReader(false);
		InputSource source = new InputSource(new ByteArrayInputStream(xmlBytes));
		source.setEncoding(charset);
		Document doc = reader.read(source);
		return element2Map(doc.getRootElement());
	}
	
	public static String toXml(List<KeyValuePair> keyValuePairs) {
		if (keyValuePairs == null || CollectionUtils.isEmpty(keyValuePairs)) {
			return StringUtils.EMPTY;
		}
		StringBuilder buf = new StringBuilder();
		buf.append("<xml>");
		for (KeyValuePair pair : keyValuePairs) {
			buf.append("<").append(pair.getKey()).append(">");
			buf.append(pair.getValue());
			buf.append("</").append(pair.getKey()).append(">\n");
		}
		buf.append("</xml>");
		return buf.toString();
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, String> element2Map(Element root) {
		Map<String, String> map = new HashMap<String, String>();
		List<Element> elements = root.elements();
		if (elements != null && CollectionUtils.isNotEmpty(elements)) {
			for (Element element : elements) {
				map.put(element.getName().toLowerCase(), element.getTextTrim());
			}
		}
		return map;
	}
}

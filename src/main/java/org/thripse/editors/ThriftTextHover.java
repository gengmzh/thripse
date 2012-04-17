/**
 * 
 */
package org.thripse.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.DefaultTextHover;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.ISourceViewer;

/**
 * @since 2012-4-16
 * @author gmz
 * 
 */
public class ThriftTextHover extends DefaultTextHover {

	public ThriftTextHover(ISourceViewer sourceViewer) {
		super(sourceViewer);
	}

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		// String info = super.getHoverInfo(textViewer, hoverRegion);
		// if (info != null && !info.isEmpty()) {
		// return info;
		// }
		// my hover
		String title = null;
		try {
			title = this.getTitle(textViewer, hoverRegion);
		} catch (Exception e1) {
			System.out.println("get title failed");
			e1.printStackTrace();
		}
		if (title == null || title.isEmpty()) {
			return null;
		}
		String comment = "";
		try {
			comment = this.getComment(textViewer, hoverRegion);
		} catch (Exception e) {
			System.out.println("get comment failed");
			e.printStackTrace();
		}
		return title + "\n" + comment;
	}

	protected String getTitle(ITextViewer textViewer, IRegion hoverRegion) throws Exception {
		IDocument doc = textViewer.getDocument();
		int ln = doc.getLineOfOffset(hoverRegion.getOffset());
		IRegion reg = doc.getLineInformation(ln);
		String title = doc.get(reg.getOffset(), reg.getLength());
		return title != null ? this.trim(title) : null;
	}

	private String getComment(ITextViewer textViewer, IRegion hoverRegion) throws Exception {
		IDocument doc = textViewer.getDocument();
		int ln = doc.getLineOfOffset(hoverRegion.getOffset());
		boolean ce = false;
		List<String> lines = new ArrayList<String>();
		while (ln-- >= 0) {
			IRegion reg = doc.getLineInformation(ln);
			String line = doc.get(reg.getOffset(), reg.getLength());
			if (line == null || line.isEmpty()) {
				continue;
			}
			line = this.trim(line);
			if (ce == false) {
				if (line.endsWith("*/")) {
					ce = true;
					lines.add(this.normalize(line));
				} else {
					break;
				}
			} else {
				lines.add(this.normalize(line));
				if (line.startsWith("/*")) {
					break;
				}
			}
		}
		// result
		StringBuffer buf = new StringBuffer();
		if (!lines.isEmpty()) {
			int i = lines.size() - 1;
			buf.append(lines.get(i));
			while (--i >= 0) {
				buf.append("\n" + lines.get(i));
			}
		}
		return buf.toString();
	}

	public String trim(String line) {
		int st = 0, ed = line.length();
		while (st < ed) {
			char ch = line.charAt(st);
			if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
				st++;
			} else {
				break;
			}
		}
		while (st < ed) {
			char ch = line.charAt(ed - 1);
			if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
				ed--;
			} else {
				break;
			}
		}
		return line.substring(st, ed);
	}

	public String normalize(String line) {
		String res = line.replace("*", "").replace("/", "").replace("\t", "").replace("\n", "").replace("\r", "");
		return trim(res);
	}

}

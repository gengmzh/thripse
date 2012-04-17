/**
 * 
 */
package org.thripse.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;

/**
 * @since 2012-4-13
 * @author gmz
 * 
 */
public class ThriftAnnotationHover extends DefaultAnnotationHover {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.source.IAnnotationHover#getHoverInfo(org.eclipse
	 * .jface.text.source.ISourceViewer, int)
	 */
	@Override
	public String getHoverInfo(ISourceViewer arg0, int arg1) {
		String info = super.getHoverInfo(arg0, arg1);
		if (info != null && !info.isEmpty()) {
			return info;
		}
		// my hover
		IDocument doc = arg0.getDocument();
		try {
			IRegion region = doc.getLineInformation(arg1);
			info = doc.get(region.getOffset(), region.getLength());
		} catch (BadLocationException e) {
		}
		return arg1 + ": " + ((info != null && !info.isEmpty()) ? info : "nothing");
	}

}

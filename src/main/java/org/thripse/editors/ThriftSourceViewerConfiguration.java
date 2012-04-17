/**
 * 
 */
package org.thripse.editors;

import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/**
 * @since 2012-4-13
 * @author gmz
 * 
 */
public class ThriftSourceViewerConfiguration extends SourceViewerConfiguration {

	public ThriftSourceViewerConfiguration() {
	}

	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		// return super.getAnnotationHover(sourceViewer);
		return new ThriftAnnotationHover();
	}

	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
		// return super.getTextHover(sourceViewer, contentType);
		return new ThriftTextHover(sourceViewer);
	}

	/**
	 * syntax coloring
	 */
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		// TODO Auto-generated method stub
		return super.getPresentationReconciler(sourceViewer);
	}

}

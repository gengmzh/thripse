/**
 * 
 */
package org.thripse.editors;

import org.eclipse.ui.editors.text.TextEditor;

/**
 * @since 2012-4-13
 * @author gmz
 * 
 */
public class ThriftSourceEditor extends TextEditor {

	public ThriftSourceEditor() {
		super();
	}

	@Override
	protected void initializeEditor() {
		super.initializeEditor();
		this.setSourceViewerConfiguration(new ThriftSourceViewerConfiguration());
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}

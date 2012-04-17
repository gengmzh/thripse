/**
 * 
 */
package org.thripse.editors;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @since 2012-4-17
 * @author gmz
 * 
 */
public class ThriftBuildComposite extends Composite {

	private FileEditor thriftCompiler;
	private Combo generator;
	private DirectoryEditor outpath;
	private String thriftFile;

	public ThriftBuildComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		this.setLayout(layout);

		this.thriftCompiler = new FileEditor(this);

		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(GridData.BEGINNING));
		label.setText("Thrift generator: ");
		this.generator = new Combo(this, SWT.READ_ONLY);
		GridData grid = new GridData(GridData.BEGINNING);
		grid.horizontalSpan = 2;
		generator.setLayoutData(grid);

		this.outpath = new DirectoryEditor(this);
		// build
		Button build = new Button(this, SWT.NONE);
		build.setLayoutData(new GridData(GridData.BEGINNING));
		build.setText("Build...");
		build.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ThriftCompiler compiler = new ThriftCompiler(thriftCompiler.getValue());
				if (generator.getSelectionIndex() < 0) {
					System.out.println("please select generator");
					return;
				}
				String str = generator.getItem(generator.getSelectionIndex());
				int res = compiler.setSTR(str.split(" ")[0]).setOut(outpath.getValue()).compile(thriftFile);
				System.out.println("building: " + (res != 0 ? "failed" : "done successfully"));
			}
		});
	}

	public FileEditor getCompiler() {
		return thriftCompiler;
	}

	public DirectoryEditor getOutpath() {
		return outpath;
	}

	public String getThriftFile() {
		return thriftFile;
	}

	public ThriftBuildComposite setThriftFile(String thriftFile) {
		this.thriftFile = thriftFile;
		return this;
	}

	class FileEditor {

		private Label label;
		private Text text;
		private Button button;
		private FileDialog dialog;

		public FileEditor(Composite composite) {
			// label
			label = new Label(composite, SWT.NONE);
			GridData grid = new GridData(GridData.BEGINNING);
			label.setLayoutData(grid);
			label.setText("Thrift compiler : ");
			// text
			text = new Text(composite, SWT.BORDER);
			grid = new GridData(GridData.BEGINNING);
			grid.widthHint = 300;
			text.setLayoutData(grid);
			text.setEditable(false);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent arg0) {
					ThriftCompiler compiler = new ThriftCompiler(text.getText());
					List<String> gl = compiler.parseGenerator();
					generator.setItems(gl.toArray(new String[gl.size()]));
					generator.select(0);
				}
			});
			// button
			button = new Button(composite, SWT.NONE);
			grid = new GridData(GridData.BEGINNING);
			button.setLayoutData(grid);
			button.setText("Browse...");
			// dialog
			dialog = new FileDialog(composite.getShell());
			dialog.setText("select thrift compiler");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String path = dialog.open();
					if (path == null || path.isEmpty()) {
						return;
					}
					text.setText(path);
				}
			});
		}

		public FileEditor setLabel(String text) {
			this.label.setText(text);
			return this;
		}

		public FileEditor setDefaultPath(String defaultPath) {
			this.text.setText(defaultPath);
			this.dialog.setFilterPath(defaultPath);
			return this;
		}

		public FileEditor setTextHorizontalSpan(int span) {
			GridData grid = (GridData) this.text.getLayoutData();
			grid.horizontalSpan = span;
			return this;
		}

		public FileEditor setTextWidth(int width) {
			GridData grid = (GridData) this.text.getLayoutData();
			grid.widthHint = width;
			return this;
		}

		public FileEditor setButtonText(String text) {
			this.button.setText(text);
			return this;
		}

		public FileEditor setDialogText(String text) {
			this.dialog.setText(text);
			return this;
		}

		public String getValue() {
			return this.text.getText();
		}

	}

	class DirectoryEditor {

		private Label label;
		private Text text;
		private Button button;
		private DirectoryDialog dialog;

		public DirectoryEditor(Composite composite) {
			// label
			label = new Label(composite, SWT.NONE);
			GridData grid = new GridData(GridData.BEGINNING);
			label.setLayoutData(grid);
			label.setText("Output location: ");
			// text
			text = new Text(composite, SWT.BORDER);
			grid = new GridData(GridData.BEGINNING);
			grid.widthHint = 300;
			text.setLayoutData(grid);
			text.setEditable(false);
			// button
			button = new Button(composite, SWT.NONE);
			grid = new GridData(GridData.BEGINNING);
			button.setLayoutData(grid);
			button.setText("Browse...");
			// dialog
			dialog = new DirectoryDialog(composite.getShell());
			dialog.setText("select output location");
			dialog.setMessage("Please select the output location");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String path = dialog.open();
					if (path != null && !path.isEmpty()) {
						text.setText(path);
					}
				}
			});
		}

		public DirectoryEditor setLabel(String text) {
			this.label.setText(text);
			return this;
		}

		public DirectoryEditor setDefaultPath(String defaultPath) {
			this.text.setText(defaultPath);
			this.dialog.setFilterPath(defaultPath);
			return this;
		}

		public DirectoryEditor setHorizontalSpan(int span) {
			GridData grid = (GridData) this.text.getLayoutData();
			grid.horizontalSpan = span;
			return this;
		}

		public DirectoryEditor setTextWidth(int width) {
			GridData grid = (GridData) this.text.getLayoutData();
			grid.widthHint = width;
			return this;
		}

		public DirectoryEditor setButtonText(String text) {
			this.button.setText(text);
			return this;
		}

		public DirectoryEditor setDialogText(String text) {
			this.dialog.setText(text);
			return this;
		}

		public DirectoryEditor setDialogMessage(String msg) {
			this.dialog.setMessage(msg);
			return this;
		}

		public String getValue() {
			return this.text.getText();
		}
	}

}

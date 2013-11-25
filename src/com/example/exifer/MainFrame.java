package com.example.exifer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.drew.metadata.MetadataException;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String NEWLINE = System.getProperty("line.separator");
	private JPanel contentPane;
	private JTextField srcField;
	private JTextField destField;
	private JTextArea textArea;
	Object mutex = new Object();
	boolean stop = false;
	
	public void setStop(boolean stop) {
		synchronized (mutex) {
			this.stop = stop;
		}
	}
	
	public boolean isStop() {
		return stop;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	void chooseDirectory(JTextField field) {
		String prev = field.getText();
		JFileChooser chooser = null;
		if (prev != null && prev.length() > 0) {
			chooser = new JFileChooser(new File(prev));
		} else {
			chooser = new JFileChooser();
		}
		// DOTO set listener
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
			File dir = chooser.getSelectedFile();
			field.setText(dir.getAbsolutePath());
		}
	}
	
	void doDrop(DropTargetDropEvent dtde, JTextField field) {
		Transferable t = dtde.getTransferable();
		if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			dtde.acceptDrop(DnDConstants.ACTION_REFERENCE);
			try {
				List list = (List)t.getTransferData(DataFlavor.javaFileListFlavor);
				for (int i = 0; i < list.size(); i++) {
					File f = (File) list.get(i);
					if (f.isDirectory()) {
						field.setText(f.getAbsolutePath());
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	DropTarget srcTarget = new DropTarget() {
		@Override
		public synchronized void drop(DropTargetDropEvent dtde) {
			doDrop(dtde, srcField);
		}};
		
	DropTarget destTarget = new DropTarget() {
		@Override
		public synchronized void drop(DropTargetDropEvent dtde) {
			doDrop(dtde, destField);
		}};

	ExifListener listener = new ExifListener(){

		@Override
		public void update(String state) {
			String prev = textArea.getText();
			textArea.setText(state+NEWLINE+prev);
			textArea.invalidate();
		}};
		
	void retrieve(File dir, File destRoot, Exifer exifer) throws MetadataException {
		File[] files = dir.listFiles();

		if (files == null || files.length <= 0) return;
		for (File f: files) {
			if (isStop()) break;
			if (f.isFile())      exifer.copyExif(f, destRoot);
			if (f.isDirectory()) retrieve(f, destRoot, exifer);
		}
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton copyButton = new JButton("Copy");
		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(new Runnable(){
					@Override
					public void run() {
						File src = new File(srcField.getText());
						File dest = new File(destField.getText());
						Exifer exifer = new Exifer();
						exifer.addExifListener(listener);
						try {
							retrieve(src, dest, exifer);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}});
				thread.start();
			}
		});
		toolBar.add(copyButton);
		
		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setStop(true);
			}
		});
		toolBar.add(stopButton);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		srcField = new JTextField();
		srcField.setColumns(10);
		srcField.setDropTarget(srcTarget);
		
		destField = new JTextField();
		destField.setColumns(10);
		destField.setDropTarget(destTarget);
		
		JButton srcButton = new JButton("File...");
		srcButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseDirectory(srcField);
			}
		});
		
		JButton destButton = new JButton("File...");
		destButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseDirectory(destField);
			}
		});
		
		textArea = new JTextArea();
		
		JLabel lblFrom = new JLabel("From:");
		
		JLabel lblTo = new JLabel("To:");
		
		JLabel lblLog = new JLabel("Log:");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblFrom)
						.addComponent(lblTo)
						.addComponent(lblLog))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(textArea, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(destField, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
								.addComponent(srcField, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE))
							.addGap(12)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(destButton, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
								.addComponent(srcButton))))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(srcField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(srcButton)
						.addComponent(lblFrom))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(destField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(destButton)
						.addComponent(lblTo))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
						.addComponent(lblLog))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
	}
}
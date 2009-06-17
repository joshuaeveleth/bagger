/*
 * Copyright 2002-2004 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagit.Manifest.Algorithm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.application.PageComponent;

public class SaveBagFrame extends JFrame implements ActionListener {
	private static final Log log = LogFactory.getLog(SaveBagFrame.class);
	private static final long serialVersionUID = 1L;
	BagView bagView;
	DefaultBag bag = null;
	File bagFile;
	String bagFileName = "";
	private Dimension preferredDimension = new Dimension(600, 400);
	JPanel savePanel;
	JPanel serializeGroupPanel;
	JTextField bagNameField;
	JLabel urlLabel;
	JTextField urlField;
	JButton saveAsButton;
	JButton okButton;
	JButton cancelButton;
	JRadioButton noneButton;
	JRadioButton zipButton;
	JRadioButton tarButton;
	JRadioButton tarGzButton;
	JRadioButton tarBz2Button;

	JCheckBox holeyCheckbox;
	JCheckBox isTagCheckbox;
	JCheckBox isVerifyCheckbox;
	JCheckBox isPayloadCheckbox;
    JComboBox tagAlgorithmList;
    JComboBox payAlgorithmList;

	public SaveBagFrame(BagView bagView, String title) {
        super(title);
		this.bagView = bagView;
		if (bagView != null) {
			bag = bagView.getBag();
	        getContentPane().removeAll();
	        savePanel = createComponents();
		} else {
			savePanel = new JPanel();
		}
        getContentPane().add(savePanel, BorderLayout.CENTER);
        setPreferredSize(preferredDimension);
        pack();
    }

    private JPanel createComponents() {
        Border border = new EmptyBorder(5, 5, 5, 5);

        // TODO: Add bag name field
    	// TODO: Add save name file selection button
    	saveAsButton = new JButton(getMessage("bag.button.browse"));
    	saveAsButton.addActionListener(new SaveBagAsHandler());
        saveAsButton.setEnabled(true);
        saveAsButton.setToolTipText(getMessage("bag.button.browse.help"));
    	String fileName = "";
    	if (bag != null) fileName = bag.getName();
    	bagNameField = new JTextField(fileName);
        bagNameField.setCaretPosition(fileName.length());
        bagNameField.setEditable(false);
        bagNameField.setEnabled(false);

        // Holey bag control
        JLabel holeyLabel = new JLabel(bagView.getPropertyMessage("bag.label.isholey"));
        holeyLabel.setToolTipText(bagView.getPropertyMessage("bag.isholey.help"));
        holeyCheckbox = new JCheckBox(bagView.getPropertyMessage("bag.checkbox.isholey"));
        holeyCheckbox.setBorder(border);
        holeyCheckbox.setSelected(false);
        holeyCheckbox.addActionListener(new HoleyBagHandler());
        holeyCheckbox.setToolTipText(bagView.getPropertyMessage("bag.isholey.help"));

        urlLabel = new JLabel(bagView.getPropertyMessage("baseURL.label"));
        urlLabel.setToolTipText(bagView.getPropertyMessage("baseURL.description"));
        urlLabel.setEnabled(false);
    	urlField = new JTextField("");
        urlField.setEnabled(false);
        
        // TODO: Add format label
    	JLabel serializeLabel;
        serializeLabel = new JLabel(getMessage("bag.label.ispackage"));
        serializeLabel.setToolTipText(getMessage("bag.serializetype.help"));

    	// TODO: Add format selection panel
        noneButton = new JRadioButton(getMessage("bag.serializetype.none"));
        noneButton.setEnabled(true);
        AbstractAction serializeListener = new SerializeBagHandler();
        noneButton.addActionListener(serializeListener);
        noneButton.setToolTipText(getMessage("bag.serializetype.none.help"));

        zipButton = new JRadioButton(getMessage("bag.serializetype.zip"));
        zipButton.setEnabled(true);
        zipButton.addActionListener(serializeListener);
        zipButton.setToolTipText(getMessage("bag.serializetype.zip.help"));

        tarButton = new JRadioButton(getMessage("bag.serializetype.tar"));
        tarButton.setEnabled(true);
        tarButton.addActionListener(serializeListener);
        tarButton.setToolTipText(getMessage("bag.serializetype.tar.help"));
        
        tarGzButton = new JRadioButton(getMessage("bag.serializetype.targz"));
        tarGzButton.setEnabled(true);
        tarGzButton.addActionListener(serializeListener);
        tarGzButton.setToolTipText(getMessage("bag.serializetype.targz.help"));
        
        tarBz2Button = new JRadioButton(getMessage("bag.serializetype.tarbz2"));
        tarBz2Button.setEnabled(true);
        tarBz2Button.addActionListener(serializeListener);
        tarBz2Button.setToolTipText(getMessage("bag.serializetype.tarbz2.help"));

        short mode = bag.getSerialMode();
    	if (mode == DefaultBag.NO_MODE) {
    		this.noneButton.setEnabled(true);
    	} else if (mode == DefaultBag.ZIP_MODE) {
    		this.zipButton.setEnabled(true);
    	} else if (mode == DefaultBag.TAR_MODE) {
    		this.tarButton.setEnabled(true);
    	} else if (mode == DefaultBag.TAR_GZ_MODE) {
    		this.tarGzButton.setEnabled(true);
    	} else if (mode == DefaultBag.TAR_BZ2_MODE) {
    		this.tarBz2Button.setEnabled(true);
    	} else {
    		this.noneButton.setEnabled(true);
    	}
        
        ButtonGroup serializeGroup = new ButtonGroup();
        serializeGroup.add(noneButton);
        serializeGroup.add(zipButton);
        serializeGroup.add(tarButton);
        serializeGroup.add(tarGzButton);
        serializeGroup.add(tarBz2Button);
        serializeGroupPanel = new JPanel(new FlowLayout());
        serializeGroupPanel.add(serializeLabel);
        serializeGroupPanel.add(noneButton);
        serializeGroupPanel.add(zipButton);
        serializeGroupPanel.add(tarButton);
        serializeGroupPanel.add(tarGzButton);
        serializeGroupPanel.add(tarBz2Button);
        serializeGroupPanel.setBorder(border);
        serializeGroupPanel.setEnabled(true);
        serializeGroupPanel.setToolTipText(bagView.getPropertyMessage("bag.serializetype.help"));

    	// TODO: Add tag manifest generate checkbox
        JLabel tagLabel = new JLabel(getMessage("bag.label.istag"));
        tagLabel.setToolTipText(getMessage("bag.label.istag.help"));
        isTagCheckbox = new JCheckBox();
        isTagCheckbox.setBorder(border);
        isTagCheckbox.setSelected(bag.isBuildTagManifest());
        isTagCheckbox.addActionListener(new TagManifestHandler());
        isTagCheckbox.setToolTipText(getMessage("bag.checkbox.istag.help"));

        // TODO: Add tag manifest algorithm selection
        JLabel tagAlgorithmLabel = new JLabel(getMessage("bag.label.tagalgorithm"));
        tagAlgorithmLabel.setToolTipText(getMessage("bag.label.tagalgorithm.help"));
        ArrayList<String> listModel = new ArrayList<String>();
		for(Algorithm algorithm : Algorithm.values()) {
			listModel.add(algorithm.bagItAlgorithm);
		}
        tagAlgorithmList = new JComboBox(listModel.toArray());
        tagAlgorithmList.setName(getMessage("bag.tagalgorithmlist"));
        tagAlgorithmList.setSelectedItem(bag.getTagManifestAlgorithm());
        tagAlgorithmList.addActionListener(new TagAlgorithmListHandler());
        tagAlgorithmList.setToolTipText(getMessage("bag.tagalgorithmlist.help"));
    	
    	// TODO: Add payload manifest generate checkbox
        JLabel payloadLabel = new JLabel(getMessage("bag.label.ispayload"));
        payloadLabel.setToolTipText(getMessage("bag.ispayload.help"));
        isPayloadCheckbox = new JCheckBox();
        isPayloadCheckbox.setBorder(border);
        isPayloadCheckbox.setSelected(bag.isBuildPayloadManifest());
        isPayloadCheckbox.addActionListener(new PayloadManifestHandler());
        isPayloadCheckbox.setToolTipText(getMessage("bag.ispayload.help"));

        // TODO: Add payload manifest algorithm selection
        JLabel payAlgorithmLabel = new JLabel(bagView.getPropertyMessage("bag.label.payalgorithm"));
        payAlgorithmLabel.setToolTipText(getMessage("bag.payalgorithm.help"));
        payAlgorithmList = new JComboBox(listModel.toArray());
        payAlgorithmList.setName(getMessage("bag.payalgorithmlist"));
        payAlgorithmList.setSelectedItem(bag.getPayloadManifestAlgorithm());
        payAlgorithmList.addActionListener(new PayAlgorithmListHandler());
        payAlgorithmList.setToolTipText(getMessage("bag.payalgorithmlist.help"));

    	// TODO: Add verify after write label
        JLabel verifyLabel = new JLabel(getMessage("bag.isverify"));
        verifyLabel.setToolTipText(getMessage("bag.isverify.help"));
    	// TODO: Add verify checkbox
        isVerifyCheckbox = new JCheckBox();
        isVerifyCheckbox.setBorder(border);
        isVerifyCheckbox.setSelected(bag.isValidateOnSave());
        isVerifyCheckbox.addActionListener(new VerifyHandler());
        isVerifyCheckbox.setToolTipText(getMessage("bag.isverify.help"));

    	okButton = new JButton("Save");
    	okButton.addActionListener(new OkSaveBagHandler());
        okButton.setEnabled(true);

    	cancelButton = new JButton("Cancel");
    	cancelButton.addActionListener(new CancelSaveBagHandler());
    	cancelButton.setEnabled(true);
        
    	GridBagLayout layout = new GridBagLayout();
        GridBagConstraints glbc = new GridBagConstraints();
        JPanel panel = new JPanel(layout);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        int row = 0;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(saveAsButton, glbc);
        panel.add(saveAsButton);
        buildConstraints(glbc, 1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST); 
        layout.setConstraints(bagNameField, glbc);
        panel.add(bagNameField);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(holeyLabel, glbc);
        panel.add(holeyLabel);
        buildConstraints(glbc, 1, row, 1, 1, 80, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(holeyCheckbox, glbc);
        panel.add(holeyCheckbox);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(urlLabel, glbc);
        panel.add(urlLabel);
        buildConstraints(glbc, 1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(urlField, glbc);
        panel.add(urlField);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(serializeLabel, glbc);
    	panel.add(serializeLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST); 
        layout.setConstraints(serializeGroupPanel, glbc);
    	panel.add(serializeGroupPanel);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(tagLabel, glbc);
    	panel.add(tagLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(isTagCheckbox, glbc);
    	panel.add(isTagCheckbox);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(tagAlgorithmLabel, glbc);
    	panel.add(tagAlgorithmLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(tagAlgorithmList, glbc);
    	panel.add(tagAlgorithmList);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(payloadLabel, glbc);
    	panel.add(payloadLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(isPayloadCheckbox, glbc);
    	panel.add(isPayloadCheckbox);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(payAlgorithmLabel, glbc);
    	panel.add(payAlgorithmLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(payAlgorithmList, glbc);
    	panel.add(payAlgorithmList);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(verifyLabel, glbc);
    	panel.add(verifyLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(isVerifyCheckbox, glbc);
    	panel.add(isVerifyCheckbox);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(cancelButton, glbc);
    	panel.add(cancelButton);
        buildConstraints(glbc, 1, row, 1, 1, 80, 50, GridBagConstraints.NONE, GridBagConstraints.CENTER); 
        layout.setConstraints(okButton, glbc);
    	panel.add(okButton);
        
    	return panel;
    }
    
    public void setBag(DefaultBag bag) {
    	this.bag = bag;
    	bagNameField.setText(bag.getName());
    	short mode = bag.getSerialMode();
    	if (mode == DefaultBag.NO_MODE) {
    		noneButton.setEnabled(true);
    		noneButton.setSelected(true);
    	} else if (mode == DefaultBag.ZIP_MODE) {
    		zipButton.setEnabled(true);
    		zipButton.setSelected(true);
    	} else if (mode == DefaultBag.TAR_MODE) {
    		tarButton.setEnabled(true);
    		tarButton.setSelected(true);
    	} else if (mode == DefaultBag.TAR_GZ_MODE) {
    		tarGzButton.setEnabled(true);
    		tarGzButton.setSelected(true);
    	} else if (mode == DefaultBag.TAR_BZ2_MODE) {
    		tarBz2Button.setEnabled(true);
    		tarBz2Button.setSelected(true);
    	} else {
    		noneButton.setEnabled(true);
    		noneButton.setSelected(true);
    	}
    	savePanel.invalidate();
    }

    public void actionPerformed(ActionEvent e) {
    	invalidate();
    	repaint();
    }

    public class SerializeBagHandler extends AbstractAction {
    	private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {
			JRadioButton cb = (JRadioButton)e.getSource();
            boolean isSel = cb.isSelected();
            if (isSel) {
            	if (cb == noneButton) {
                	bagView.getBag().isSerial(false);
                	bagView.getBag().setSerialMode(DefaultBag.NO_MODE);
            	} else if (cb == zipButton) {
            		bagView.getBag().isSerial(true);
            		bagView.getBag().setSerialMode(DefaultBag.ZIP_MODE);
            	} else if (cb == tarButton) {
            		bagView.getBag().isSerial(true);
            		bagView.getBag().setSerialMode(DefaultBag.TAR_MODE);
            	} else if (cb == tarGzButton) {
            		bagView.getBag().isSerial(true);
            		bagView.getBag().setSerialMode(DefaultBag.TAR_GZ_MODE);
            	} else if (cb == tarBz2Button) {
            		bagView.getBag().isSerial(true);
            		bagView.getBag().setSerialMode(DefaultBag.TAR_BZ2_MODE);
            	} else {
            		bagView.getBag().isSerial(false);
            		bagView.getBag().setSerialMode(DefaultBag.NO_MODE);
            	}
            }
        }
    }

    private class SaveBagAsHandler extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
	        File selectFile = new File(File.separator+".");
	        JFrame frame = new JFrame();
	        JFileChooser fs = new JFileChooser(selectFile);
	    	fs.setDialogType(JFileChooser.SAVE_DIALOG);
	    	fs.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    	fs.addChoosableFileFilter(bagView.noFilter);
	    	fs.addChoosableFileFilter(bagView.zipFilter);
	        fs.addChoosableFileFilter(bagView.tarFilter);
	        fs.setDialogTitle("Save Bag As");
	    	fs.setCurrentDirectory(bag.getRootDir());
	    	if (bag.getName() != null && !bag.getName().equalsIgnoreCase(bagView.getPropertyMessage("bag.label.noname"))) {
	    		String selectedName = bag.getName();
	    		if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
	    			selectedName += "."+DefaultBag.ZIP_LABEL;
	    			fs.setFileFilter(bagView.zipFilter);
	    		}
	    		else if (bag.getSerialMode() == DefaultBag.TAR_MODE ||
	    				bag.getSerialMode() == DefaultBag.TAR_GZ_MODE ||
	    				bag.getSerialMode() == DefaultBag.TAR_BZ2_MODE) {
	    			selectedName += "."+DefaultBag.TAR_LABEL;
	    			fs.setFileFilter(bagView.tarFilter);
	    		}
	    		else {
	    			fs.setFileFilter(bagView.noFilter);
	    		}
	    		fs.setSelectedFile(new File(selectedName));
	    	} else {
    			fs.setFileFilter(bagView.noFilter);
	    	}
	    	int	option = fs.showSaveDialog(frame);

	        if (option == JFileChooser.APPROVE_OPTION) {
	            File file = fs.getSelectedFile();
	            bagFile = file;
	            bagFileName = bagFile.getAbsolutePath();
	            bagNameField.setText(bagFileName);
	            bagNameField.setCaretPosition(bagFileName.length());
	            bagNameField.invalidate();
	        }
        }
    }

    private class OkSaveBagHandler extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			if (bagNameField.getText().trim().isEmpty() || bagNameField.getText().equalsIgnoreCase(bagView.getPropertyMessage("bag.label.noname"))) {
    			bagView.showWarningErrorDialog("Error - bag not saved", "The bag must have a file name.");
    			return;
			}
			if (bag.isHoley()) {
				if (urlField.getText().trim().isEmpty()) {
        			bagView.showWarningErrorDialog("Error - bag not saved", "A holey bag must have a URL value.");
        			return;
				} else {
					//if (bagView.getBag().getFetch() == null)
					bagView.getBag().getFetch().setBaseURL(urlField.getText().trim());
				}
				bagView.holeyCheckbox.setSelected(true);
			} else {
				bagView.holeyCheckbox.setSelected(false);
			}
			setVisible(false);
            bagView.getBag().setName(bagFileName);
            bagView.bagNameField.setText(bagFileName);
            bagView.bagNameField.setCaretPosition(bagFileName.length());
            bagView.bagNameField.invalidate();
			bagView.save(bagFile);
        }
    }

    private class CancelSaveBagHandler extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			setVisible(false);
        }
    }

    private class TagManifestHandler extends AbstractAction {
    	private static final long serialVersionUID = 75893358194076314L;
    	public void actionPerformed(ActionEvent e) {
    		JCheckBox cb = (JCheckBox)e.getSource();
                
    		// Determine status
    		boolean isSelected = cb.isSelected();
    		if (isSelected) {
    			bagView.getBag().isBuildTagManifest(true);
    		} else {
    			bagView.getBag().isBuildTagManifest(false);
    		}
    	}
    }
    
    private class TagAlgorithmListHandler extends AbstractAction {
    	private static final long serialVersionUID = 75893358194076314L;
    	public void actionPerformed(ActionEvent e) {
        	JComboBox jlist = (JComboBox)e.getSource();
        	String alg = (String) jlist.getSelectedItem();
        	bagView.getBag().setTagManifestAlgorithm(alg);
    	}
    }
    
    private class PayloadManifestHandler extends AbstractAction {
    	private static final long serialVersionUID = 75893358194076314L;
    	public void actionPerformed(ActionEvent e) {
    		JCheckBox cb = (JCheckBox)e.getSource();
                
    		// Determine status
    		boolean isSelected = cb.isSelected();
    		if (isSelected) {
    			bagView.getBag().isBuildPayloadManifest(true);
    		} else {
    			bagView.getBag().isBuildPayloadManifest(false);
    		}
    	}
    }

    private class PayAlgorithmListHandler extends AbstractAction {
    	private static final long serialVersionUID = 75893358194076314L;
    	public void actionPerformed(ActionEvent e) {
        	JComboBox jlist = (JComboBox)e.getSource();
        	String alg = (String) jlist.getSelectedItem();
        	bagView.getBag().setPayloadManifestAlgorithm(alg);
    	}
    }

    private class VerifyHandler extends AbstractAction {
    	private static final long serialVersionUID = 75893358194076314L;
    	public void actionPerformed(ActionEvent e) {
    		JCheckBox cb = (JCheckBox)e.getSource();
                
    		// Determine status
    		boolean isSelected = cb.isSelected();
    		if (isSelected) {
    			bagView.getBag().isValidateOnSave(true);
    		} else {
    			bagView.getBag().isValidateOnSave(false);
    		}
    	}
    }
    
    private class HoleyBagHandler extends AbstractAction {
       	private static final long serialVersionUID = 1L;

    	public void actionPerformed(ActionEvent e) {
    		JCheckBox cb = (JCheckBox)e.getSource();

    		// Determine status
    		boolean isSelected = cb.isSelected();
    		if (isSelected) {
    			bag.isHoley(true);
    			urlLabel.setEnabled(true);
    			urlField.setEnabled(true);
    			urlField.requestFocus();
    		} else {
    			bag.isHoley(false);
    			urlLabel.setEnabled(false);
    			urlField.setEnabled(false);
    		}
       	}
    }
    
    private void buildConstraints(GridBagConstraints gbc,int x, int y, int w, int h, int wx, int wy, int fill, int anchor) {
    	gbc.gridx = x; // start cell in a row
    	gbc.gridy = y; // start cell in a column
    	gbc.gridwidth = w; // how many column does the control occupy in the row
    	gbc.gridheight = h; // how many column does the control occupy in the column
    	gbc.weightx = wx; // relative horizontal size
    	gbc.weighty = wy; // relative vertical size
    	gbc.fill = fill; // the way how the control fills cells
    	gbc.anchor = anchor; // alignment
    }
    
    private String getMessage(String property) {
    	return bagView.getPropertyMessage(property);
    }
}
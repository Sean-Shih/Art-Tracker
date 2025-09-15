import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;


/**
 * The Main Menu screen for the Art Tracker Application. Users can upload artworks or 
 * choose to view old artworks from here.
 * 
 * @author seanshih
 * 
 */
public class MainMenu extends JFrame implements ActionListener {

	
	/** Set width for the Upload Panel */
	private static final int UPPANELWIDTH = 700;
	/** Set height for the Upload Panel */
	private static final int UPPANELHEIGHT = 800;
	
	/** Set width for the Title Panel that contains the title */
	private static final int TPANELWIDTH = 500;
	/** Set height for the Title Panel that contains the title */
	private static final int TPANELHEIGHT = 100;
	
	/** Set width for the Buttons Panel */
	private static final int BPANELWIDTH = 500;
	/** Set height for the Buttons Panel */
	private static final int BPANELHEIGHT = 700;
	
	/** Create Queries object to allow for calls to the database */
	Queries query = new Queries();
	/** Create JFrame object to set up the frame */
	JFrame frame = new JFrame();
	
	/** Button to view old artworks */
	JButton viewButton;
	/** Button to select artwork from the local machine */
	JButton selArtButton;
	/** Button to confirm upload of current artwork" */
	JButton uploadButton;
	/** Button to bring up a small tutorial for the user */
	JButton infoButton;
	
	/** Creates a GetLocalDate object to display the current date in the frame */
	GetLocalDate date;
	
	/** Text area for the user to enter a caption for the artwork */
	JTextArea captionArea;
	/** Text area for the user to enter a title for the artwork */
	JTextArea artTitleArea;
	
	/** ImageIcon used to show a preview of the artwork the user wants to upload */
	ImageIcon previewImage = null;
	
	/** File object to represent the artwork file the user uploads */
	File physFile;
	
	/**
	 * Constructor, creates the frame and any elements in the frame.
	 */
	MainMenu() {
		
		/** Frame */
		frame.setTitle("Daily Art Tracker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize (1200, 800); 
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		/** Upload Panel and all the Java Swing elements associated with it */
		JPanel uploadPanel = new JPanel();
		uploadPanel.setLayout(null);
		uploadPanel.setBackground(Color.darkGray);
		uploadPanel.setBounds(500, 0, UPPANELWIDTH, UPPANELHEIGHT);
		
		captionArea = new JTextArea("Enter a caption here");
		captionArea.setOpaque(true);
		captionArea.setBounds(300, 550, 300, 40);
		captionArea.setLineWrap(true);
		captionArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		captionArea.setCaretColor(Color.black);
		
		selArtButton = new JButton("Select Artwork");
		selArtButton.setFont(new Font(null, Font.PLAIN, 20));
		selArtButton.setBounds(125, 650, 200, 60);
		selArtButton.setFocusable(false);
		selArtButton.addActionListener(this);
		
		uploadButton = new JButton("Upload");
		uploadButton.setFont(new Font(null, Font.PLAIN, 20));
		uploadButton.setBounds(375, 650, 200, 60);
		uploadButton.setFocusable(false);
		uploadButton.addActionListener(this);
				
		date = new GetLocalDate();
		JLabel dateLabel = new JLabel(date.FormatDate());
		dateLabel.setFont(new Font("Serif", Font.PLAIN, 30));
		dateLabel.setForeground(new Color(255, 255, 255));
		dateLabel.setBounds(30, 10, 300, 60);
		
		artTitleArea = new JTextArea("Enter Title");
		artTitleArea.setBounds(70, 557, 200, 25);	
		artTitleArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		artTitleArea.setCaretColor(Color.black);
		artTitleArea.setOpaque(true);
		
		JLabel preview = new JLabel("Drag your artwork here", SwingConstants.CENTER);
		preview.setFont(new Font("Serif", Font.ITALIC, 30));
		preview.setBounds(125, 80, 450, 450);
		preview.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		preview.setOpaque(true);
		preview.setForeground(Color.lightGray);
		preview.setBackground(Color.white);
		
		preview.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            @SuppressWarnings("unchecked")
            public boolean importData(TransferSupport support) {
                try {
                    List<File> files = (List<File>) support.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);

                    for (File file : files) { 
                        if (file.getName().toLowerCase().matches(".*\\.(png|jpg|jpeg|gif)$")) {
                        	physFile = file;
                        	previewImage = new ImageIcon(file.getAbsolutePath());
                        	preview.setIcon(previewImage);
                        	preview.setText(null);
                        	
                        } else {
                            JOptionPane.showMessageDialog(null, "Please upload an image file.");
                        }
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
		
		/** Title Panel and all associated elements */

		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBounds(0, 0, TPANELWIDTH, TPANELHEIGHT);
		
		JLabel title = new JLabel();
		title.setText("Daily Art Tracker");
		title.setFont(new Font("Times New Roman", Font.PLAIN, 50));
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setBorder(BorderFactory.createMatteBorder(5, 0, 5, 0, Color.darkGray));
		
		/** Buttons Panel and all associated elements */
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setBounds(0, 100, BPANELWIDTH, BPANELHEIGHT);
		
		viewButton = new JButton("View artworks");
		viewButton.setFont(new Font(null, Font.PLAIN, 20));
		viewButton.setBounds(150, 30, 200, 60); 
		viewButton.setFocusable(false);
		viewButton.addActionListener(this);
		
		infoButton = new JButton();
		infoButton.setIcon(new ImageIcon("Info-Button.png"));
		infoButton.setBorderPainted(false);
		infoButton.setBounds(650, 0, 50, 50);
		infoButton.setFocusable(false);
		infoButton.addActionListener(this);
		
		/** Adding all necessary components to their respective panels and the 
		 * adding those panels onto the frame */
		titlePanel.add(title, BorderLayout.CENTER);
		uploadPanel.add(selArtButton);
		uploadPanel.add(uploadButton);
		uploadPanel.add(artTitleArea);
		uploadPanel.add(captionArea);
		uploadPanel.add(infoButton);
		uploadPanel.add(preview);
		uploadPanel.add(dateLabel);
		buttonPanel.add(viewButton);
		frame.add(uploadPanel);
		frame.add(titlePanel);
		frame.add(buttonPanel);
		frame.setVisible(true);
	}
		
	/** 
	 * Receives inputs from all buttons and executes a code block depending on the source
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == selArtButton) {
			
			JFileChooser fileChooser = new JFileChooser();
			int response = fileChooser.showOpenDialog(null);
			
			if (response == JFileChooser.APPROVE_OPTION) {
				physFile = fileChooser.getSelectedFile();
			}	
		}
		
		if (e.getSource() == viewButton) {
			frame.dispose();
			ArtSelectionMenu artMenu = new ArtSelectionMenu();
		}
		
		if (e.getSource() == uploadButton) {
			if (previewImage == null) {
				JOptionPane.showMessageDialog(null, "No artwork uploaded!", "Error", JOptionPane.ERROR_MESSAGE);	

			} else {
				query.addArtQuery(artTitleArea.getText(), captionArea.getText(), physFile.getAbsolutePath(), LocalDate.now().toString());
				
				JOptionPane.showMessageDialog(null, "Artwork uploaded!", "Upload confirmation", JOptionPane.INFORMATION_MESSAGE);	
			}
				
		}
		
		if (e.getSource() == infoButton) {
			JOptionPane.showMessageDialog(null, "You can upload an image by dragging it or selecting the \"Select Artwork\" button.\n"
					+ "Press the \"Upload\" button to upload your artwork for today.\n"
					+ "Remember to add a title and caption for your artwork!.", "Tutorial", JOptionPane.QUESTION_MESSAGE);	
		}
		
	}
	
}
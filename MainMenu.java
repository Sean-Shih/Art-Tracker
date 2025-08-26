import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

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
 * 
 * GUI class for the main menu of the application
 * 
 * @author seanshih
 * 
 */
public class MainMenu extends JFrame implements ActionListener {

	/** Private variables for the height and width of the various panels in the frame */
	private static final int UPPANELWIDTH = 700;
	private static final int UPPANELHEIGHT = 800;
	
	private static final int TPANELWIDTH = 500;
	private static final int TPANELHEIGHT = 100;
	
	private static final int BPANELWIDTH = 500;
	private static final int BPANELHEIGHT = 700;

	/** used to pass artwork details between frames (may have to change later */
	private List<Artwork> artworks;
	  // Load from artworks.json at startup
	private Artwork artwork;
	
	/** main menu frame*/
	JFrame frame = new JFrame();
	
	/** panel to contain buttons and elements related to uploading an artwork */
	JPanel uploadPanel;
	
	/** panel to display the title */
	JPanel titlePanel;
	
	/** panel for button area */
	JPanel buttonPanel;
	
	/** label for users to drag artwork into */
	JLabel drag;
	
	/** label for the title*/
	JLabel title;
	
	/** button for navigating to "view artwork" frame */
	JButton viewButton;
	
	/** button for selecting an artwork from the local computer to upload */
	JButton selArtButton;
	
	/** button to upload the artwork into the application */
	JButton uploadButton;
	
	/** button to show short tutorial on how to navigate the current frame */
	JButton infoButton;
	
	/** calls {@link GetLocalDate.java} to get today's date (formatted) */
	GetLocalDate date;
	
	/** text area for user to enter a caption for the artwork */
	JTextArea captionArea;
	
	/** text area for user to enter a title for the artwork */
	JTextArea artTitleArea;
	
	/**
	 * 
	 * Initialized a JFrame that serves as the main menu
	 *  
	 * @param artwork
	 */
	MainMenu() {
		
		artworks = JsonStorage.loadArtworks();
		this.artwork = artwork;
		
		/**
		 * Sets the size, title, and various operations for the main menu frame
		 */
		frame.setTitle("Daily Art Tracker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize (1200, 800); 
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		
		/** 
		 * Sets color and bounds for the upload panel
		 */
		uploadPanel = new JPanel();
		uploadPanel.setLayout(null);
		uploadPanel.setBackground(Color.darkGray);
		uploadPanel.setBounds(500, 0, UPPANELWIDTH, UPPANELHEIGHT);
		
		/**
		 * text area for adding a caption for the artwork
		 */
		captionArea = new JTextArea("Enter a caption here");
		captionArea.setOpaque(true);
		captionArea.setBounds(300, 550, 300, 40);
		captionArea.setLineWrap(true);
		captionArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		captionArea.setCaretColor(Color.black);
		
		/**
		 * Lets user select an image file from their computer to upload
		 */
		selArtButton = new JButton("Select Artwork");
		selArtButton.setFont(new Font(null, Font.PLAIN, 20));
		selArtButton.setBounds(125, 650, 200, 60);
		selArtButton.setFocusable(false);
		selArtButton.addActionListener(this);
		
		/**
		 * Confirmation button for uploading the date's artwork
		 */
		uploadButton = new JButton("Upload");
		uploadButton.setFont(new Font(null, Font.PLAIN, 20));
		uploadButton.setBounds(375, 650, 200, 60);
		uploadButton.setFocusable(false);
		uploadButton.addActionListener(this);
	
		/**
		 * Displays the current date for the user based on the machine
		 * 
		 * @see GetLocalDate.java
		 */
		date = new GetLocalDate();
		JLabel dateLabel = new JLabel(date.FormatDate());
		dateLabel.setFont(new Font("Serif", Font.PLAIN, 30));
		dateLabel.setForeground(new Color(255, 255, 255));
		dateLabel.setBounds(30, 10, 300, 60);
		
		
		/**
		 * Title area for users to enter a title for the artwork
		 */
		artTitleArea = new JTextArea("Enter Title");
		artTitleArea.setBounds(70, 557, 200, 25);	
		artTitleArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		artTitleArea.setCaretColor(Color.black);
		artTitleArea.setOpaque(true);
		
		/**
		 * JLabel for users to physically drag their artwork into to upload
		 */
		drag = new JLabel("Drag your artwork here", SwingConstants.CENTER);
		drag.setFont(new Font("Serif", Font.ITALIC, 30));
		drag.setBounds(125, 80, 450, 450);
		drag.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		drag.setOpaque(true);
		drag.setForeground(Color.lightGray);
		drag.setBackground(Color.white);
		
		/** Handles the drag n drop aspect of the drag JLabel */
		drag.setTransferHandler(new TransferHandler() {
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
                        	drag.setIcon(new ImageIcon(file.getAbsolutePath()));
                        	drag.setText(null);
//                        	artwork.setFilename(file);
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

		/** 
		 * JPanel to display the application's title
		 */
		titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBounds(0, 0, TPANELWIDTH, TPANELHEIGHT);
		
		/** 
		 * Label for the title 
		 */
		title = new JLabel();
		title.setText("Daily Art Tracker");
		title.setFont(new Font("Times New Roman", Font.PLAIN, 50));
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setBorder(BorderFactory.createMatteBorder(5, 0, 5, 0, Color.darkGray));
		
		/**
		 * Panel for buttons to lead to various functions (current and future) 
		 */
		buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setBounds(0, 100, BPANELWIDTH, BPANELHEIGHT);
		
		/**
		 * JButton to lead user to new frame to view old artwork
		 */
		viewButton = new JButton("View artworks");
		viewButton.setFont(new Font(null, Font.PLAIN, 20));
		viewButton.setBounds(150, 30, 200, 60); 
		viewButton.setFocusable(false);
		viewButton.addActionListener(this);
		
		/**
		 * button to show user how to navigate the page
		 */
		infoButton = new JButton();
		infoButton.setIcon(new ImageIcon("src/Info-Button.png"));
		infoButton.setBorderPainted(false);
		infoButton.setBounds(650, 0, 50, 50);
		infoButton.setFocusable(false);
		infoButton.addActionListener(this);
		
		/**
		 * adding all panels and their components to the overall frame
		 */
		titlePanel.add(title, BorderLayout.CENTER);
		uploadPanel.add(selArtButton);
		uploadPanel.add(uploadButton);
		uploadPanel.add(artTitleArea);
		uploadPanel.add(captionArea);
		uploadPanel.add(infoButton);
		uploadPanel.add(drag);
		uploadPanel.add(dateLabel);
		buttonPanel.add(viewButton);
		frame.add(uploadPanel);
		frame.add(titlePanel);
		frame.add(buttonPanel);
		frame.setVisible(true);
	}
		
	/**
	 * listens to button interactions
	 * 
	 * @param e changes depending on which button is clicked
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		/** button to select artwork from disc */
		if (e.getSource() == selArtButton) {
			
			JFileChooser fileChooser = new JFileChooser();
			int response = fileChooser.showOpenDialog(null);
			
			if (response == JFileChooser.APPROVE_OPTION) {
				artwork.setFilename(fileChooser.getSelectedFile().getAbsolutePath());
			}	
		}
		
		/** button to view today's artwork and older artworks */
		if (e.getSource() == viewButton) {
			frame.dispose();
			ArtSelectionMenu artMenu = new ArtSelectionMenu();
		}
		
		/** button to confirm uploading of today's artwork */
		if (e.getSource() == uploadButton) {
			
			
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showOpenDialog(this);
			
			if (result == JFileChooser.APPROVE_OPTION) {
				File selFile = fileChooser.getSelectedFile();
				String title = artTitleArea.getText();
				String caption = captionArea.getText();
//				LocalDate date = artwork.getDate(); // ?
				
				Artwork newArt = new Artwork(title, selFile.getAbsolutePath(), caption, LocalDate.now());
				artworks.add(newArt);
				
				JsonStorage.saveArtworks(artworks);
		        JOptionPane.showMessageDialog(this, "Artwork uploaded and saved!");
				
			}
			
			/*
			if (artwork.getFilename() == null) {
				JOptionPane.showMessageDialog(null, "No artwork uploaded!", "Error", JOptionPane.ERROR_MESSAGE);	

			} else {
				artwork.setCaption(captionArea.getText());
				artwork.setArtTitle(artTitleArea.getText());
				artwork.setDate();
				captionArea.setEditable(false);
				artTitleArea.setEditable(false);
				
				JOptionPane.showMessageDialog(null, "Artwork uploaded!", "Upload confirmation", JOptionPane.INFORMATION_MESSAGE);	
			}
			*/
			
		}
		
		/** button to tell user how to navigate the frame */
		if (e.getSource() == infoButton) {
			JOptionPane.showMessageDialog(null, "You can upload an image by dragging it or selecting the \"Select Artwork\" button.\n"
					+ "Press the \"Upload\" button to upload your artwork for today.\n"
					+ "Remember to add a title and caption for your artwork!.", "Tutorial", JOptionPane.QUESTION_MESSAGE);	

		}
		
	}
	
}

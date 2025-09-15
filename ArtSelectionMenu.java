import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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

import com.toedter.calendar.*;

/**
 * The Art Selection screen. The user can select a date from the calendar to see the artwork saved on that date (if there's any)
 * 
 * @author seanshih
 */
public class ArtSelectionMenu extends JFrame implements ActionListener{
	
	/** Set width for the Artwork Panel */
	private static final int APANELWIDTH = 600;
	/** Set height for the Artwork Panel */
	private static final int APANELHEIGHT = 800;

	/** Set width for the Calendar Panel */
	private static final int CPANELWIDTH = 600;
	/** Set height for the Calendar Panel */
	private static final int CPANELHEIGHT = 800;

	/** */
	private Artwork artwork;
	/** The date selected by the user */
	private Date selDate;
	/** selDate converted to string for comparisons */
	private String convertedDate;
	String[] metadata;
	
	/** Create Queries object to allow for calls to the database */
	Queries query = new Queries();
	/** Create JFrame object to set up the frame */
	JFrame frame = new JFrame();
	
	/** Button to return to main menu */
	JButton backButton;
	/** Button to download current artwork */
	JButton downloadButton;
	/** Button to delete current date's artwork */
	JButton deleteButton;
	/** Button to bring up a small tutorial for the user */
	JButton infoButton;
	
	/** Text area to show the selected artwork's caption */
	JTextArea captionArea;
	/** Text area to show the selected artwork's title */
	JTextArea artTitleArea;
	
	/** Object to store the artwork file to display later */
	ImageIcon art;
	
	/** Label to display the selected artwork (if any) */
	JLabel artworkLabel;
	/** Label to display the title of the frame */
	JLabel title;
//	MainMenu menu;
	
	/**
	 * Constructor, creates the frame and any elements in the frame.
	 */
	ArtSelectionMenu() {
				
		/** Frame */
		
		frame.setTitle("Artworks");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize (1200, 800); 
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		/** Calendar Panel */
		
		JPanel calPanel = new JPanel();
		calPanel.setLayout(null);
		calPanel.setBounds(0, 0, CPANELWIDTH, CPANELHEIGHT);
		
		title = new JLabel();
		title.setBounds(50, 50, 500, 100);
		title.setFont(new Font("Times New Roman", Font.PLAIN, 35));
		title.setText("View Artwork");
		title.setHorizontalAlignment(SwingConstants.LEFT);
		
		JCalendar calendar = new JCalendar();
		calendar.setBounds(50, 150, 500, 400);
		calendar.setBorder(BorderFactory.createEtchedBorder());
		calendar.setMaxSelectableDate(new Date());
		calendar.setMaxDayCharacters(3);
		calendar.setWeekOfYearVisible(false);
		calendar.setSundayForeground(Color.black);
		calendar.setWeekdayForeground(Color.black);
		
		calendar.addPropertyChangeListener("calendar", e -> {
		    selDate = calendar.getDate();
		    convertedDate = selDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();		    
		    metadata = query.selectFromQuery(convertedDate);
		    
		    try {
		    	if (metadata != null) {
		    		
		    		artTitleArea.setText(metadata[0]);
		    		captionArea.setText(metadata[1]);
		    		art = new ImageIcon(metadata[2]);
					artworkLabel.setIcon(art);
					artworkLabel.setText("");
					
//			    	artworkLabel.setIcon(null);
//			    	artworkLabel.setText("No artwork for current date");
//			    	captionArea.setText("");
//			    	artTitleArea.setText("");
//			    	artworkLabel.setBackground(Color.white);
			    }
		    } catch (NullPointerException nullExp) {
		    	artworkLabel.setText("No artwork for current date");
		    	artworkLabel.setForeground(Color.darkGray);
				
		    }
		});

		
		
		/** Art Panel */
		
		JPanel artPanel = new JPanel();
		artPanel.setLayout(null);
		artPanel.setBounds(600, 0, APANELWIDTH, APANELHEIGHT);
		
		/** Dowload and Delete Buttons */
		
		downloadButton = new JButton("Download");
		downloadButton.setFont(new Font(null, Font.PLAIN, 20));
		downloadButton.setBounds(80, 670, 200, 50); 
		downloadButton.setFocusable(false);
		downloadButton.addActionListener(this);
		
		downloadButton.addActionListener(e -> {
			if (metadata != null && metadata[2] != null) {
				
				File sourceFile = new File(metadata[2]);
				
		        if (sourceFile.exists()) {
		        	JFileChooser chooser = new JFileChooser();
		        	chooser.setSelectedFile(new File(sourceFile.getName()));
		        	
		        	int choice = chooser.showSaveDialog(null);
		        	
		        	if (choice == JFileChooser.APPROVE_OPTION) {
		        		File destinationFile = chooser.getSelectedFile();
		        		
		        		try (InputStream in = new FileInputStream(sourceFile);
		                        OutputStream out = new FileOutputStream(destinationFile)) {

		                       byte[] buffer = new byte[1024];
		                       int length;
		                       while ((length = in.read(buffer)) > 0) {
		                           out.write(buffer, 0, length);
		                       }

		                       JOptionPane.showMessageDialog(null, 
		                           "Artwork downloaded successfully to:\n" + destinationFile.getAbsolutePath());
		                   } catch (IOException ex) {
		                       JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage());
		                   }
		        	} else {
		                JOptionPane.showMessageDialog(null, "Original artwork file not found.");
		        	}
		        	
		        } else {
		            JOptionPane.showMessageDialog(null, "No artwork available to download.");
		        }
		    }

			
			
		});
		
		deleteButton = new JButton("Delete");
		deleteButton.setFont(new Font(null, Font.PLAIN, 20));
		deleteButton.setBounds(320, 670, 200, 50); 
		deleteButton.setFocusable(false);
		deleteButton.addActionListener(this);
		
		captionArea = new JTextArea();
		captionArea.setBounds(50, 550, 500, 80);
		captionArea.setLineWrap(true);
		captionArea.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		captionArea.setEditable(false);
		captionArea.setFocusable(false);
		
		artTitleArea = new JTextArea();
		artTitleArea.setBounds(200, 50, 200, 25);	
		artTitleArea.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		artTitleArea.setCaretColor(Color.black);
		artTitleArea.setEditable(false);
		artTitleArea.setFocusable(false);
		
		infoButton = new JButton();
		infoButton.setIcon(new ImageIcon("Info-Button.png"));
		infoButton.setBorderPainted(false);
		infoButton.setBounds(550, 0, 50, 50);
		infoButton.setFocusable(false);
		infoButton.addActionListener(this);
		
		artworkLabel = new JLabel();
		artworkLabel.setOpaque(true);
		artworkLabel.setBackground(Color.white);
		artworkLabel.setHorizontalAlignment(SwingConstants.CENTER);
		artworkLabel.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		artworkLabel.setBounds(75, 80, 450, 450);
		artworkLabel.setPreferredSize(new Dimension(500, 500));		
	
		backButton = new JButton("Back");
		backButton.setFont(new Font(null, Font.PLAIN, 20));
		backButton.setBounds(50, 670, 90, 60); 
		backButton.setFocusable(false);
		backButton.addActionListener(this);
		
		/** Adding all necessary components to their respective panels and the 
		 * adding those panels onto the frame */
		artPanel.add(captionArea);
		artPanel.add(artTitleArea);
		artPanel.add(artworkLabel);
		artPanel.add(downloadButton);
		artPanel.add(deleteButton);
		artPanel.add(infoButton);
		frame.add(artPanel);
		calPanel.add(backButton);
		calPanel.add(title);
		calPanel.add(calendar);
		frame.add(calPanel);
		frame.setVisible(true);
		
		
	}
	
	/** 
	 * Receives inputs from all buttons and executes a code block depending on the source
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == backButton) {
			frame.dispose();
			MainMenu menu = new MainMenu();
		}
		
		if (e.getSource() == deleteButton) {
			int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this artwork?", "Delete artwork?", JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_NO_OPTION) {
				query.deleteArtQuery(metadata[3]);
			} 
			
		}
		
		if (e.getSource() == infoButton) {
			JOptionPane.showMessageDialog(null, "Click on a date on the calendar to view that date's artwork!\n"
					+ "Press the \"Download\" button to download an artwork from the selected date.\n"
					+ "Press the \"Delete\" to delete a date's artwork.", "Tutorial", JOptionPane.QUESTION_MESSAGE);	
		}	
	}
}
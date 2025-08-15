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
import java.io.IOException;
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


public class ArtSelectionMenu extends JFrame implements ActionListener{
	
	private static final int APANELWIDTH = 600;
	private static final int APANELHEIGHT = 800;

	private static final int CPANELWIDTH = 600;
	private static final int CPANELHEIGHT = 800;

	private Artwork artwork;
	private Date selDate;
	private LocalDate conDate;
	
	JFrame frame = new JFrame();
	JButton backButton;
	JButton downloadButton;
	JButton deleteButton;
	JButton infoButton;
//	JButton uploadButton;
	JTextArea captionArea;
	JTextArea artTitleArea;
	MainMenu menu;
	ImageIcon art;
	JLabel artworkLabel;
	JLabel title;
	
	ArtSelectionMenu(Artwork artwork) {
		
		
		this.artwork = artwork;
		// this will need to be replaced so that this.artwork grabs attributes from an artwork in the database
		
		// ----------------------- Frame -------------------------------
		
		frame.setTitle("Artworks");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize (1200, 800); 
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		// -------------------- Calendar Panel -------------------------
		
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
		

//				calendar.addPropertyChangeListener("calendar", new PropertyChangeListener() {
//					@Override
//					public void propertyChange(PropertyChangeEvent evt) {
//						Date selDate = calendar.getDate();
//						dateLabel.setText("Selected Date: " + selDate.toString());
//						// TODO Auto-generated method stub	
//					}
//				});
		
		calendar.addPropertyChangeListener("calendar", e -> {
		    selDate = calendar.getDate();
		    conDate = selDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		    LocalDate tempDate = artwork.getDate();
		    try {
			    if (tempDate.toString().equals(conDate.toString())) {
			    	captionArea.setText(artwork.getCaption());
					artTitleArea.setText(artwork.getArtTitle());
					art = new ImageIcon(artwork.getFilename().toString());
					artworkLabel.setIcon(art);
					artworkLabel.setText("");
			    } else {
			    	artworkLabel.setIcon(null);
			    	artworkLabel.setText("No artwork for current date");
			    	captionArea.setText("");
			    	artTitleArea.setText("");
			    	artworkLabel.setBackground(Color.white);
			    }
		    } catch (NullPointerException nullExp) {
		    	artworkLabel.setText("No artwork for current date");
		    	artworkLabel.setForeground(Color.darkGray);
				
		    }
		});
		
		// ----------------------- Art Panel ---------------------------
		
		JPanel artPanel = new JPanel();
//		JPanel uploadPanel = new JPanel();
		artPanel.setLayout(null);
		artPanel.setBounds(600, 0, APANELWIDTH, APANELHEIGHT);
//		artPanel.setBackground(Color.darkGray);
		
		// --------------------- Download/Delete -----------------------
		
		downloadButton = new JButton("Download");
		downloadButton.setFont(new Font(null, Font.PLAIN, 20));
		downloadButton.setBounds(80, 670, 200, 50); 
		downloadButton.setFocusable(false);
		downloadButton.addActionListener(this);
		
		downloadButton.addActionListener(e -> {
			if (artwork.getFilename() == null) {
		        JOptionPane.showMessageDialog(frame, "The current date does not have any artwork saved", "Error", JOptionPane.ERROR_MESSAGE);
		        return;
		    }

			File ogFile = new File(artwork.getFilename().toString());
		    if (!ogFile.exists()) {
		        JOptionPane.showMessageDialog(frame, "Original file not found!", "Error", JOptionPane.ERROR_MESSAGE);
		        return;
		    }
		    
		    JFileChooser fChooser = new JFileChooser();
		    fChooser.setDialogTitle("Save Artwork As");
		    fChooser.setSelectedFile(new File(ogFile.getName()));
		    
		    int choice = fChooser.showSaveDialog(frame);
		    if (choice == JFileChooser.APPROVE_OPTION) {
		    	File savefile = fChooser.getSelectedFile();
		    	try {
		    		Files.copy(ogFile.toPath(), savefile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		    		JOptionPane.showMessageDialog(frame, "Artwork saved to: " + savefile.getAbsolutePath(), "Download Complete", JOptionPane.INFORMATION_MESSAGE, null);
		    	} catch (IOException exp) {
		    		JOptionPane.showMessageDialog(frame, "Failed to save artwork!", "Error", JOptionPane.ERROR_MESSAGE);
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
		infoButton.setIcon(new ImageIcon("src/Info-Button.png"));
		infoButton.setBorderPainted(false);
		infoButton.setBounds(550, 0, 50, 50);
		infoButton.setFocusable(false);
		infoButton.addActionListener(this);
		
		
		// prob need to surround with try/catch to catch filename errors
		artworkLabel = new JLabel();
		artworkLabel.setOpaque(true);
		artworkLabel.setBackground(Color.white);
//		try {
//			if (conDate.isEqual(artwork.getDate())) { // works
//				System.out.println("works");
//				
//			}
//		} catch (Exception e){
//			artworkLabel.setText("No artwork for current date");
//			artworkLabel.setBackground(Color.white);
//			artworkLabel.setHorizontalAlignment(SwingConstants.CENTER);
//			artworkLabel.setBorder(BorderFactory.createLineBorder(Color.black, 5));
//		}
		artworkLabel.setHorizontalAlignment(SwingConstants.CENTER);
		artworkLabel.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		artworkLabel.setBounds(75, 80, 450, 450);
		artworkLabel.setPreferredSize(new Dimension(500, 500));		
	
		backButton = new JButton("Back");
		backButton.setFont(new Font(null, Font.PLAIN, 20));
		backButton.setBounds(50, 670, 90, 60); 
		backButton.setFocusable(false);
		backButton.addActionListener(this);
		
		
		artPanel.add(captionArea);
		artPanel.add(artTitleArea);
		artPanel.add(artworkLabel);
//		artPanel.add(uploadButton);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == backButton) {
			frame.dispose();
			MainMenu menu = new MainMenu(artwork);
		}
		
		if (e.getSource() == deleteButton) {
			
			if (artwork.getFilename() == null && artwork.getArtTitle() == null && artwork.getCaption() == null) {
				JOptionPane.showMessageDialog(null, "There is no artwork to delete", "Error", JOptionPane.WARNING_MESSAGE);
			} else {
				int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this artwork?", "Delete artwork?", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					this.artwork.setArtTitle(null);
					this.artwork.setCaption(null);
					this.artwork.setFilename(null);		
					captionArea.setText(null);
					artTitleArea.setText(null);
					artworkLabel.setIcon(null);
					artworkLabel.setText("No artwork for current date");
			    	artworkLabel.setForeground(Color.darkGray);
				} 
			}
		}
		
		if (e.getSource() == infoButton) {
			JOptionPane.showMessageDialog(null, "Click on a date on the calendar to view that date's artwork!\n"
					+ "Press the \"Download\" button to download an artwork from the selected date.\n"
					+ "Press the \"Delete\" to delete a date's artwork.", "Tutorial", JOptionPane.QUESTION_MESSAGE);	

		}
		
	}
	
}

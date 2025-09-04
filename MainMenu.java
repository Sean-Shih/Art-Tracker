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

public class MainMenu extends JFrame implements ActionListener {

	private static final int UPPANELWIDTH = 700;
	private static final int UPPANELHEIGHT = 800;
	
	private static final int TPANELWIDTH = 500;
	private static final int TPANELHEIGHT = 100;
	
	private static final int BPANELWIDTH = 500;
	private static final int BPANELHEIGHT = 700;

	private Artwork artwork;
	
	Queries query = new Queries();
	
	JFrame frame = new JFrame();
	JButton viewButton;
	JButton selArtButton;
	JButton uploadButton;
	JButton infoButton;
	GetLocalDate date;
	JTextArea captionArea;
	JTextArea artTitleArea;
	ImageIcon previewImage = null;
	File physFile;
	
	
	MainMenu() {
		
		this.artwork = artwork;
		
		// ----------------------- Frame -------------------------------
		
//		JFrame frame = new JFrame();
		frame.setTitle("Daily Art Tracker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize (1200, 800); 
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		// -------------------- Upload Art Panel -----------------------
		
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
		
//		Border border = BorderFactory.createLineBorder(Color.black, 3);
		
		date = new GetLocalDate();
		JLabel dateLabel = new JLabel(date.FormatDate());
		dateLabel.setFont(new Font("Serif", Font.PLAIN, 30));
		dateLabel.setForeground(new Color(255, 255, 255));
//		dateLabel.setBorder(border);
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

                    for (File file : files) { // may have to change to explicitly needing to press "upload" to upload
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
		
		
		// -------------------- Title Panel ----------------------------

		JPanel titlePanel = new JPanel(new BorderLayout());
//		titlePanel.setBackground(new Color(192,192,192));
		titlePanel.setBounds(0, 0, TPANELWIDTH, TPANELHEIGHT);
		
		JLabel title = new JLabel();
		title.setText("Daily Art Tracker");
		title.setFont(new Font("Times New Roman", Font.PLAIN, 50));
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setBorder(BorderFactory.createMatteBorder(5, 0, 5, 0, Color.darkGray));
		
		// ---------------------- Button Panel --------------------------
		
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
	
	public String getCaption() {
		return captionArea.getText();
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == selArtButton) {
			
			JFileChooser fileChooser = new JFileChooser();
			int response = fileChooser.showOpenDialog(null);
			
			if (response == JFileChooser.APPROVE_OPTION) {
				artwork.setFilename(fileChooser.getSelectedFile().getAbsolutePath());
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
//				artwork.setCaption(captionArea.getText());
//				artwork.setArtTitle(artTitleArea.getText());
//				artwork.setDate();
//				captionArea.setEditable(false);
//				artTitleArea.setEditable(false);
				
//				query.addArtQuery(artwork.getArtTitle(), artwork.getCaption(), artwork.getFilename(), artwork.getDate());
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
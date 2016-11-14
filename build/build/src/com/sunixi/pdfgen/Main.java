package com.sunixi.pdfgen;
	
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class Main extends Application {
	private Desktop desktop = Desktop.getDesktop();
	File selectedImageDirectory =null;
    File selectedPdfDirectory =null;
    static final String[] EXTENSIONS = new String[]{
            "gif", "png", "bmp","JPG" // and other formats you need
        };
    
 // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
    @Override
    public void start(final Stage stage) {
        stage.setTitle("Sunixi- Choose File ");
 
        final Button browseButton = new Button("Choose Directory Where images are kept");
        final Button openMultipleButton = new Button("Save PDF");
        
        browseButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    final DirectoryChooser directoryChooser =
                        new DirectoryChooser();
                    final File selectedDirectory =
                            directoryChooser.showDialog(stage);
                    if (selectedDirectory != null) {
                    	selectedDirectory.getAbsolutePath();
                    	selectedImageDirectory=selectedDirectory;
                    }
                }
            }
        );
 
       
 
        openMultipleButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        final DirectoryChooser directoryChooser =
                            new DirectoryChooser();
                        final File selectedDirectory =
                                directoryChooser.showDialog(stage);
                        if (selectedDirectory != null) {
                            selectedDirectory.getAbsolutePath();
                            selectedPdfDirectory=selectedDirectory;
                            
                            System.out.println("selectepdfDirecotry"+selectedPdfDirectory.getAbsolutePath());
                            System.out.println("selectedimageDirecotry"+selectedImageDirectory.getAbsolutePath());
                                
                            try {
                                //here direcotry is accessed and files should be chooosed ..
                                    if (selectedImageDirectory.isDirectory() && selectedPdfDirectory.isDirectory()) { // make sure it's a directory
                                        for (final File f : selectedImageDirectory.listFiles(IMAGE_FILTER)) {
                                            BufferedImage img = null;
                                            img = ImageIO.read(f);
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            ImageIO.write( img, "jpg", baos );
                                            baos.flush();
                                            byte[] imageInByte = baos.toByteArray();
                                            baos.close();
                                                
                                                Document document = new Document();
                                                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(selectedPdfDirectory.getAbsolutePath()+"/"+f.getName()+".pdf"));
                                                    document.open();
                                                    document.add(new Paragraph("Some content here"));
                                                 
                                                    //Set attributes here
                                                    document.addAuthor("Lokesh Gupta");
                                                    document.addCreationDate();
                                                    document.addCreator("HowToDoInJava.com");
                                                    document.addTitle("Set Attribute Example");
                                                    document.addSubject("An example to show how attributes can be added to pdf files.");
                                                    
                                                    
                                                  //Add Image
                                                    Image image1 = Image.getInstance(imageInByte);
                                                    //Fixed Positioning
                                                    image1.setAbsolutePosition(100f, 550f);
                                                    //Scale to new height and new width of image
                                                    image1.scaleAbsolute(200, 200);
                                                    //Add to document
                                                    document.add(image1);
                                                    document.close();
                                                    writer.close();

                                                // you probably want something more involved here
                                                // to display in your UI
                                                System.out.println("image: " + f.getName());
                                                System.out.println(" width : " + img.getWidth());
                                                System.out.println(" height: " + img.getHeight());
                                                System.out.println(" size  : " + f.length());
                                            } 
                                        }
                                    
                      
                            		
        						
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                            
                            
                        }
                        
                        
                                
                                    
                           
                        
                        
                        
                        
                    }
                }
            );
 
 
        final GridPane inputGridPane = new GridPane();
 
        GridPane.setConstraints(browseButton, 0, 0);
        GridPane.setConstraints(openMultipleButton, 1, 0);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(browseButton, openMultipleButton);
 
        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));
 
        stage.setScene(new Scene(rootGroup));
        stage.show();
    }
 
    public static void main(String[] args) {
        Application.launch(args);
    }
 
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
            		Main.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }
    }
}
	


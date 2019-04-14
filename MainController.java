package bookinventory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 * @author nazem
 */
public class MainController implements Initializable {
    
    @FXML
    private Button fullList;
    
    @FXML
    private Button insertList;
    
    @FXML
    private Button fullListGenre;
    
    @FXML
    private Button insertGenre;
    
    @FXML
    private Label inputLbl;  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    @FXML
    protected void insertAction(ActionEvent event) throws IOException, Exception{
        ((Node) (event.getSource())).getScene().getWindow().hide();
         
        Parent root = FXMLLoader.load(getClass().getResource("bookinsert.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Insert Book");
        stage.setScene(scene);
        stage.show();
        
    }
    
    @FXML
    protected void seeAllAction(ActionEvent event) throws IOException, Exception{
        ((Node) (event.getSource())).getScene().getWindow().hide(); 
        
        Parent root = FXMLLoader.load(getClass().getResource("booksall.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("All Books");
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    protected void insertGenreAction(ActionEvent event) throws IOException, Exception{
        ((Node) (event.getSource())).getScene().getWindow().hide();
         
        Parent root = FXMLLoader.load(getClass().getResource("genreinsert.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Insert Genre");
        stage.setScene(scene);
        stage.show();
        
    }
    
    @FXML
    protected void seeAllGenreAction(ActionEvent event) throws IOException, Exception{
        ((Node) (event.getSource())).getScene().getWindow().hide(); 
        
        Parent root = FXMLLoader.load(getClass().getResource("genreall.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("All Genres");
        stage.setScene(scene);
        stage.show();
    }
    
    

}




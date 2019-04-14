package bookinventory;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Window;

/**
 * @author nazem
 */

public class GenreInsertController implements Initializable {
    @FXML
    private TextField genreName;

    @FXML
    private Button goBack; //
    
    @FXML
    private Button insertGenre;
    private Connection conn;
    private PreparedStatement prestmt;
    private PreparedStatement genrestmt;
    private ResultSet res = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String dbName="bookinventory";
        String userName="root";
        String password="";

        try {
            conn= DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,userName,password);
        } catch (Exception e) {
            e.printStackTrace();
        }     
    }    
    
    @FXML
    protected void goBackAction(ActionEvent event) throws Exception, IOException{
        ((Node) (event.getSource())).getScene().getWindow().hide();
        
        Parent root = FXMLLoader.load(getClass().getResource("firstpage.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Book Inventory");
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    protected void insertGenreAction(ActionEvent event) throws IOException, Exception{
        prestmt = conn.prepareStatement("INSERT INTO genre (genre_name) VALUES (?)");
        prestmt.setString(1, genreName.getText());
        prestmt.executeUpdate();  
        
        ((Node) (event.getSource())).getScene().getWindow().hide();
        
        Parent root = FXMLLoader.load(getClass().getResource("genreall.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("All Genres");
        stage.setScene(scene);
        stage.show();
    } 
    
}


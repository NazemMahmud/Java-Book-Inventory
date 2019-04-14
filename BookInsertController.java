package bookinventory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Window;

/**
 * @author nazem
 */
public class BookInsertController implements Initializable {
    @FXML
    private TextField inputBookName;

    @FXML
    private TextField inputWriterName;

    @FXML
    private TextField price;
    
    @FXML
    private ComboBox genre;


    @FXML
    private Button goBack;
    
    @FXML
    private Button insertBook;
    private Connection conn;
    private PreparedStatement prestmt;
    private PreparedStatement genrestmt;
    private ResultSet res = null;
    final ObservableList<String> options = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        String dbName="bookinventory";
        String userName="root";
        String password="";

        try {
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn= DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,userName,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            genrestmt = conn.prepareStatement("SELECT genre_name FROM genre");
            res = genrestmt.executeQuery();
            while (res.next()) {
                options.add(res.getString("genre_name"));            
            }  
        } catch (SQLException ex) {
            Logger.getLogger(BookInsertController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        genre.setItems(options);
        
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
    protected void insertBookAction(ActionEvent event) throws IOException, Exception{
//        Window owner = submitButton.getScene().getWindow();
        
        prestmt = conn.prepareStatement("SELECT * FROM genre WHERE genre_name= ?");
        prestmt.setString(1, (String) genre.getValue());
        res = prestmt.executeQuery();
        int genreId = 0;
        if(res.next()){
            genreId = Integer.parseInt(res.getString("id"));
        }
          
        prestmt = conn.prepareStatement("INSERT INTO books (book_name, writer_name, genre, price) VALUES (?, ?, ?, ?)");
        prestmt.setString(1, inputBookName.getText());
        prestmt.setString(2, inputWriterName.getText());
        prestmt.setInt(3, genreId);
        prestmt.setString(4, price.getText());
        prestmt.executeUpdate();  
        
        ((Node) (event.getSource())).getScene().getWindow().hide();
        
        Parent root = FXMLLoader.load(getClass().getResource("booksall.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("All Books");
        stage.setScene(scene);
        stage.show();
    } 
    
}

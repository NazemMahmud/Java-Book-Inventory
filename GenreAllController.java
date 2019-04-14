//https://stackoverflow.com/questions/32940335/how-to-update-observablelist-in-tableview-to-write-to-db
//https://www.youtube.com/watch?v=_FBPukQOLCY
package bookinventory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javafx.util.Callback;


/**
 * FXML Controller class
 *
 * @author nazem
 */
public class GenreAllController implements Initializable {
    @FXML
    private TextField genreId;
            
    @FXML
    private TextField genreName;
    
    @FXML
    private Button search;
    
    @FXML
    private Button update;
    
    @FXML
    private Button delete;
    
    @FXML
    private Button goBack;
    
    @FXML 
    private Button insertGenre;
    
    @FXML
    private TableView<Genre> tableview;
    
    @FXML
    private TableColumn<Genre, String> col_id;
    
    @FXML
    private TableColumn<Genre, String> col_name;
    
//    private SimpleStringProperty ;
    
    private Connection con;
    private PreparedStatement genrestmt;
    private ResultSet res = null;
    private ObservableList<Genre> genreList = FXCollections.observableArrayList();
    
    private int ID; private String genre_name;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            con = DBConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        try {
            genrestmt = con.prepareStatement("SELECT * FROM genre ORDER BY id DESC");
            res = genrestmt.executeQuery();
            int counter = 0;
            while (res.next()) {
                counter++;
                genreList.add(new Genre(res.getInt("id"), res.getString("genre_name") ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenreAllController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        tableview.setItems(genreList);
    }    
    
    @FXML
    protected void searchAction(ActionEvent event) throws SQLException, Exception, IOException, ClassNotFoundException{
        final ObservableList<Genre> searchGenreList = FXCollections.observableArrayList();
        tableview.getItems().clear();
        try {
//            System.out.println(genreName.getText());
            
            genre_name = genreName.getText();
            if(genreId.getText().trim().isEmpty()){
                ID = 0;
            }else{ ID = Integer.parseInt(genreId.getText()); }
            if(genreName.getText().trim().isEmpty()){
                genre_name = " ";
            }
            genrestmt = con.prepareStatement("SELECT * FROM genre where id=? OR genre_name LIKE ?");
            genrestmt.setInt(1, ID);
            genrestmt.setString(2, '%'+genre_name+'%');
            res = genrestmt.executeQuery();
            System.out.println(genrestmt);
            /*if (res.next()) {
                System.out.println("searched");
            } else {
                System.out.println("not updated");
            } */ 
            int counter = 0;
            while (res.next()) {
                counter++;
                System.out.println("What!!!:: "+res.getString("genre_name"));
                searchGenreList.add(new Genre(res.getInt("id"), res.getString("genre_name") ));
            }
             System.out.println(searchGenreList);
        } catch (SQLException ex) {
            Logger.getLogger(GenreAllController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));       
        tableview.setItems(searchGenreList);
    
    }
    
    @FXML
    protected void updateAction(ActionEvent event) throws IOException, Exception{
        final ObservableList<Genre> updatedList = FXCollections.observableArrayList();
        tableview.getItems().clear();
        try {
            genrestmt = con.prepareStatement("UPDATE genre set genre_name=? where id=?");
            genrestmt.setString(1, genreName.getText());
            genrestmt.setInt(2, Integer.parseInt(genreId.getText()));
            int i = genrestmt.executeUpdate();
            
            
            genrestmt = con.prepareStatement("SELECT * FROM genre ORDER BY id DESC");
            res = genrestmt.executeQuery();
            int counter = 0;
            while (res.next()) {
                counter++;
                updatedList.add(new Genre(res.getInt("id"), res.getString("genre_name") ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenreAllController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        tableview.setItems(updatedList);
    }
    
    @FXML
    protected void deleteAction(ActionEvent event) throws IOException, Exception{
        final ObservableList<Genre> deletedList = FXCollections.observableArrayList();
        tableview.getItems().clear();
        try {
            genrestmt = con.prepareStatement("delete from genre where id=? OR genre_name LIKE ? ");
            genrestmt.setInt(1, Integer.parseInt(genreId.getText()));
            genrestmt.setString(2, '%'+genreName.getText()+'%');
            int i = genrestmt.executeUpdate();
            
            
            genrestmt = con.prepareStatement("SELECT * FROM genre ORDER BY id DESC");
            res = genrestmt.executeQuery();
            int counter = 0;
            while (res.next()) {
                counter++;
                deletedList.add(new Genre(res.getInt("id"), res.getString("genre_name") ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenreAllController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        tableview.setItems(deletedList);
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
        ((Node) (event.getSource())).getScene().getWindow().hide();
         
        Parent root = FXMLLoader.load(getClass().getResource("genreinsert.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Insert Genre");
        stage.setScene(scene);
        stage.show();
        
    }
}

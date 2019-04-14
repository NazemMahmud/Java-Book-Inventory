
package bookinventory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.beans.value.ObservableValue;
//import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;

import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javafx.util.Callback;

/**
 * @author nazem
 */
public class BooksAllController implements Initializable {
    @FXML
    private TextField bookId;
    
    @FXML
    private TextField bookName;
    
    @FXML
    private TextField writerName;
    
    @FXML
    private ComboBox genreName;
    
    @FXML
    private TextField price;
    
    @FXML
    private Button search;
    
    @FXML
    private Button update;
    
    @FXML
    private Button delete;
    
    @FXML
    private Button insertBook;
    
    @FXML
    private Button goBack;
    
    @FXML
    private TableView<Books> tableview;
    
    @FXML
    private TableColumn<Books, String> col_id;
    
    @FXML
    private TableColumn<Books, String> col_book_name;
    
    @FXML
    private TableColumn<Books, String> col_writer_name;
    
    @FXML
    private TableColumn<Books, String> col_genre_name;
    
    @FXML
    private TableColumn<Books, String> col_price;
    
    private Connection con;
    private PreparedStatement prestmt;
    private ResultSet res = null;
    final ObservableList<Books> bookList = FXCollections.observableArrayList();
    final ObservableList<String> options = FXCollections.observableArrayList();
    
    private int ID, Price; private String book_name, writer_name, genre_name;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            con = DBConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        try {
            prestmt = con.prepareStatement("SELECT books.id, books.book_name, books.writer_name, genre.genre_name, books.price "
                    + "FROM books JOIN genre ON books.genre = genre.id order by books.id desc");
            res = prestmt.executeQuery();
            int counter = 0;
            while (res.next()) {
                counter++; // Integer.toString(counter)
                bookList.add(new Books(res.getInt("id"), res.getString("book_name"), 
                        res.getString("writer_name"), res.getString("genre_name"), res.getString("price") ));
                System.out.println("Show: " + bookList);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(GenreAllController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_book_name.setCellValueFactory(new PropertyValueFactory<>("book_name"));
        col_writer_name.setCellValueFactory(new PropertyValueFactory<>("writer_name"));
        col_genre_name.setCellValueFactory(new PropertyValueFactory<>("genre_name"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        tableview.setItems(bookList);
        
        try {
            prestmt = con.prepareStatement("SELECT genre_name FROM genre");
            res = prestmt.executeQuery();
            while (res.next()) {
                options.add(res.getString("genre_name"));            
            }  
        } catch (SQLException ex) {
            Logger.getLogger(BookInsertController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        genreName.setItems(options);
    }    
    
    @FXML
    protected void searchAction(ActionEvent event) throws SQLException, Exception, IOException, ClassNotFoundException{
        final ObservableList<Books> searchList = FXCollections.observableArrayList();
        tableview.getItems().clear();
        try {
//            System.out.println("off "+ bookId.getText());
            book_name = bookName.getText();
            writer_name = writerName.getText();
            genre_name = (String) genreName.getValue();// genreName.getText();
            if(bookId.getText().trim().isEmpty()){
                ID = 0;
            }else{ ID = Integer.parseInt(bookId.getText()); }
            
            if(price.getText().trim().isEmpty()){
                Price = 0;
            }else{ Price = Integer.parseInt(price.getText()); }
            
            if(bookName.getText().trim().isEmpty()){
                book_name = "!";
            }  
            if(writerName.getText().trim().isEmpty()){
                writer_name = "!";
            }  
//            if(genreName.getValue().trim().isEmpty()){
//                genre_name = "!";
//            }   
            prestmt = con.prepareStatement("SELECT books.id, books.book_name, books.writer_name, genre.genre_name, books.price "
                    + "FROM books JOIN genre ON books.genre = genre.id where books.id=? OR books.book_name LIKE ? OR"
                    + " books.writer_name LIKE ? OR genre.genre_name LIKE ? OR books.price=?"); 
            prestmt.setInt(1, ID);
            prestmt.setString(2, '%'+book_name+'%');
            prestmt.setString(3, '%'+writer_name+'%');
            prestmt.setString(4, '%'+genre_name+'%');
            prestmt.setInt(5, Price);
//            System.out.println(prestmt);
            res = prestmt.executeQuery();
            int counter = 0;
            while (res.next()) {
                counter++;
//                System.out.println("What!!!:: "+res.getString("genre_name"));
                searchList.add(new Books(res.getInt("id"), res.getString("book_name"), 
                        res.getString("writer_name"), res.getString("genre_name"), res.getString("price") ));
            }
//             System.out.println(searchGenreList);
        } catch (SQLException ex) {
            Logger.getLogger(GenreAllController.class.getName()).log(Level.SEVERE, null, ex);
        }
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_book_name.setCellValueFactory(new PropertyValueFactory<>("book_name"));
        col_writer_name.setCellValueFactory(new PropertyValueFactory<>("writer_name"));
        col_genre_name.setCellValueFactory(new PropertyValueFactory<>("genre_name"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("price"));       
        tableview.setItems(searchList);
    }
    
    @FXML
    protected void updateAction(ActionEvent event) throws IOException, Exception, SQLException{
        final ObservableList<Books> updatedList = FXCollections.observableArrayList();
        tableview.getItems().clear();
        try {
            prestmt = con.prepareStatement("UPDATE books set book_name=?, writer_name=?, price=? where id=?");
            prestmt.setString(1, bookName.getText());
            prestmt.setString(2, writerName.getText());
            prestmt.setDouble(3, Double.parseDouble(price.getText()));
            prestmt.setInt(4, Integer.parseInt(bookId.getText()));
            int i = prestmt.executeUpdate();
            
            prestmt = con.prepareStatement("SELECT * FROM genre where genre_name=?");
            prestmt.setString(1, (String) genreName.getValue());
            res = prestmt.executeQuery();
//            int counter = 0;
            if (res.next()) {
                prestmt = con.prepareStatement("UPDATE books set genre=? where id=?");
                prestmt.setInt(1, res.getInt("id"));
                prestmt.setInt(2, Integer.parseInt(bookId.getText()));
                i = prestmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenreAllController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            prestmt = con.prepareStatement("SELECT books.id, books.book_name, books.writer_name, genre.genre_name, books.price "
                    + "FROM books JOIN genre ON books.genre = genre.id order by books.id desc");
            res = prestmt.executeQuery();
            int counter = 0;
            while (res.next()) {
                counter++; // Integer.toString(counter)
                updatedList.add(new Books(res.getInt("id"), res.getString("book_name"), 
                        res.getString("writer_name"), res.getString("genre_name"), res.getString("price") ));
                System.out.println("Show: " + updatedList);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(GenreAllController.class.getName()).log(Level.SEVERE, null, ex);
        }
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_book_name.setCellValueFactory(new PropertyValueFactory<>("book_name"));
        col_writer_name.setCellValueFactory(new PropertyValueFactory<>("writer_name"));
        col_genre_name.setCellValueFactory(new PropertyValueFactory<>("genre_name"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("price"));       
        tableview.setItems(updatedList);
    }
    
    @FXML
    protected void deleteAction(ActionEvent event) throws IOException, Exception{
        final ObservableList<Books> deletedList = FXCollections.observableArrayList();
        tableview.getItems().clear();
        try {
            book_name = bookName.getText();
            writer_name = writerName.getText();
            genre_name = (String) genreName.getValue();
            if(bookId.getText().trim().isEmpty()){
                ID = 0;
            }else{ ID = Integer.parseInt(bookId.getText()); }
            
            if(price.getText().trim().isEmpty()){
                Price = 0;
            }else{ Price = Integer.parseInt(price.getText()); }
            
            if(bookName.getText().trim().isEmpty()){
                book_name = "!";
            }  
            if(writerName.getText().trim().isEmpty()){
                writer_name = "!";
            }  
            int genre_id = 0;
            if(genre_name!=null){
                prestmt = con.prepareStatement("select * from genre where genre_name=? ");
                prestmt.setString(1, genre_name);
                res = prestmt.executeQuery();
//                System.out.println("Genre: " + prestmt);
                if(res.next()){
                    genre_id = res.getInt("id");
                }
            }
            
            prestmt = con.prepareStatement("DELETE from books where id=? OR book_name LIKE ? OR writer_name LIKE ? OR genre=? OR price=? "); 
            prestmt.setInt(1, ID);
            prestmt.setString(2, '%'+book_name+'%');
            prestmt.setString(3, '%'+writer_name+'%');
            prestmt.setInt(4, genre_id);
            prestmt.setDouble(5, Price);
            int i = prestmt.executeUpdate();
//                             System.out.println("delete Show: " + prestmt);
             
            prestmt = con.prepareStatement("SELECT books.id, books.book_name, books.writer_name, genre.genre_name, books.price "
                    + "FROM books JOIN genre ON books.genre = genre.id order by books.id desc");
            res = prestmt.executeQuery();
            int counter = 0;
            while (res.next()) {
                counter++; // Integer.toString(counter)
                deletedList.add(new Books(res.getInt("id"), res.getString("book_name"), 
                        res.getString("writer_name"), res.getString("genre_name"), res.getString("price") ));
            }

        } catch (SQLException ex) {
            Logger.getLogger(GenreAllController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_book_name.setCellValueFactory(new PropertyValueFactory<>("book_name"));
        col_writer_name.setCellValueFactory(new PropertyValueFactory<>("writer_name"));
        col_genre_name.setCellValueFactory(new PropertyValueFactory<>("genre_name"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("price"));       
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
    protected void insertBookAction(ActionEvent event) throws IOException, Exception{
        ((Node) (event.getSource())).getScene().getWindow().hide();
         
        Parent root = FXMLLoader.load(getClass().getResource("bookinsert.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Insert Book");
        stage.setScene(scene);
        stage.show();
        
    }
}
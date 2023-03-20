module com.example.createtask {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.createtask to javafx.fxml;
    exports com.example.createtask;
}
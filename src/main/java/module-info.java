module com.example.bjszerver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bjszerver to javafx.fxml;
    exports com.example.bjszerver;
}
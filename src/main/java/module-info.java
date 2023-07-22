module com.example.seconddatastructureproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.seconddatastructureproject to javafx.fxml;
    exports com.example.seconddatastructureproject;
}
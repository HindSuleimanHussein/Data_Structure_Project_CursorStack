package com.example.seconddatastructureproject;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        CursorStack<String> fileStack = new CursorStack<>(20);
        CursorStack<String> equationsAreaStack = new CursorStack<>(20);
        CursorStack<String> filesAreaStack = new CursorStack<>(20);
        CursorStack<String> textFieldStack = new CursorStack<>(20);

        // for Scene
        VBox sceneBox = new VBox(5);
        Label equationsLabel = new Label("Equations");
        equationsLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
        equationsLabel.setTranslateX(50);
        equationsLabel.setTranslateY(10);

        Label filesLabel = new Label("Files");
        filesLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
        filesLabel.setTranslateX(50);
        filesLabel.setTranslateY(10);

        Label informUser = new Label();
        informUser.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
        informUser.setTranslateX(50);
        informUser.setTranslateY(10);
        informUser.setTextFill(Color.web("#006400"));

        //for the TextArea
        TextArea equationsArea = new TextArea();
        equationsArea.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        equationsArea.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));
        equationsArea.setMaxSize(500, 300);
        equationsArea.setTranslateX(50);
        equationsArea.setTranslateY(10);

        ListView<String> filesArea = new ListView<>();
        filesArea.setBorder(new Border(new BorderStroke(Color.VIOLET, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        filesArea.setMaxSize(500, 100);
        filesArea.setTranslateX(50);
        filesArea.setTranslateY(10);
        filesArea.setStyle("-fx-text-fill: navy; -fx-font-size: 16px;");

        VBox equationBox = new VBox(10);
        equationBox.getChildren().addAll(equationsLabel, equationsArea, filesLabel, filesArea, informUser);

        // for Buttons and TextField
        HBox buttonsBox = new HBox(30);
        Button back = new Button("Back");
        back.setStyle("-fx-border-radius: 90;");
        back.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
        back.setStyle("-fx-border-color: #00008B; " + "-fx-border-radius: 30px;" +
                "-fx-background-radius: 30px;" + "-fx-border-width: 4px");
        back.setTextFill(Color.DARKBLUE);
        back.setDisable(true);

        TextField textField = new TextField();
        textField.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
        textField.setPrefWidth(600);
        textField.setBorder(new Border(new BorderStroke(Color.FORESTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));
        textField.setStyle("-fx-text-fill: forestgreen; -fx-font-size: 16px;");

        Button load = new Button("Load");
        load.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
        load.setStyle("-fx-border-color: #A52A2A; " + "-fx-border-radius: 30px;" +
                 "-fx-background-radius: 30px;" + "-fx-border-width: 4px");
        load.setTextFill(Color.BROWN);

        //files
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open My Files");
        //fileChooser.setInitialDirectory(new File("Files"));

        load.setOnAction(e->{
            filesArea.getItems().clear();
            File selectedFile = fileChooser.showOpenDialog(stage);
            textField.setText(selectedFile.getPath());
            fileStack.push(selectedFile.getPath());

            String str = "";
            String strFileArea ="";
            try{
                equationsArea.clear();
                Scanner sc = null;
                try {
                    sc = new Scanner(selectedFile);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                while (sc.hasNext()) {
                    String s = sc.nextLine();
                    str += s + "\n";
                    strFileArea += s + "\n";
                }

                strFileArea = strFileArea.replaceAll("\\s" , " ");
                String sFilter = filter(str);

                // Check if balanced
                String sEquationArea="";
                String postfix="";
                if (checkBalance(sFilter)){
                    informUser.setText("The File Is Balanced");
                    // for equations TextArea
                    if (str.contains("<equation>") && str.contains("</equation>")) {
                        while (str.contains("<equation>") && str.contains("</equation>")) {
                            int last = str.indexOf("</equation>");
                            str += str.substring(str.indexOf("<equation>") + "<equation>".length(), str.indexOf("</equation>")) + "\n";
                            sEquationArea+=str.substring(str.indexOf("<equation>") + "<equation>".length(), str.indexOf("</equation>")) + "\n";
                            str = str.substring(last + 1);
                        }

                        String[] splitting = sEquationArea.split("\n");
                        for(int k=0; k< splitting.length; k++) {
                            postfix += toPostfix(splitting[k]) + "\n";
                        }

                        String[] eval= new String[10];
                        String[] postEva = postfix.split("\n");
                        for(int k=0; k< postEva.length; k++) {
                            if(checkBalanceEquation(postEva[k])) {
                                if (evalPostfix(postEva[k]) != 0) {
                                    eval[k] = evalPostfix(postEva[k]) + "\n";
                                    equationsArea.appendText(splitting[k] + " ==> " + postEva[k] + " ==> " + eval[k]);
                                }
                                else {
                                    equationsArea.appendText(splitting[k] + " ==> " + postEva[k] + " ==> [Invalid equation]" + "\n");
                                }
                            }
                            else {
                                equationsArea.appendText(splitting[k] + " ==> " + postEva[k] + " ==> [unbalanced]" + "\n");
                            }
                        }

                    } else {
                        equationsArea.setText("");
                    }

                    equationsAreaStack.push(equationsArea.getText());


                    // for files TextArea
                    String sFileArea;
                    for (int i=0; i<strFileArea.length();i++) {
                        if (strFileArea.contains(" <file> ") && strFileArea.contains(" </file> ")) {
                        int last = strFileArea.indexOf(" </file> ");
                        strFileArea += strFileArea.substring(strFileArea.indexOf(" <file> ") + " <file> ".length(), strFileArea.indexOf(" </file> ")) + "\n";
                        sFileArea=strFileArea.substring(strFileArea.indexOf(" <file> ") + " <file> ".length(), strFileArea.indexOf(" </file> "));

                        filesArea.getItems().add(sFileArea);
                        strFileArea = strFileArea.substring(last + 1);

                    }
                }

                    String pressing = String.valueOf(filesArea.getItems());
                    pressing=pressing.replace("[", "");
                    pressing=pressing.replace("]", "");
                    String[] pressingArray = pressing.split(",");
                    String connectString="";
                    for(int i=0; i<pressingArray.length; i++) {
                        connectString+=pressingArray[i] + "\n";
                    }

                    filesAreaStack.push(connectString);
            }
                else {
                    informUser.setText("The File Is Not Balanced");
                    filesArea.getItems().clear();
                    equationsArea.setText("");
                    equationsAreaStack.push(equationsArea.getText());
                }

            } catch(Exception x) {
                informUser.setText("File Does Not Exist");
                textField.setText("");
                equationsArea.setText("");
                filesArea.getItems().clear();
            }

            textFieldStack.push(informUser.getText());
            back.setDisable(false);


        });


        filesArea.setOnMousePressed(e->{
            fileStack.push(filesArea.getSelectionModel().getSelectedItem());
            filesArea.getItems().clear();
            File selectedFile = new File(fileStack.peek());
            textField.setText(selectedFile.getPath());
            fileStack.push(selectedFile.getPath());


            String str = "";
            String strFileArea ="";
            String sFilter="";
            try{
                equationsArea.clear();
                Scanner sc = null;
                try {
                    sc = new Scanner(selectedFile);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
               while (sc.hasNext()) {
                 String s = sc.nextLine();
                    str += s + "\n";
                   strFileArea += s + "\n";
                }
               if( !(strFileArea.equals(""))) {
                   strFileArea = strFileArea.replaceAll("\\s", " ");
                   sFilter = filter(str);
               }

          // Check if balanced
                String sEquationArea="";
                String postfix="";
                if (checkBalance(sFilter)){
                    informUser.setText("The File Is Balanced");
                    // for equations TextArea
                if (str.contains("<equation>") && str.contains("</equation>")) {
                       while (str.contains("<equation>") && str.contains("</equation>")) {
                          int last = str.indexOf("</equation>");
                            str += str.substring(str.indexOf("<equation>") + "<equation>".length(), str.indexOf("</equation>")) + "\n";
                          sEquationArea+=str.substring(str.indexOf("<equation>") + "<equation>".length(), str.indexOf("</equation>")) + "\n";
                           str = str.substring(last + 1);
                       }

                        String[] splitting = sEquationArea.split("\n");
                        for(int k=0; k< splitting.length; k++) {
                            postfix += toPostfix(splitting[k]) + "\n";
                        }

                        String[] eval= new String[10];
                        String[] postEva = postfix.split("\n");
                        for(int k=0; k< postEva.length; k++) {
                            if(checkBalanceEquation(postEva[k])) {
                                if (evalPostfix(postEva[k]) != 0) {
                                    eval[k] = evalPostfix(postEva[k]) + "\n";
                                    equationsArea.appendText(splitting[k] + " ==> " + postEva[k] + " ==> " + eval[k]);
                                }
                                else {
                                    equationsArea.appendText(splitting[k] + " ==> " + postEva[k] + " ==> [Invalid equation]" + "\n");
                                }
                            }
                            else {
                                equationsArea.appendText(splitting[k] + " ==> " + postEva[k] + " ==> [unbalanced]" + "\n");
                            }
                        }

                    } else {
                        equationsArea.setText(" ");
                    }

                    equationsAreaStack.push(equationsArea.getText());

                    // for files TextArea
                    String sFileArea;
                    for (int i=0; i<strFileArea.length();i++) {
                        if(!strFileArea.equals("")) {
                            if (strFileArea.contains(" <file> ") && strFileArea.contains(" </file> ")) {
                                int last = strFileArea.indexOf(" </file> ");
                                strFileArea += strFileArea.substring(strFileArea.indexOf(" <file> ") + " <file> ".length(), strFileArea.indexOf(" </file> ")) + "\n";
                                sFileArea = strFileArea.substring(strFileArea.indexOf(" <file> ") + " <file> ".length(), strFileArea.indexOf(" </file> "));
                                filesArea.getItems().add(sFileArea);

                                filesAreaStack.push(String.valueOf(filesArea.getItems()));
                                strFileArea = strFileArea.substring(last + 1);
                            }
                        }
                    }
        }
                else {
                    informUser.setText("The File Is Not Balanced");
                    filesArea.getItems().clear();
                    equationsArea.setText("");
                    equationsAreaStack.push(equationsArea.getText());
                }

            } catch(Exception x) {
                informUser.setText("File Does Not Exist");
                textField.setText("");
                equationsArea.setText("");
                filesArea.getItems().clear();
            }

            textFieldStack.push(informUser.getText());
            back.setDisable(false);

            fileStack.pop();

        });

        back.setOnAction(e->{
            try {
                fileStack.pop();
                back.setDisable(fileStack.isEmpty());
                textField.setText(fileStack.peek());
                equationsAreaStack.pop();
                equationsArea.setText(equationsAreaStack.peek());
                filesAreaStack.pop();
                textFieldStack.pop();
                informUser.setText(textFieldStack.peek());

                // for the ListView
                if(fileStack.isEmpty()){
                    filesArea.getItems().clear();
                }
                else {
                filesArea.getItems().clear();
                File selectedFile = new File(fileStack.peek());
                textField.setText(selectedFile.getPath());
                String strFileArea = "";
                Scanner sc = null;
                try {
                    sc = new Scanner(selectedFile);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                while (sc.hasNext()) {
                    String s = sc.nextLine();
                    strFileArea += s + "\n";
                }
                if (!(strFileArea.equals(""))) {
                    strFileArea = strFileArea.replaceAll("\\s", " ");

                }

                String sFileArea;
                for (int i = 0; i < strFileArea.length(); i++) {
                    if (!strFileArea.equals("")) {
                        if (strFileArea.contains(" <file> ") && strFileArea.contains(" </file> ")) {
                            int last = strFileArea.indexOf(" </file> ");
                            strFileArea += strFileArea.substring(strFileArea.indexOf(" <file> ") + " <file> ".length(), strFileArea.indexOf(" </file> ")) + "\n";
                            sFileArea = strFileArea.substring(strFileArea.indexOf(" <file> ") + " <file> ".length(), strFileArea.indexOf(" </file> "));
                            filesArea.getItems().add(sFileArea);

                            filesAreaStack.push(String.valueOf(filesArea.getItems()));
                            strFileArea = strFileArea.substring(last + 1);
                        }
                    }
                }
            }
            } catch(NullPointerException ex){
                ex.printStackTrace();
            }

        });

        buttonsBox.getChildren().addAll(back, textField, load);
        buttonsBox.setAlignment(Pos.CENTER);

        sceneBox.getChildren().addAll(buttonsBox, equationBox);
        sceneBox.setBorder(new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));


        Scene scene = new Scene(sceneBox, 850, 650);
        stage.setTitle("Second Data Structure Project!");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }

     public static String filter(String s) {
        String str="";
         s = s.replaceAll("\\s" , " ");
         s = s.replaceAll("<equation>", " <equation> ");
         s = s.replaceAll("</equation>", " </equation> ");

        String[] sl=s.trim().split(" ");
        for(int i=0; i<sl.length;i++) {
            if (sl[i].equals("</242>") || sl[i].equals("<242>") ||
                    sl[i].equals("</equations>") || sl[i].equals("<equations>") ||
                    sl[i].equals("</equation>") || sl[i].equals("<equation>") ||
                    sl[i].equals("</file>") || sl[i].equals("<file>") ||
                    sl[i].equals("</files>") || sl[i].equals("<files>"))
                str += sl[i] + " ";
        }

        return str;

    }

    public static boolean checkBalance(String s) {
        CursorStack<String> stack = new CursorStack<>(30);
        String[] c=s.trim().split(" ");
        for (int i = 0; i < c.length; i++) {
            switch (c[i]) {
                case "<242>":
                case "<equations>":
                case "<files>":
                case "<equation>":
                case "<file>":

                    stack.push(c[i]);
                    break;

                case "</242>":
                case "</equations>":
                case "</files>":
                case "</equation>":
                case "</file>":
                    if (stack.isEmpty())
                        return false;
                    String o = stack.pop();
                    if (!((c[i].equals("</242>") && o.equals("<242>")) ||
                            (c[i].equals("</equations>") && o.equals("<equations>")) ||
                            (c[i].equals("</equation>") && o.equals("<equation>")) ||
                            (c[i].equals("</file>") && o.equals("<file>")) ||
                            (c[i].equals("</files>") && o.equals("<files>"))))
                        return false;
            }
        }
        return (stack.isEmpty()) ? true : false;
    }

    public static String toPostfix(String s) {
        CursorStack<String> stack = new CursorStack<>(30);
        String[] str = s.split(" ");
        String result = "";
        for (int i = 0; i < str.length; i++)
            switch (str[i]) {
                case "+":
                case "-":
                    while (!stack.isEmpty() && !stack.peek().equals("("))
                        result += " " + stack.pop();

                    stack.push(str[i]);
                    break;
                case "/":
                case "*":
                    while (!stack.isEmpty() && !precedenceOf(str[i], stack.peek()) && !stack.peek().equals("("))
                        result += " " + stack.pop();

                    stack.push(str[i]);
                    break;
                case "(":

                    stack.push(str[i]);
                    break;
                case ")":
                    while (!stack.isEmpty() && !stack.peek().equals("("))
                        result += " " + stack.pop();
                    stack.pop();
                    break;
                case "":
                    break;
                default:
                    result += " " + str[i];

            }
        while (!stack.isEmpty())
            result += " " + stack.pop();

        return result;
    }

    public static boolean precedenceOf(String op1, String op2) {
        if (op1.equals("+") || op1.equals("-"))
            return false;
        else if (op2.equals("+") || op2.equals("-"))
            return true;
        return false;
    }

    public  static int evalPostfix(String s) {
        CursorStack<Integer> stack = new CursorStack<>(10);
        String[] variable = s.trim().split(" ");
        int operators = 0, operands=0;
        for(int i=0; i < variable.length; i++) {

            if (!(variable[i].equals("+") || variable[i].equals("-") || variable[i].equals("*") || variable[i].equals("/"))) {
                stack.push(Integer.valueOf(variable[i]));
                operators++;
            }

            else {

                operands++;
                if (operands >= operators)
                    return 0;

                else {
                    int operandTwo = stack.pop();
                    int operandOne = stack.pop();

                    switch (variable[i]) {
                        case "+":
                            stack.push(operandTwo + operandOne);
                            break;

                        case "-":
                            stack.push(operandTwo - operandOne);
                            break;

                        case "*":
                            stack.push(operandTwo * operandOne);
                            break;

                        case "/":
                            stack.push(operandTwo / operandOne);
                            break;

                    }
                }
            }

        }
        if ((operands % 2 != 0 && operators % 2 != 0) || (operands % 2 == 0 && operators % 2 == 0))
            return 0;

        return stack.pop();
    }

    public static boolean checkBalanceEquation(String s) {
        CursorStack<Character> stack = new CursorStack<>(30);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c + "") {
                case "(":

                    stack.push(c);
                    break;

                case ")":
                    if (stack.isEmpty())
                        return false;
                    char o = stack.pop();
                    if (!(c == ')' && o == '('))
                        return false;
            }
        }
        return stack.isEmpty();
    }

}
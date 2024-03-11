/*
* Name: Dewan Fathima
* Class: CSIT Java 501
* Project: ProfBuilder.java
* Description: In this project I will be creating a profile builder that will take the users input like name, pic, bio, birthday, and more
* */
package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

public class ProfileBuilder extends Application {

    private File PicFile;
    private File BioFile;
    private String font;
    private DatePicker birthdayPicker;
    private TextField nameField;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;

        // Creating the layout for the page
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        // Creating the label and the button to upload the picture
        Label pictureLabel = new Label("Upload Picture:");
        Button uploadPicBT = new Button("Upload");
        uploadPicBT.setOnAction(this:: uploadPic);

        // This is creating the label for the name and the textfield
        Label nameLabel = new Label("Name:");
        nameField = new TextField();

        //Creating the label and creating the datepicker
        Label birthdayLabel = new Label("Birthday:");
        birthdayPicker = new DatePicker();

        //Creating the label for uploading the bio and creating the upload button with an event handler
        Label bioLabel = new Label("Upload Bio:");
        Button uploadBioBT = new Button("Upload");
        uploadBioBT.setOnAction(this::uploadBio);

        //Creating the font label, creating the drop down menu, adding different font to the menu, and able to select the font with the event handler
        Label fontLabel = new Label("Font:");
        ChoiceBox<String> fontChoiceBox = new ChoiceBox<>();
        fontChoiceBox.getItems().addAll("Arial", "Times New Roman", "Verdana");
        fontChoiceBox.setValue("Arial");
        fontChoiceBox.setOnAction(this:: selectedFont);

        //Creating the submit button and the label with an event handler
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(this :: handleSub);

        //Creating the toggle button to add a dark mode to the background and the label with an event handler to create the action
        ToggleButton darkMode = new ToggleButton("Dark Mode");
        darkMode.setOnAction(this :: togDarkMode);

        // Adding the components to the layout
        root.getChildren().addAll(
                pictureLabel, uploadPicBT,
                nameLabel, nameField,
                birthdayLabel, birthdayPicker,
                bioLabel, uploadBioBT,
                fontLabel, fontChoiceBox,
                submitButton, darkMode
        );


        // Creating the first initial page to create the profile
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Profile Builder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // this event handler is the submit button that will generate the profile page
    private void handleSub(ActionEvent actionEvent) {
        genPort(primaryStage);
    }
    // this event handler method will allow the user to upload a picture from the computer
    private void uploadPic(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Picture");
        PicFile = fileChooser.showOpenDialog(primaryStage);
    }
    // this event handler method will allow the user to upload the bio text file from the computer
    private void uploadBio(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Biography");
        BioFile = fileChooser.showOpenDialog(primaryStage);
    }

    // this event handler method will allow the user to change the background color to dark mode from white to gray
    private void togDarkMode(ActionEvent actionEvent) {
        ToggleButton darkMode = (ToggleButton) actionEvent.getSource();
        boolean darkModeSelected = darkMode.isSelected();
        VBox root = (VBox) darkMode.getParent(); // Assuming the root is a VBox
        if (darkModeSelected) {
            root.setBackground(new Background(new BackgroundFill(Color.DARKGREY, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    // this event handler method allow the user to select the font to the text
    private void selectedFont(ActionEvent actionEvent) {
        font = ((ChoiceBox<String>) actionEvent.getSource()).getValue();
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------
    //This is the profile generator method that will open a new page when the submit button is activated
    private void genPort(Stage primaryStage) {
        Stage portfolioStage = new Stage();

        //Creating the  layout for the new page
        VBox profileRoot = new VBox(10);
        profileRoot.setPadding(new Insets(10));
        profileRoot.setAlignment(Pos.CENTER);

        // this is the error message if any fields are empty
        if (PicFile == null || BioFile == null || birthdayPicker.getValue() == null) {
            showErrorAlert("Please fill in all fields.");
            return;
        }

        // These are the new instances for images, label, age, link, darkmode, zodicaSigns, and the startOver button
        ImageView imageView = createImageView();
        Label nameLabel = new Label("Name: " + nameField.getText());
        Label ageLabel = createAgeLabel(birthdayPicker.getValue());
        ImageView zodiacSigns = createZodiacSigns(birthdayPicker.getValue());
        Hyperlink websiteLink = createHyperlink();
        TextArea bioText = createBioTextArea();
        Button startOverBT = createStartOverBT(primaryStage);
        ToggleButton darkMode = createDarkModeBT(profileRoot);

        // Adding all the components to portfolioRoot
        profileRoot.getChildren().addAll(
                imageView, nameLabel, ageLabel,
                zodiacSigns, websiteLink, bioText,
                startOverBT, darkMode
        );

        // this will display all the components to the new generated profile page
        Scene portfolioScene = new Scene(profileRoot, 600, 400);
        portfolioStage.setTitle("Generated Portfolio");
        portfolioStage.setScene(portfolioScene);
        portfolioStage.show();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------
    // This method is creating the imageView to display the uploaded picture
    private ImageView createImageView() {
        ImageView imageView = new ImageView();
        try {
            imageView.setImage(new Image(new FileInputStream(PicFile)));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(200);
        } catch (FileNotFoundException e) {
            showErrorAlert("Error uploading picture: " + e.getMessage());
        }
        return imageView;
    }

    // this method will display the users age
    private Label createAgeLabel(LocalDate birthday) {
        int age = calculateAge(birthday);
        return new Label("Age: " + age);
    }

    // this method is going to display the zodiac signs by calling it from the Zodics folder
    private ImageView createZodiacSigns(LocalDate birthday) {
        String zodiacSign = getZodiacSign(birthday.getMonthValue(), birthday.getDayOfMonth());
        ImageView zodiacIcon = new ImageView(new Image("file:src/Zodiacs/" + zodiacSign + ".png"));
        zodiacIcon.setFitWidth(200); // Set the desired width
        zodiacIcon.setFitHeight(200); // Set the desired height
        return zodiacIcon;
    }

    // this method is creating the linkedIn hyperlink for the new page
    private Hyperlink createHyperlink() {
        Hyperlink websiteLink = new Hyperlink("My LinkedIn Profile");
        websiteLink.setOnAction(e -> {
            // Open the URL in a web browser
            String url = "https://www.linkedin.com/in/dewan-fathima/";
            getHostServices().showDocument(url);
        });
        return websiteLink;
    }

    // this method is displaying the text for the file that was uploaded for the bio
    private TextArea createBioTextArea() {
        TextArea bioText = new TextArea();
        bioText.setPrefWidth(100);
        bioText.setPrefHeight(200);
        try {
            Scanner scanner = new Scanner(BioFile);
            StringBuilder biographyText = new StringBuilder();
            while (scanner.hasNextLine()) {
                biographyText.append(scanner.nextLine()).append("\n");
            }
            bioText.setText(biographyText.toString());
            scanner.close();
            bioText.setFont(Font.font(font));
        } catch (FileNotFoundException e) {
            showErrorAlert("Error reading biography file: " + e.getMessage());
        }
        return bioText;
    }

    // this method is for the start over button letting the user to go back to the first builder page
    private Button createStartOverBT(Stage primaryStage) {
        Button startOverBT = new Button("Start Over");
        startOverBT.setOnAction(e -> {
            primaryStage.close();
            primaryStage.show();
        });
        return startOverBT;
    }

    // these two methods are created to put a dark mode to the second page and will switch from white to gray colored background
    private ToggleButton createDarkModeBT(VBox portfolioRoot) {
        ToggleButton darkMode = new ToggleButton("Dark Mode");
        darkMode.setOnAction(this::togDarkMode);
        return darkMode;
    }
    private void toggleDarkMode(VBox root, boolean darkModeSelected) {
        if (darkModeSelected) {
            root.setBackground(new Background(new BackgroundFill(Color.DARKGREY, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    // this method is calculating the age from the date that was selected from the datePicker
    private int calculateAge(LocalDate birthday) {
        LocalDate now = LocalDate.now();
        return Period.between(birthday, now).getYears();
    }

    // this is the exception method that will make the error message
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // this is the zodiac signs method to calculate the right sign based on the birth date that was chosen
    private String getZodiacSign(int month, int day) {
        if (month == 1) { // January
            return (day <= 20) ? "capricorn" : "aquarius";
        } else if (month == 2) { // February
            return (day <= 19) ? "aquarius" : "pisces";
        } else if (month == 3) { // March
            return (day <= 20) ? "pisces" : "aries";
        } else if (month == 4) { // April
            return (day <= 20) ? "aries" : "taurus";
        } else if (month == 5) { // May
            return (day <= 21) ? "taurus" : "gemini";
        } else if (month == 6) { // June
            return (day <= 21) ? "gemini" : "cancer";
        } else if (month == 7) { // July
            return (day <= 22) ? "cancer" : "leo";
        } else if (month == 8) { // August
            return (day <= 22) ? "leo" : "virgo";
        } else if (month == 9) { // September
            return (day <= 23) ? "virgo" : "libra";
        } else if (month == 10) { // October
            return (day <= 23) ? "libra" : "scorpio";
        } else if (month == 11) { // November
            return (day <= 22) ? "scorpio" : "sagittarius";
        } else if (month == 12) { // December
            return (day <= 21) ? "sagittarius" : "capricorn";
        } else {
            return "unknown";
        }
    }

}

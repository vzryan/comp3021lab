package comp3021.src.comp3021.base;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * 
 * NoteBook GUI with JAVAFX
 * 
 * COMP 3021
 * 
 * 
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 * 
	 * Combobox for selecting the folder
	 * 
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";
	String currentNote = "";

	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
	}

	/**
	 * This create the top section
	 * 
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Button buttonLoad = new Button("Load from File");
		buttonLoad.setPrefSize(100, 20);
		buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Object!");
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				Window stage = null;
				File file = fileChooser.showOpenDialog(stage);
				if (file != null){
					loadNoteBook(file);
				}
				
			}
		});
		Button buttonSave = new Button("Save to File");
		buttonSave.setPrefSize(100, 20);
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Object!");
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				Window stage = null;
				File file = fileChooser.showOpenDialog(stage);
				if (file != null){
					noteBook.save(file.getPath());
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Successfully saved");
					alert.setContentText("You file has been saved to file " + file.getName());
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}
			}
		});
		Label searchtext = new Label("Search:");
		searchtext.setPrefSize(70,20);
		TextField searcharg = new TextField();
		searcharg.setPrefSize(120,20);



		Button buttonsearch = new Button("Search");
		buttonsearch.setPrefSize(100, 20);
		buttonsearch.setOnMouseClicked(mouseEvent -> {
			ObservableList<Object> temp = FXCollections.observableArrayList( );
			for (Note x : noteBook.searchNotes(searcharg.getText()  )){
				temp.add(x.getTitle());
			}
			//System.out.println("temp"+temp);
			ObservableList result = FXCollections.observableArrayList( );
			for (Object x: temp){
				for (Folder y: noteBook.getFolders()){
					for (Note z:y.getNotes()){
						if (y.getName().equals(currentFolder)){
							if (z.getTitle().equals(x)){
								result.add(x);
							}
						}
					}
				}
			}
			//System.out.println("DLLM2"+result);
			titleslistView.setItems( result );
		});
		Button buttonclear = new Button("Clear Search");
		buttonclear.setPrefSize(100, 20);
		buttonclear.setOnMouseClicked(mouseEvent -> {
			searcharg.setText("");
			updateListView();
		});


		hbox.getChildren().addAll(buttonLoad, buttonSave,searchtext,searcharg,buttonsearch,buttonclear);

		return hbox;
	}

	/**
	 * this create the section on the left
	 * 
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes

		// TODO: This line is a fake folder list. We should display the folders in noteBook variable! Replace this with your implementation
		//foldersComboBox.getItems().addAll("FOLDER NAME 1", "FOLDER NAME 2", "FOLDER NAME 3");
		for (Folder x : noteBook.getFolders()){
			foldersComboBox.getItems().add(x.getName());
		}

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				currentFolder = t1.toString();
				// this contains the name of the folder selected
				// TODO update listview
				updateListView();

			}

		});

		foldersComboBox.setValue("-----");

		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				String title = t1.toString();
				// This is the selected title
				// TODO load the content of the selected note in
				// textAreNote
				String content = "";
				for (Folder x :noteBook.getFolders() ){
					for (Note y : x.getNotes()){
						if (y instanceof TextNote) {
							if (y.getTitle().equals(title)) {
								content = ((TextNote) y).getContent();
							}
						}
					}
				}
				textAreaNote.setText(content);
				textAreaNote.setEditable(true);
			}
		});
		HBox box2 = new HBox();
		Button buttonaddfolder = new Button("Add a Folder");
		buttonaddfolder.setPrefSize(100, 20);
		buttonaddfolder.setOnMouseClicked(mouseEvent -> {
			TextInputDialog dialog = new TextInputDialog("Add a Folder");
			dialog.setTitle("Input");
			dialog.setHeaderText("Add a new folder for your notebook:");
			dialog.setContentText("Please enter the name you want to create:");
			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.get().isEmpty() || result.get().toString().equals("")){
				// TODO
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Warning");
				alert.setContentText("Please input a valid folder name");
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
					}
				});
			}else if ( noteBook.getFolders().stream().map(Folder::getName).toList().contains(result.get().toString()) ){
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Warning");
				alert.setContentText("You already have a folder named"+result.get().toString());
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
					}
				});
			}else{
				noteBook.addFolder(result.get().toString());
			}
			foldersComboBox.getItems().clear();
			for (Folder x : noteBook.getFolders()){
				foldersComboBox.getItems().add(x.getName());
			}
			updateListView();
		});
		Button buttonaddnote = new Button("Add a Note");
		buttonaddnote.setPrefSize(100, 20);
		buttonaddnote.setOnMouseClicked(mouseEvent -> {
			if (  foldersComboBox.getSelectionModel().getSelectedItem() ==null||  foldersComboBox.getSelectionModel().getSelectedItem().toString().equals("")){
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Warning");
				alert.setContentText("Please choose a folder first");
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
					}
				});
			}else{
				TextInputDialog dialog = new TextInputDialog("Add a Note");
				dialog.setTitle("Input");
				dialog.setHeaderText("Add a new note to current folder:");
				dialog.setContentText("Please enter the name you want to create:");
				// Traditional way to get the response value.
				Optional<String> result = dialog.showAndWait();
				if (!noteBook.getFolders().stream().map(x->x.getNotes()).toList().contains(result.get())) {
					noteBook.createTextNote(currentFolder, result.get().toString());
				}
				else{

				}
				if (result.get().isEmpty() || result.get().toString().equals("")) {
					// TODO
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Successful");
					alert.setContentText("Insert note"+result.get().toString()+" to folder "+currentFolder+" successfully!");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}
				foldersComboBox.getItems().clear();
				for (Folder x : noteBook.getFolders()) {
					foldersComboBox.getItems().add(x.getName());
				}
				updateListView();
			}
		});
		box2.getChildren().add(foldersComboBox);
		box2.getChildren().add(buttonaddfolder);
		vbox.getChildren().add(new Label("Choose folder: "));
		vbox.getChildren().add(box2);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
		vbox.getChildren().add(buttonaddnote);

		return vbox;
	}

	private void updateListView() {
		ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the
		for (Folder x :noteBook.getFolders() ){
			for (Note y : x.getNotes()){
				if (y instanceof TextNote){
					if (x.getName().equals(currentFolder)){
						list.add(y.getTitle());
					}
				}
			}
		}
		//System.out.println(list);
		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		textAreaNote.setEditable(false);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
		saveView.setFitHeight(18);
		saveView.setFitWidth(18);
		saveView.setPreserveRatio(true);
		ImageView deleteView = new ImageView(new Image(new File("delete.png").toURI().toString()));
		deleteView.setFitHeight(18);
		deleteView.setFitWidth(18);
		deleteView.setPreserveRatio(true);
		Button buttonsavenote = new Button("Save Note");
		buttonsavenote.setPrefSize(100, 20);
		buttonsavenote.setOnMouseClicked(mouseEvent -> {
			if (foldersComboBox.getSelectionModel().getSelectedItem()==null ||titleslistView.getSelectionModel().getSelectedItem()==null){
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Warning");
				alert.setContentText("Please input a folder and a note");
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
					}
				});
			}else{
				for (Folder x : noteBook.getFolders()){
					if (x.getNotes().stream().map(Note::getTitle).toList().contains(titleslistView.getSelectionModel().getSelectedItem().toString())){
						for (Note y : x.getNotes()){
							if (y.getTitle().equals(titleslistView.getSelectionModel().getSelectedItem().toString())){
								x.getNotes().remove(y);
								x.getNotes().add(new TextNote(titleslistView.getSelectionModel().getSelectedItem(),textAreaNote.getText()));
							}
						}
					}
				}
			}
			updateListView();
		});
		Button buttondeletenote = new Button("Delete Note");
		buttondeletenote.setPrefSize(100, 20);
		buttondeletenote.setOnMouseClicked(mouseEvent -> {
			if (foldersComboBox.getSelectionModel().getSelectedItem().isBlank()||titleslistView.getSelectionModel().getSelectedItem().isBlank()){
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Warning");
				alert.setContentText("Please input a folder and a note");
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
					}
				});
			}else{
				for (Folder x : noteBook.getFolders()){
					if (x.getNotes().stream().map(Note::getTitle).toList().contains(titleslistView.getSelectionModel().getSelectedItem().toString())){
						x.getNotes().removeIf(y -> y.getTitle().equals(titleslistView.getSelectionModel().getSelectedItem().toString()));
					}
				}
			}
			updateListView();
		});
		HBox h1 = new HBox();
		h1.getChildren().add(saveView);
		h1.getChildren().add(buttonsavenote);
		h1.getChildren().add(deleteView);
		h1.getChildren().add(buttondeletenote);
		VBox v1 = new VBox();
		v1.getChildren().add(h1);
		v1.getChildren().add(textAreaNote);
		// 0 0 is the position in the grid
		grid.add(v1, 0, 0);

		return grid;
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called “the most shocking play in NFL history” and the Washington Redskins dubbed the “Throwback Special”: the November 1985 play in which the Redskins’ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Award–winning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything—until it wasn’t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant—a part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether’s Daddy Was a Number Runner and Dorothy Allison’s Bastard Out of Carolina, Jacqueline Woodson’s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood—the promise and peril of growing up—and exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;

	}
	private void loadNoteBook(File file){
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try{
			fis = new FileInputStream(file);
			in = new ObjectInputStream(fis);
			NoteBook object = (NoteBook) in.readObject();
			System.out.println(object.getFolders());
			noteBook = object;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		foldersComboBox.getItems().clear();
		for (Folder x : noteBook.getFolders()){
			foldersComboBox.getItems().add(x.getName());
		}
		updateListView();
	}

}

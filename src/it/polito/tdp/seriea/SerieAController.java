/**
 * Sample Skeleton for 'SerieA.fxml' Controller Class
 */

package it.polito.tdp.seriea;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class SerieAController {
	
	Model model;
	Season stagioneScelta = null;
	

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxSeason"
    private ChoiceBox<Season> boxSeason; // Value injected by FXMLLoader

    @FXML // fx:id="boxTeam"
    private ChoiceBox<String> boxTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void handleCarica(ActionEvent event) {
    	
    	stagioneScelta = boxSeason.getValue();
    	if(stagioneScelta == null) {
    		txtResult.setText("Seleziona una stagione");
    		return;
    	}
    	
    	model.creaGrafo(stagioneScelta);
    	
    	
    	for(Team t : model.getSquadreAnno()) {
    		boxTeam.getItems().add(t.getTeam());
    	}
    	Collections.sort(boxTeam.getItems());
    	
    	txtResult.clear();
    	List<Team> classifica = model.getClassifica();
    	for(Team t: classifica) {
    		txtResult.appendText(t.toString()+"\n");
    	}
    }

    @FXML
    void handleDomino(ActionEvent event) {
    	List<Team> domino = model.calcolaDomino();
    	txtResult.clear();
    	txtResult.appendText("Miglior DOMINO calcolato: lunghezza "+domino.size()+"\n");
    	
    	for(Team t : domino) {
    		txtResult.appendText(t.getTeam()+"\n");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxSeason != null : "fx:id=\"boxSeason\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert boxTeam != null : "fx:id=\"boxTeam\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";
    }

	public void setModel(Model model) {
		this.model = model;
		boxSeason.getItems().addAll(model.getAllSeason());
	}
}

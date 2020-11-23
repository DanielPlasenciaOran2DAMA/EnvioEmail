package dad.javafx.email;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class EmailController implements Initializable {

	@FXML
	private GridPane view;

	@FXML
	private TextField servidorText;

	@FXML
	private TextField puertoText;

	@FXML
	private CheckBox conexionCheck;

	@FXML
	private TextField remitenteText;

	@FXML
	private PasswordField contraseñaText;

	@FXML
	private TextField destinatarioText;

	@FXML
	private TextField asuntoText;

	@FXML
	private TextArea mensajeTextArea;

	private EmailModel model = new EmailModel();

	public EmailController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EmailView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		servidorText.textProperty().bindBidirectional(model.servidorProperty());
		puertoText.textProperty().bindBidirectional(model.puertoProperty(), new NumberStringConverter());
		conexionCheck.selectedProperty().bindBidirectional(model.conexionProperty());
		remitenteText.textProperty().bindBidirectional(model.remitenteProperty());
		contraseñaText.textProperty().bindBidirectional(model.contraseñaProperty());
		destinatarioText.textProperty().bindBidirectional(model.destinatarioProperty());
		asuntoText.textProperty().bindBidirectional(model.asuntoProperty());
		mensajeTextArea.textProperty().bindBidirectional(model.mensajeProperty());
	}

	@FXML
	void onCerrarButtonAction(ActionEvent event) {
		App.getPrimaryStage().close();
	}

	@FXML
	void onEnviarButtonAction(ActionEvent event) {
		try {
			Email email = new SimpleEmail();
			email.setHostName(model.getServidor());
			email.setSmtpPort(model.getPuerto());
			email.setAuthenticator(new DefaultAuthenticator(model.getRemitente(), model.getContraseña()));
			email.setSSLOnConnect(model.isConexion());
			email.setFrom(model.getRemitente());
			email.setSubject(model.getAsunto());
			email.setMsg(model.getMensaje());
			email.addTo(model.getDestinatario());
			email.send();

			Alert infoAlert = new Alert(AlertType.INFORMATION);
			infoAlert.setTitle("Mensaje enviado");
			infoAlert.setHeaderText("Mensaje enviado con,exito a '" + model.getDestinatario() + "'.");

			Stage stage = (Stage) infoAlert.getDialogPane().getScene().getWindow();
			stage.getIcons().setAll(App.getPrimaryStage().getIcons());

			infoAlert.showAndWait();
		} catch (EmailException e) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setTitle("Error");
			errorAlert.setHeaderText("No se pudo enviar el email.");
			errorAlert.setContentText("Invalid message supplied");

			Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
			stage.getIcons().setAll(App.getPrimaryStage().getIcons());

			errorAlert.showAndWait();
		}
	}

	@FXML
	void onVaciarButtonAction(ActionEvent event) {
		model.setServidor("");
		model.setPuerto(0);
		model.setConexion(false);
		model.setRemitente("");
		model.setContraseña("");
		model.setDestinatario("");
		model.setAsunto("");
		model.setMensaje("");
	}

	public GridPane getView() {
		return this.view;
	}
}

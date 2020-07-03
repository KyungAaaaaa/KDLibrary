package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Member;

public class RootController implements Initializable {
	@FXML
	private Button btnLogin;
	@FXML
	private Button btnExit;
	@FXML
	private Label lbSign;
	@FXML
	private Button btnAdminLogin;
	@FXML
	private Label lblFindPass;
	@FXML
	private TextField txtId;
	@FXML
	private PasswordField txtPass;
	public Stage stage;
	private String see = "86182";;
	private Image img = new Image(getClass().getResource("/image/보안문자.png").toString());
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 로그인
		btnLogin.setOnAction(event -> handleBtnLoginAction(event));

		// 회원가입
		lbSign.setOnMouseClicked(event -> handleBtnSignAction(event));

		// 비밀번호 찾기
		lblFindPass.setOnMouseClicked(event -> handleLblFindPassAction(event));

		// 관리자 로그인
		btnAdminLogin.setOnAction(event -> handleBtnAdminLoginAction(event));

		// 종료버튼
		btnExit.setOnAction(event -> Platform.exit());

	}

	
	private TextFormatter PhoneNumberTextFieldFormatSetting() {
		DecimalFormat decimalFormat = new DecimalFormat("###########");
		TextFormatter textFormatter = new TextFormatter<>(e -> {
			if (e.getControlNewText().isEmpty())
				return e;
			ParsePosition parsePosition = new ParsePosition(0);
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("입력 에러");
				alert.setHeaderText("숫자 외 문자는 입력되지 않습니다.");
				alert.setContentText("올바른 휴대전화 번호를 입력하세요.");
				alert.showAndWait();
				return null;
			}
			if (object == null || e.getControlNewText().length() > 12 )
				return null;
			else
				return e;
		});
		return textFormatter;
	}
	
	// 회원가입
	private void handleBtnSignAction(MouseEvent event) {

		try {
			
			int randomPass = 0;
			Parent root = FXMLLoader.load(getClass().getResource("/view/sign.fxml"));
			Scene scene = new Scene(root);
			Stage signStage = new Stage();
			signStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			TextField txtFieldSignName = (TextField) scene.lookup("#txtName");
			TextField txtFieldSignId = (TextField) scene.lookup("#txtId");
			TextField txtFieldSignPass = (TextField) scene.lookup("#txtPass");
			TextField txtFieldSignPass2 = (TextField) scene.lookup("#txtPass2");
			TextField txtFieldSignPhoneNumber = (TextField) scene.lookup("#txtPhoneNumber");
			TextField txtFieldSignSee = (TextField) scene.lookup("#txtSee");
			ComboBox<String> cmbYear = (ComboBox) root.lookup("#cmbYear");
			ComboBox<String> cmbMonth = (ComboBox) root.lookup("#cmbMonth");
			ComboBox<String> cmbDay = (ComboBox) root.lookup("#cmbDay");
			Button btnSignOk = (Button) scene.lookup("#btnOk");
			Button btnSignNo = (Button) scene.lookup("#btnNo");
			ImageView imgSignView = (ImageView) scene.lookup("#imgView");
			txtFieldSignPhoneNumber.setTextFormatter(PhoneNumberTextFieldFormatSetting());
			imgSignView.setImage(img);
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			randomPass = (int) ((Math.random()) * (5 - 0 + 1) - 0);
			switch (randomPass) {
			case 0:
				img = new Image(getClass().getResource("/image/보안문자.png").toString());
				see = "86182";
				break;
			case 1:
				img = new Image(getClass().getResource("/image/보안문자2.png").toString());
				see = "251531";
				break;
			case 2:
				img = new Image(getClass().getResource("/image/보안문자3.png").toString());
				see = "602125";
				break;
			case 3:
				img = new Image(getClass().getResource("/image/보안문자4.png").toString());
				see = "42626";
				break;
			case 4:
				img = new Image(getClass().getResource("/image/보안문자5.png").toString());
				see = "03358";
				break;
			}

			cmbYear.setItems(FXCollections.observableArrayList("1940", "1941", "1942", "1943", "1944", "1945", "1946",
					"1947", "1948", "1949", "1950", "1951", "1952", "1953", "1954", "1955", "1956", "1957", "1958",
					"1959", "1960", "1961", "1962", "1963", "1964", "1965", "1966", "1967", "1968", "1969", "1970",
					"1971", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982",
					"1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994",
					"1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006",
					"2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018",
					"2019", "2020"));
			cmbMonth.setItems(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08", "09",
					"10", "11", "12"));
			cmbDay.setItems(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08", "09",
					"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
					"26", "27", "28", "29", "30", "31"));
			imgSignView.setImage(img);

			btnSignOk.setOnAction(event1 -> {
				Connection con = null;
				PreparedStatement pstmt = null;
				if (txtFieldSignSee.getText().equals(see)) {
					if (!(txtFieldSignName.getText().equals("") ||cmbYear.getValue()==null||cmbMonth.getValue()==null||cmbDay.getValue()==null|| txtFieldSignId.getText().trim().equals("")
							|| txtFieldSignPhoneNumber.getText().trim().equals(""))) {
						if (txtFieldSignPass.getText().equals(txtFieldSignPass2.getText())) {
							try {
								con = DBUtil.getConnection();

								String query = "Insert into memberTBL(name,Id,Pass,phoneNumber,birth,etc) values(?, ?, ?, ?, ?,?)";
								pstmt = con.prepareStatement(query);
								Member m = new Member(txtFieldSignName.getText(), txtFieldSignId.getText(),
										txtFieldSignPass.getText(), txtFieldSignPhoneNumber.getText(),
										cmbYear.getValue() + "-" + cmbMonth.getValue() + "-" + cmbDay.getValue());
								pstmt.setString(1, m.getName());
								pstmt.setString(2, m.getId());
								pstmt.setString(3, m.getPass());
								pstmt.setString(4, m.getPhoneNumber());
								pstmt.setString(5, m.getBirth());
								pstmt.setString(6, "정상");

								try {
									int signValue = pstmt.executeUpdate();
									if (signValue != 0) {
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("KDLibrary");
										alert.setHeaderText(m.getName() + "님 회원가입 완료");
										alert.setContentText("환영합니다!");
										alert.showAndWait();
										signStage.close();
									} else {
										Alert alert = new Alert(AlertType.ERROR);
										alert.setTitle("에러발생");
										alert.setHeaderText("관리자에게 문의하세요");
										alert.showAndWait();
									}
								} catch (Exception e) {
									Alert alert = new Alert(AlertType.ERROR);
									alert.setTitle("아이디 중복");
									alert.setHeaderText("이미 존재하는 아이디입니다.");
									alert.setContentText("다른 아이디를 입력하세요.");
									alert.showAndWait();
								}
							} catch (Exception e) {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("에러발생");
								alert.setHeaderText("DB를 확인해주세요");
								alert.showAndWait();
							}
						} else {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("에러발생");
							alert.setHeaderText("비밀번호가 일치하지 않습니다");
							alert.showAndWait();
						}
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("에러발생");
						alert.setHeaderText("회원정보를 모두 입력해주시기 바랍니다");
						alert.showAndWait();
					}
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("에러발생");
					alert.setHeaderText("보안문구가 일치하지 않습니다");
					alert.showAndWait();
				}

			});
			signStage.initModality(Modality.WINDOW_MODAL);
			signStage.initOwner(stage);
			signStage.setScene(scene);
			signStage.setResizable(false);
			signStage.setTitle("회원가입");
			signStage.show();

			btnSignNo.setOnAction(event1 -> signStage.close());
		} catch (Exception e) {

		}

	}

	// 비밀번호 찾기
	private void handleLblFindPassAction(MouseEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/passFind.fxml"));
			Scene scene = new Scene(root);
			Stage passStage = new Stage();
			passStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			TextField txtId = (TextField) scene.lookup("#txtId");
			TextField txtPhoneNumber = (TextField) scene.lookup("#txtPhoneNumber");
			Button btnOk = (Button) scene.lookup("#btnOk");
			Button btnNo = (Button) scene.lookup("#btnNo");

			Parent root1 = FXMLLoader.load(getClass().getResource("/view/passReset.fxml"));
			Scene scenePassReset = new Scene(root1);

			PasswordField txtpwResetPass = (PasswordField) scenePassReset.lookup("#txtResetPass");
			PasswordField txtpwResetPass2 = (PasswordField) scenePassReset.lookup("#txtResetPass2");
			Button btnOk1 = (Button) scenePassReset.lookup("#btnOk");
			Button btnNo1 = (Button) scenePassReset.lookup("#btnNo");

			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			passStage.initModality(Modality.WINDOW_MODAL);
			passStage.initOwner(stage);
			passStage.setScene(scene);
			passStage.setResizable(false);
			passStage.setTitle("비밀번호 찾기");
			passStage.show();

			btnNo.setOnAction(event1 -> passStage.close());

			btnOk.setOnAction(event1 -> {
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Member mem = null;
				try {
					if (txtId.getText().trim().equals("") || txtPhoneNumber.getText().trim().equals(""))
						throw new Exception();

					con = DBUtil.getConnection();
					try {
						String query = "select * from memberTBL where Id = ? and phoneNumber = ?";
						pstmt = con.prepareStatement(query);
						pstmt.setString(1, txtId.getText().trim());
						pstmt.setString(2, txtPhoneNumber.getText().trim());

						rs = pstmt.executeQuery();
						while (rs.next()) {
							mem = new Member(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
									rs.getString(5), rs.getString(6), rs.getString(7));
						}
						if (mem == null)
							throw new Exception();
						scenePassReset.getStylesheets().add(getClass().getResource("/application/main.css").toString());
						passStage.setScene(scenePassReset);
						passStage.setResizable(false);
						passStage.setTitle("새 비밀번호");
						passStage.show();
						btnNo1.setOnAction(event3 -> passStage.close());
						btnOk1.setOnAction(event2 -> {
							PreparedStatement pstmt1 = null;
							Connection con1 = null;
							try {
								if (txtpwResetPass.getText().trim().equals("")
										|| txtpwResetPass2.getText().trim().equals("")) {
									Alert alert = new Alert(AlertType.ERROR);
									alert.setTitle("에러발생");
									alert.setHeaderText("모든 항목을 입력하세요.");
									alert.showAndWait();
									return;
								}
								if (txtpwResetPass.getText().trim().equals(txtpwResetPass2.getText().trim())) {
									con1 = DBUtil.getConnection();
									String query1 = "Update memberTBL set pass = ? where Id = ?";
									pstmt1 = con1.prepareStatement(query1);
									pstmt1.setString(1, txtpwResetPass.getText().trim());
									pstmt1.setString(2, txtId.getText().trim());

									int newPassGet = pstmt1.executeUpdate();
									if (newPassGet != 0) {
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("비밀번호 변경");
										alert.setHeaderText("비밀번호가 변경되었습니다.");
										alert.showAndWait();
										passStage.close();
									} else {
										throw new Exception("문제발생");
									}
								} else {
									throw new Exception();
								}
							} catch (Exception e) {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("에러발생");
								alert.setHeaderText("비밀번호가 서로 일치하지 않습니다");
								alert.showAndWait();
							}
						});
					} catch (Exception e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("계정 오류");
						alert.setHeaderText("존재하지 않는 계정입니다.");
						alert.showAndWait();
					}

				} catch (Exception e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("에러발생");
					alert.setHeaderText("모든 항목을 입력하세요.");
					alert.showAndWait();
					return;
				}

			});

		} catch (Exception e) {

		}
	}

	// 관리자 로그인
	private void handleBtnAdminLoginAction(ActionEvent event) {
		try {
			String adminChord = "12230313";
		
			Parent root = FXMLLoader.load(getClass().getResource("/view/admin_Login.fxml"));
			Scene scene = new Scene(root);
			Stage adminStage = new Stage();
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			adminStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			PasswordField txtAdminCode = (PasswordField) scene.lookup("#txtCode");
			Button btnAdminOk = (Button) scene.lookup("#btnOk");
			Button btnAdminNo = (Button) scene.lookup("#btnNo");

			adminStage.initModality(Modality.WINDOW_MODAL);
			adminStage.initOwner(stage);
			adminStage.setScene(scene);
			adminStage.setResizable(false);
			adminStage.setTitle("관리자 로그인");
			adminStage.show();

			btnAdminNo.setOnAction(event1 -> adminStage.close());
			btnAdminOk.setOnAction(event1 -> {
				try {
					if(txtAdminCode.getText().trim().equals("")) throw new Exception();
					if(txtAdminCode.getText().equals(adminChord)) {
					Stage adminMain = new Stage();
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/admin_Main.fxml"));
					Parent root1 = fxmlLoader.load();
					AdminController adminController= fxmlLoader.getController();
					adminController.stage = adminMain;
					adminMain.initOwner(stage);
					
					Scene s = new Scene(root1);
					s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
					adminMain.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
					adminMain.setTitle("KD Library-Admin");
					adminMain.setScene(s);
					adminMain.setResizable(true);
					((Stage) btnLogin.getScene().getWindow()).close();
					adminStage.close();
					adminMain.show();
					}else{
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("에러발생");
						alert.setHeaderText("관리자코드를 점검하세요.");
						alert.showAndWait();
					}
				} catch (Exception e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("에러발생");
					alert.setHeaderText("관리자코드를 입력하세요.");
					alert.showAndWait();
					return;
				}
			
			});

		} catch (Exception e) {
		}
	}

	// 로그인
	public void handleBtnLoginAction(ActionEvent event) {
		try {
			if (txtId.getText().trim().equals("") || txtPass.getText().trim().equals(""))
				throw new Exception();
			try {
				
				
				MemberDAO dao = new MemberDAO();
				
				Member memb = dao.loginUser(txtId.getText().trim(),txtPass.getText().trim());
				if (memb == null)
					throw new Exception();
				dao.m = memb;
				
				
				
				Stage user_MainStage = new Stage();
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/user_Main.fxml"));
				Parent root = fxmlLoader.load();
				User_MainController user_MainController = fxmlLoader.getController();
				user_MainController.userStage = user_MainStage;
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
				user_MainStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
				user_MainStage.setTitle("KD Library");
				user_MainStage.setScene(scene);
				user_MainStage.initOwner(this.stage);
				user_MainStage.setResizable(false);
				((Stage) btnLogin.getScene().getWindow()).close();
				user_MainStage.show();
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("에러발생");
				alert.setHeaderText("존재하지 않는 계정입니다");
				alert.showAndWait();
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러발생");
			alert.setHeaderText("아이디와 패스워드를 입력해주세요");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}


	
}

package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
	private ObservableList<Member> obsList;
	private String see;
	private Image img;

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

	// 회원가입
	private void handleBtnSignAction(MouseEvent event) {

			try {
				int randomPass = 0;
				Parent root = FXMLLoader.load(getClass().getResource("/view/sign.fxml"));
				Scene scene = new Scene(root);
				Stage signStage = new Stage(StageStyle.UTILITY);

				TextField txtFieldSignName = (TextField) scene.lookup("#txtName");
				TextField txtFieldSignId = (TextField) scene.lookup("#txtId");
				TextField txtFieldSignPass = (TextField) scene.lookup("#txtPass");
				TextField txtFieldSignPass2 = (TextField) scene.lookup("#txtPass2");
				DatePicker datePickerSign = (DatePicker) scene.lookup("#datePicker");
				TextField txtFieldSignPhoneNumber = (TextField) scene.lookup("#txtPhoneNumber");
				TextField txtFieldSignSee = (TextField) scene.lookup("#txtSee");
				ComboBox<String> cmbYear=(ComboBox) root.lookup("#cmbYear");
				ComboBox<String> cmbMonth=(ComboBox) root.lookup("#cmbMonth");
				ComboBox<String> cmbDay=(ComboBox) root.lookup("#cmbDay");
				Button btnSignOk = (Button) scene.lookup("#btnOk");
				Button btnSignNo = (Button) scene.lookup("#btnNo");
				ImageView imgSignView = (ImageView) scene.lookup("#imgView");
				scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
				
				cmbYear.setItems(FXCollections.observableArrayList("1940",	"1941",	"1942",	"1943",	"1944",	"1945",	"1946",	"1947",	"1948",	"1949",	
						"1950",	"1951",	"1952",	"1953",	"1954",	"1955",	"1956",	"1957",	"1958",	"1959",	
						"1960",	"1961",	"1962",	"1963",	"1964",	"1965",	"1966",	"1967",	"1968",	"1969",	
						"1970",	"1971",	"1972",	"1973",	"1974",	"1975",	"1976",	"1977",	"1978",	"1979",	
						"1980",	"1981",	"1982",	"1983",	"1984",	"1985",	"1986",	"1987",	"1988",	"1989",	
						"1990",	"1991",	"1992",	"1993",	"1994",	"1995",	"1996",	"1997",	"1998",	"1999",	
						"2000",	"2001",	"2002",	"2003",	"2004",	"2005",	"2006",	"2007",	"2008",	"2009",	
						"2010",	"2011",	"2012",	"2013",	"2014",	"2015",	"2016",	"2017",	"2018",	"2019",	"2020"));
				cmbMonth.setItems(FXCollections.observableArrayList("01",	"02",	"03",	"04",	"05",	"06",	"07",	"08",	"09",	"10",	"11",	"12"));
				cmbDay.setItems(FXCollections.observableArrayList("01",	"02",	"03",	"04",	"05",	"06",	"07",	"08",	"09",	"10",	"11",	"12",	"13",	"14",	"15",	"16",	"17",	"18",	"19",	"20",	"21",	"22",	"23",	"24",	"25",	"26",	"27",	"28",	"29",	"30",	"31"));
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
				imgSignView.setImage(img);
		
				btnSignOk.setOnAction(event1 -> {
					Connection con = null;
					PreparedStatement pstmt = null;
					if (txtFieldSignSee.getText().equals(see)) {
						if (!(txtFieldSignName.getText().equals("") || txtFieldSignId.getText().trim().equals("")||txtFieldSignPhoneNumber.getText().trim().equals(""))) {
					if (txtFieldSignPass.getText().equals(txtFieldSignPass2.getText())) {
							try {
								con = DBUtil.getConnection();

								String query = "Insert into memberTBL(name,Id,Pass,phoneNumber,birth,etc) values(?, ?, ?, ?, ?,?)";
								pstmt = con.prepareStatement(query);
								/*
								 * Member m = new Member(txtFieldSignName.getText(), txtFieldSignId.getText(),
								 * txtFieldSignPass.getText(), txtFieldSignPhoneNumber.getText(),
								 * datePickerSign.getValue().toString());
								 */
								Member m = new Member(txtFieldSignName.getText(), txtFieldSignId.getText(),
										txtFieldSignPass.getText(), txtFieldSignPhoneNumber.getText(),
										cmbYear.getValue()+"-"+cmbMonth.getValue()+"-"+cmbDay.getValue());
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
										alert.setTitle("삽입관련");
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
									alert.setTitle("에러발생");
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
						}else {
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
			Stage passStage = new Stage(StageStyle.UTILITY);

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

						passStage.setScene(scenePassReset);
						passStage.setResizable(false);
						passStage.setTitle("새 비밀번호");
						passStage.show();
						btnNo1.setOnAction(event3 -> passStage.close());
						btnOk1.setOnAction(event2 -> {
							PreparedStatement pstmt1 = null;
							Connection con1 = null;
							try {
								if (txtpwResetPass.getText().trim().equals(txtpwResetPass2.getText().trim())) {
									con1 = DBUtil.getConnection();
									String query1 = "Update memberTBL set pass = ? where Id = ?";
									pstmt1 = con1.prepareStatement(query1);
									pstmt1.setString(1, txtpwResetPass.getText().trim());
									pstmt1.setString(2, txtId.getText().trim());

									int newPassGet = pstmt1.executeUpdate();
									if (newPassGet != 0) {
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("삽입관련");
										alert.setContentText("삽입을 성공적으로 진행하였습니다");
										alert.showAndWait();
										passStage.close();
									} else {
										throw new Exception("문제발생");
									}
								} else {
									Alert alert = new Alert(AlertType.ERROR);
									alert.setTitle("에러발생");
									alert.setHeaderText("비밀번호가 서로 일치하지 않습니다");
									alert.showAndWait();
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
						alert.setTitle("에러발생");
						alert.setHeaderText("존재하지 않는 계정입니다");
						alert.showAndWait();
					}

				} catch (Exception e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("에러발생");
					alert.setHeaderText("입력창을 점검하세요");
					alert.showAndWait();
				}

			});

		} catch (Exception e) {

		}
	}

	// 관리자 로그인
	private void handleBtnAdminLoginAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/adminLogin.fxml"));
			Scene scene = new Scene(root);
			Stage adminStage = new Stage(StageStyle.UTILITY);

			TextField txtAdminCode = (TextField) scene.lookup("#txtCode");
			Button btnAdminOk = (Button) scene.lookup("#btnOk");
			Button btnAdminNo = (Button) scene.lookup("#btnNo");

			adminStage.initModality(Modality.WINDOW_MODAL);
			adminStage.initOwner(stage);
			//adminStage.initStyle(StageStyle.UNDECORATED); 제목표시줄 삭제
			adminStage.setScene(scene);
			adminStage.setResizable(false);
			adminStage.setTitle("관리자 로그인");
			adminStage.show();

			btnAdminNo.setOnAction(event1 -> adminStage.close());
			btnAdminOk.setOnAction(event1 -> {
				Stage adminMain = null;
				try {
					Parent root1 = FXMLLoader.load(getClass().getResource("/view/adminMain.fxml"));
					Scene s = new Scene(root1);
					adminMain = new Stage();
					adminMain.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
					adminMain.setTitle("KD Library-Admin");
					adminMain.setScene(s);
					adminMain.setResizable(true);
					((Stage) btnLogin.getScene().getWindow()).close();
					adminStage.close();
					adminMain.show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});

		} catch (Exception e) {
		}
	}

	// 로그인
	public void handleBtnLoginAction(ActionEvent event) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Member memb = null;
		try {
			if (txtId.getText().trim().equals("") || txtPass.getText().trim().equals(""))
				throw new Exception();
			try {
				Stage user_MainStage = new Stage(StageStyle.UTILITY);
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/user_Main.fxml"));
				Parent root = fxmlLoader.load();
				User_MainController user_MainController = fxmlLoader.getController();
				user_MainController.stage = user_MainStage;
				con = DBUtil.getConnection();
				String query = "select * from memberTBL where Id = ? and pass = ?";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, txtId.getText().trim());
				pstmt.setString(2, txtPass.getText().trim());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					memb = new Member(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getString(7));
				}

				if (memb == null)
					throw new Exception();
				MemberDAO dao = new MemberDAO();
				Scene scene = new Scene(root);
				//scene.getStylesheets().add(getClass().getResource("/css/user_Main.css").toString());
				user_MainStage = new Stage();
				user_MainStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
				user_MainStage.setTitle("KD Library");
				user_MainStage.setScene(scene);
				user_MainStage.setResizable(false);
				((Stage) btnLogin.getScene().getWindow()).close();
				user_MainStage.show();
				dao.m = dao.loginUser(txtId.getText().trim());
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

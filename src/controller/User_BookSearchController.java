package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Book;

public class User_BookSearchController implements Initializable {
	@FXML
	private HBox bookBox;
	@FXML
	private ImageView imgV11;
	@FXML
	private ImageView imgV12;
	@FXML
	private ImageView imgV13;
	@FXML
	private ImageView imgV21;
	@FXML
	private ImageView imgV22;
	@FXML
	private ImageView imgV23;
	@FXML
	private ImageView imgV31;
	@FXML
	private ImageView imgV32;
	@FXML
	private ImageView imgV33;
	@FXML
	private Label lbTitle11;
	@FXML
	private Label lbTitle12;
	@FXML
	private Label lbTitle13;
	@FXML
	private Label lbTitle21;
	@FXML
	private Label lbTitle22;
	@FXML
	private Label lbTitle23;
	@FXML
	private Label lbTitle31;
	@FXML
	private Label lbTitle32;
	@FXML
	private Label lbTitle33;
	@FXML
	private Button btnBack;
	@FXML
	private Button btnCategory1;
	@FXML
	private Button btnCategory11;
	@FXML
	private Button btnCategory12;
	@FXML
	private Button btnCategory13;
	@FXML
	private Button btnCategory14;
	@FXML
	private Button btnCategory15;
	@FXML
	private Button btnCategory16;
	@FXML
	private Button btnCategory17;
	@FXML
	private Button btnNext;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnSearch;
	@FXML
	private TextField txtSearch;
	private String listViewSelectItem;
	private String wkdfm = null;
	public Stage stage;
	private ArrayList<Book> bookList = new ArrayList<Book>();
	private ArrayList<Book> bookList2 = null;
	private ArrayList<Label> bookTitleList = new ArrayList<Label>();
	private ArrayList<ImageView> bookImageVList = new ArrayList<ImageView>();
	private ArrayList<Button> buttonList = new ArrayList<Button>();
	private String selectFileName;
	private String localUrl;
	private int bookCount;
	private int page = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 검색 버튼 이벤트
		btnSearch.setOnAction(e -> handleBtnSearchAction(e));

		// 뒤로가기 버튼 이벤트
		btnExit.setOnAction(e -> handleBtnBackAction(e));

		// 도서 선택 이벤트
		getBookSelectMethod();

		// 장르선택 이벤트
		getCategorySelectMethod();

	}
	/////////////////////////////////////////////////////////////////

	// 제목 검색 버튼 이벤트
	private void handleBtnSearchAction(ActionEvent e) {
		bookBox.setStyle("-fx-background-color:#dedcee");
		page = 0;
		btnNext.setDisable(true);
		btnBack.setDisable(true);
		try {
			if (txtSearch.getText().trim().equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("검색 오류");
				alert.setHeaderText("검색할 책 제목을 입력하세요.");
				alert.showAndWait();
				return;
			}
			BookDAO dao = new BookDAO();

			bookList = dao.searchBook(txtSearch.getText(), "title");
			bookCount = bookList.size();
			bookTitleList.addAll(FXCollections.observableArrayList(lbTitle11, lbTitle12, lbTitle13, lbTitle21,
					lbTitle22, lbTitle23, lbTitle31, lbTitle32, lbTitle33));
			bookImageVList.addAll(FXCollections.observableArrayList(imgV11, imgV12, imgV13, imgV21, imgV22, imgV23,
					imgV31, imgV32, imgV33));
			for (int j = 0; j < 9; j++) {
				bookTitleList.get(j).setText("");
				bookImageVList.get(j).setImage(null);
				bookImageVList.get(j).setDisable(true);
			}
			if (bookCount > 9) {
				bookList2 = bookList;
			}

			for (int i = 0; i < 9; i++) {
				if (bookCount < 9) {
					if (i == bookCount) {
						break;
					}
					;
				}
				Book b = bookList.get(i);
				selectFileName = b.getFileimg();
				localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
				bookTitleList.get(i).setText(b.getTitle());
				bookImageVList.get(i).setImage(new Image(localUrl));
				bookImageVList.get(i).setDisable(false);
			}

			if (bookList2.size() > 9) {
				btnNext.setDisable(false);
				btnNext.setOnAction(event -> {
					page++;
					btnBack.setDisable(false);
					for (int j = 0; j < 9; j++) {
						bookTitleList.get(j).setText("");
						bookImageVList.get(j).setImage(null);
						bookImageVList.get(j).setDisable(true);
					}

					for (int i = 9; i < bookList2.size(); i++) {

						Book b = bookList.get(i);
						selectFileName = b.getFileimg();
						localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
						bookTitleList.get(i - 9).setText(b.getTitle());
						bookImageVList.get(i - 9).setImage(new Image(localUrl));
						bookImageVList.get(i - 9).setDisable(false);

					}
					btnNext.setDisable(true);
				});
				btnBack.setOnAction(event -> {
					for (int j = 0; j < 9; j++) {
						bookTitleList.get(j).setText("");
						bookImageVList.get(j).setImage(null);
						bookImageVList.get(j).setDisable(true);
					}

					for (int i = 0; i < 9; i++) {
						if (bookCount < 9) {
							if (i == bookCount) {
								break;
							}
							;
						}
						Book b = bookList.get(i);
						selectFileName = b.getFileimg();
						localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
						bookTitleList.get(i).setText(b.getTitle());
						bookImageVList.get(i).setImage(new Image(localUrl));
						bookImageVList.get(i).setDisable(false);
					}
					page--;
					btnBack.setDisable(true);
					btnNext.setDisable(false);
				});

			}
		} catch (Exception e2) {

		}
	}

	// 장르선택 이벤트
	private void getCategorySelectMethod() {
		btnCategory1.setOnAction(e -> {
			listViewSelectIndexSetMethod(btnCategory1);
			getCartegoryBook();
		});
		btnCategory11.setOnAction(e -> {
			listViewSelectIndexSetMethod(btnCategory11);
			getCartegoryBook();
		});
		btnCategory12.setOnAction(e -> {
			listViewSelectIndexSetMethod(btnCategory12);
			getCartegoryBook();
		});
		btnCategory13.setOnAction(e -> {
			listViewSelectIndexSetMethod(btnCategory13);
			getCartegoryBook();
		});
		btnCategory14.setOnAction(e -> {
			listViewSelectIndexSetMethod(btnCategory14);
			getCartegoryBook();
		});
		btnCategory15.setOnAction(e -> {
			listViewSelectIndexSetMethod(btnCategory15);
			getCartegoryBook();
		});
		btnCategory16.setOnAction(e -> {
			listViewSelectIndexSetMethod(btnCategory16);
			getCartegoryBook();
		});
		btnCategory17.setOnAction(e -> {
			listViewSelectIndexSetMethod(btnCategory17);
			getCartegoryBook();
		});
	}

	// 뒤로가기
	private void handleBtnBackAction(ActionEvent e) {
		try {

			Stage user_MainStage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/user_Main.fxml"));
			Parent root = fxmlLoader.load();
			User_MainController user_MainController = fxmlLoader.getController();
			user_MainController.userStage = user_MainStage;

			root = FXMLLoader.load(getClass().getResource("/view/user_Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			user_MainStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			user_MainStage.setTitle("KD Library");
			user_MainStage.setScene(scene);
			user_MainStage.initOwner(this.stage);
			user_MainStage.setResizable(true);
			((Stage) btnBack.getScene().getWindow()).close();
			user_MainStage.show();
		} catch (IOException e1) {
		}
	}

	// 버튼(장르) 셋팅 & 꾸미기
	private void listViewSelectIndexSetMethod(Button button) {
		for (Button b : buttonList) {
			b.setStyle("-fx-background-color:  #00ff0000");
		}
		wkdfm = button.getText();
		listViewSelectItem = wkdfm;
		button.setStyle("-fx-background-color:#dedcee");
	}

	// 장르 선택시 책 진열
	private void getCartegoryBook() {
		page = 0;
		bookList2 = null;
		bookBox.setStyle("-fx-background-color:#dedcee");
		BookDAO dao = new BookDAO();
		bookList = dao.searchBook(listViewSelectItem, "category");
		bookCount = bookList.size();
		bookTitleList.addAll(FXCollections.observableArrayList(lbTitle11, lbTitle12, lbTitle13, lbTitle21, lbTitle22,
				lbTitle23, lbTitle31, lbTitle32, lbTitle33));
		bookImageVList.addAll(FXCollections.observableArrayList(imgV11, imgV12, imgV13, imgV21, imgV22, imgV23, imgV31,
				imgV32, imgV33));
		try {
			btnNext.setDisable(true);
			btnBack.setDisable(true);
			for (int j = 0; j < 9; j++) {
				bookTitleList.get(j).setText("");
				bookImageVList.get(j).setImage(null);
				bookImageVList.get(j).setDisable(true);
			}
			if (bookCount > 9) {
				bookList2 = bookList;
			}

			for (int i = 0; i < 9; i++) {
				if (bookCount < 9) {
					if (i == bookCount)
						break;

				}
				Book b = bookList.get(i);
				selectFileName = b.getFileimg();
				localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
				bookTitleList.get(i).setText(b.getTitle());
				bookImageVList.get(i).setImage(new Image(localUrl));
				bookImageVList.get(i).setDisable(false);
			}

			if (bookList2.size() > 9) {
				btnNext.setDisable(false);
				btnNext.setOnAction(event -> {
					page++;
					btnBack.setDisable(false);
					for (int j = 0; j < 9; j++) {
						bookTitleList.get(j).setText("");
						bookImageVList.get(j).setImage(null);
						bookImageVList.get(j).setDisable(true);
					}

					for (int i = 9; i < bookList2.size(); i++) {
						Book b = null;
						try {
							b = bookList.get(i);
							selectFileName = b.getFileimg();
							localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
							bookTitleList.get(i - 9).setText(b.getTitle());
							bookImageVList.get(i - 9).setImage(new Image(localUrl));
							bookImageVList.get(i - 9).setDisable(false);
						} catch (Exception e) {
							System.out.println("페이지넘기기 오류 ㅠ");
						}

					}
					btnNext.setDisable(true);

				});
				btnBack.setOnAction(event -> {
					for (int j = 0; j < 9; j++) {
						bookTitleList.get(j).setText("");
						bookImageVList.get(j).setImage(null);
						bookImageVList.get(j).setDisable(true);
					}

					for (int i = 0; i < 9; i++) {
						if (bookCount < 9) {
							if (i == bookCount) {
								break;
							}
							;
						}
						Book b = bookList.get(i);
						selectFileName = b.getFileimg();
						localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
						bookTitleList.get(i).setText(b.getTitle());
						bookImageVList.get(i).setImage(new Image(localUrl));
						bookImageVList.get(i).setDisable(false);
					}
					page--;
					btnBack.setDisable(true);
					btnNext.setDisable(false);
				});

			}
		} catch (Exception e2) {
		}

	}

	// 도서 선택 핸들러 함수
	private void getBookSelectMethod() {
		buttonList.addAll(FXCollections.observableArrayList(btnCategory1, btnCategory11, btnCategory12, btnCategory13,
				btnCategory14, btnCategory15, btnCategory16, btnCategory17));
		for (Button b : buttonList) {
			b.setText(BookDAO.categoryList.get(buttonList.indexOf(b)));
		}
		imgV11.setOnMouseClicked(e -> getBookInformationPopup(bookList.get(0 + (page * 9))));
		imgV12.setOnMouseClicked(e -> getBookInformationPopup(bookList.get(1 + (page * 9))));
		imgV13.setOnMouseClicked(e -> getBookInformationPopup(bookList.get(2 + (page * 9))));
		imgV21.setOnMouseClicked(e -> getBookInformationPopup(bookList.get(3 + (page * 9))));
		imgV22.setOnMouseClicked(e -> getBookInformationPopup(bookList.get(4 + (page * 9))));
		imgV23.setOnMouseClicked(e -> getBookInformationPopup(bookList.get(5 + (page * 9))));
		imgV31.setOnMouseClicked(e -> getBookInformationPopup(bookList.get(6 + (page * 9))));
		imgV32.setOnMouseClicked(e -> getBookInformationPopup(bookList.get(7 + (page * 9))));
		imgV33.setOnMouseClicked(e -> getBookInformationPopup(bookList.get(8 + (page * 9))));

	}

	// 도서 정보 창셋팅 메소드
	private void getBookInformationPopup(Book b) {
		try {
			Parent userModifyView = FXMLLoader.load(getClass().getResource("/view/user_bookInformation.fxml"));
			Scene scene = new Scene(userModifyView);
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			Stage userModifyStage = new Stage();
			userModifyStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			Label lbTitle = (Label) scene.lookup("#lbTitle");
			Label lbISBN = (Label) scene.lookup("#lbISBN");
			Label lbWriter = (Label) scene.lookup("#lbWriter");
			Label lbCompany = (Label) scene.lookup("#lbCompany");
			Label lbDate = (Label) scene.lookup("#lbDate");
			TextArea txaInformation = (TextArea) scene.lookup("#txaInformation");
			Button btnClose = (Button) scene.lookup("#btnClose");
			Button btnRental = (Button) scene.lookup("#btnRental");
			ImageView imgV = (ImageView) scene.lookup("#imgV");
			lbTitle.setText(b.getTitle());
			lbISBN.setText(b.getIsbn());
			lbWriter.setText(b.getWriter());
			lbCompany.setText(b.getCompany());
			lbDate.setText(b.getDate());
			txaInformation.setText(b.getInformation());
			selectFileName = b.getFileimg();
			localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
			imgV.setImage(new Image(localUrl));
			userModifyStage.initModality(Modality.WINDOW_MODAL);
			userModifyStage.initOwner(this.stage);
			userModifyStage.setScene(scene);
			userModifyStage.setResizable(false);
			userModifyStage.setTitle("책 정보");
			userModifyStage.show();
			btnClose.setOnAction(e -> userModifyStage.close());
			btnRental.setOnAction(e -> {
				// MemberDAO dao = new MemberDAO();
				if (!(b.isRental())) {
					if (MemberDAO.m.getRentalBook() == null) {
						Connection con1 = null;
						PreparedStatement preparedStatement1 = null;
						PreparedStatement preparedStatement2 = null;
						PreparedStatement preparedStatement3 = null;
						try {

							con1 = DBUtil.getConnection(); //
							String query1 = "update memberTBL set rentalBook=? where Id=?";
							String query2 = "update BookTBL set rental=? where ISBN=?";
							String query3 = "insert into StatisticalTBL values (?,?,?,null)";
							preparedStatement1 = con1.prepareStatement(query1);
							preparedStatement2 = con1.prepareStatement(query2);
							preparedStatement3 = con1.prepareStatement(query3);

							preparedStatement1.setString(1, b.getIsbn());
							preparedStatement1.setString(2, MemberDAO.m.getId());

							preparedStatement2.setBoolean(1, true);
							preparedStatement2.setString(2, b.getIsbn());

							preparedStatement3.setString(1, b.getIsbn());
							preparedStatement3.setString(2, LocalDate.now().getYear() + "-"
									+ LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth());
							preparedStatement3.setString(3, MemberDAO.m.getId());

							if (preparedStatement1.executeUpdate() != 0 && preparedStatement2.executeUpdate() != 0
									&& preparedStatement3.executeUpdate() != 0) {
								MemberDAO.m.setRentalBook(b.getIsbn());
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setHeaderText("대여완료");
								alert.showAndWait();
								userModifyStage.close();
							} else {
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setHeaderText("등록 실패");

								alert.showAndWait();
								throw new Exception();
							}

						} catch (Exception e1) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setHeaderText("등록 실패 DB에러");
							alert.setContentText(e1.getMessage());
							alert.showAndWait();
						}

					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("대여중인 책이 있습니다.");
						alert.setContentText("책을 반납하고 도서를 대여하세요.");
						alert.showAndWait();
					}
				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("이미 대여된 책입니다.");
					alert.showAndWait();
				}
			});
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러발생");
			alert.setHeaderText("도서 정보 창 부르기 오류");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

}

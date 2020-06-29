package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Book;
import model.Member;
import model.RequestBook;

public class AdminManagement implements Initializable {
	@FXML
	TableView tblUser;
	@FXML
	TableView tblBook;
	@FXML
	TableView tblRequest;
///////////////////////////
	@FXML
	TabPane tabContainer;
	@FXML
	Tab memberTab;
	@FXML
	AnchorPane memberContainer;
	@FXML
	Tab bookTab;
	@FXML
	AnchorPane bookContainer;
	@FXML
	Tab requestTab;
	@FXML
	AnchorPane requestContainer;

///////////////////////////
	@FXML
	Button btnBookAdd;
	@FXML
	Button btnBack;
	@FXML
	Button btnBookDelete;
	@FXML
	Button btnBookEdit;
	@FXML
	Button btnUserEdit;
	@FXML
	Button btnUserDelete;
	@FXML
	Button btnUserSearch;
	@FXML
	Button btnBookSearch;
	@FXML
	Button btnRequestDelete;
	@FXML
	Button btnBookCategory;
	@FXML
	Button btnBarChart;
	@FXML
	Button btnPieChart;
	@FXML
	TextField txtBookSearch;
	@FXML
	TextField txtUserSearch;
	@FXML
	private NumberAxis xAxis;
	@FXML
	public Stage stage;
	private double tabWidth = 90.0;
	public static int lastSelectedTabIndex = 0;
	private File selectFile;
	private File directorySave;
	private int bookTableSelectIndex = -1;
	private int requestTableSelectIndex = -1;
	private int userTableSelectIndex = -1;
	private String localUrl;
	private Image localImage;
	private String selectFileName;
	private Image image = null;
	private ObservableList<Book> obLBook = FXCollections.observableArrayList();
	private ObservableList<RequestBook> obLRequest = FXCollections.observableArrayList();
	private ObservableList<Member> obLMember = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnBookCategory.setOnAction(e -> handelBtnBarChartAction(e));
		// 도서 이미지 저장 디렉토리 생성 이벤트
		setDirectorySaveImage();
		// 회원 테이블 셋팅 이벤트
		tblUserColumnSetting();
		// 도서 테이블 셋팅 이벤트
		tblBookColumnSetting();
		// 자료요청 테이블 셋팅 이벤트
		tblRequestColumnSetting();

		// 도서 테이블 선택 인덱스 반환 이벤트
		tblBook.setOnMousePressed(e -> bookTableSelectIndex = tblBook.getSelectionModel().getSelectedIndex());
		// 도서 테이블 추가 버튼 이벤트
		btnBookAdd.setOnAction(e -> handleBtnBookAddAction(e));
		// 도서테이블 수정 버튼 이벤트
		btnBookEdit.setOnAction(e -> handleBtnBookEditAction(e));
		// 도서 테이블 책 더블클릭시 수정창
		// tblBook.setOnMouseClicked(e -> handelTblBookDoubleClickAction(e));
		// 도서 테이블 삭제 버튼 이벤트
		btnBookDelete.setOnAction(e -> handleBtnBookDeleteAction(e));
		// 도서 테이블 검색 버튼 이벤트
		btnBookSearch.setOnAction(e -> handleBtnBookSearchAction(e));

		// 회원 테이블 선택 인덱스 반환 이벤트
		tblUser.setOnMousePressed(e -> userTableSelectIndex = tblUser.getSelectionModel().getSelectedIndex());
		// 회원 테이블 검색 버튼 이벤트
		btnUserSearch.setOnAction(e -> handleBtnUserSearchAction(e));
		// 회원 테이블 수정 버튼 이벤트
		btnUserEdit.setOnAction(e -> handleBtnUserEditAction(e));
		// 회원 테이블 삭제 버튼 이벤트
		btnUserDelete.setOnAction(e -> handleBtnUserDeleteAction(e));

		// 자료요청 테이블 선택 인덱스 반환 이벤트
		tblRequest.setOnMousePressed(e -> requestTableSelectIndex = tblRequest.getSelectionModel().getSelectedIndex());
		// 요청 테이블 책 더블클릭시 내용창
		tblRequest.setOnMouseClicked(e -> handelTblRequestDoubleClickAction(e));
		// 요청테이블 삭제 버튼 이벤트
		btnRequestDelete.setOnAction(e -> handleBtnRequestDeleteAction(e));

		// 관리페이지 뒤로가기
		btnBack.setOnAction(e -> handleBtnBackAction(e));

		configureView();
	}

////////////////////////////////////////////////////////////////////////////////////////////////////

	// 탭페인 디자인 메소드
	private void configureView() {
		// memberTab.setStyle("-fx-background-color:pink;");
		tabContainer.setTabMinWidth(tabWidth);
		tabContainer.setTabMaxWidth(tabWidth);
		tabContainer.setTabMinHeight(tabWidth);
		tabContainer.setTabMaxHeight(tabWidth);
		tabContainer.setRotateGraphic(true);
	}

	// 뒤로가기 버튼 핸들러 이벤트
	private void handleBtnBackAction(ActionEvent e) {
		try {

			Stage adminMain = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/admin_Main.fxml"));
			Parent root = fxmlLoader.load();
			AdminController adminController = fxmlLoader.getController();
			adminController.stage = adminMain;
			adminMain.initOwner(stage);

			Scene scene = new Scene(root);
			adminMain.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			adminMain.setTitle("KD Library-Admin");
			adminMain.setScene(scene);
			adminMain.setResizable(true);
			((Stage) btnBack.getScene().getWindow()).close();
			adminMain.show();
		} catch (IOException e1) {
		}
	}

	/* ========================회원관리====================== */

	// 유저 테이블 검색 버튼 이벤트
	private void handleBtnUserSearchAction(ActionEvent e) {
		MemberDAO dao = new MemberDAO();
		// ArrayList<Member> arrayList = dao.searchUser(txtUserSearch.getText());
		ArrayList<Member> arrayList = dao.searchUser(txtUserSearch.getText());
		obLMember.clear();
		for (Member m : arrayList) {
			obLMember.add(m);

		}
	}

	// 유저 테이블 삭제 버튼 이벤트
	private void handleBtnUserDeleteAction(ActionEvent e) {
		try {
			if (userTableSelectIndex == -1)
				throw new Exception();
			Member selectUser = obLMember.get(userTableSelectIndex);
			MemberDAO dao = new MemberDAO();

			int returnValue = dao.deleteUser(selectUser);
			if (returnValue != 0) {
				obLMember.remove(userTableSelectIndex);

			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("데이터 미선택");
			alert.setContentText("삭제할 데이터를 선택하세요.");
			alert.showAndWait();
			userTableSelectIndex = -1;
		}

	}

	// 유저테이블 수정 버튼 이벤트
	private void handleBtnUserEditAction(ActionEvent e) {

		try {
			if (userTableSelectIndex == -1)
				throw new Exception();
			Parent root = FXMLLoader.load(getClass().getResource("/view/admin_MemberEdit.fxml"));
			Scene s = new Scene(root);
			Stage addPopup = new Stage();
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			addPopup.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			addPopup.initModality(Modality.WINDOW_MODAL);
			addPopup.initOwner(btnBookAdd.getScene().getWindow());
			addPopup.setScene(s);
			addPopup.setTitle("회원 정보 수정");

			Button btnOk = (Button) root.lookup("#btnOk");
			Button btnCancel = (Button) root.lookup("#btnCancel");
			TextField txtId = (TextField) root.lookup("#txtId");
			TextField txtName = (TextField) root.lookup("#txtName");
			TextField txtPass = (TextField) root.lookup("#txtPass");
			TextField txtPhoneNumber = (TextField) root.lookup("#txtPhoneNumber");
			// DatePicker dpBirth = (DatePicker) root.lookup("#dpBirth");
			ComboBox cmbEtc = (ComboBox) root.lookup("#cmbEtc");
			ComboBox<String> cmbYear = (ComboBox) root.lookup("#cmbYear");
			ComboBox<String> cmbMonth = (ComboBox) root.lookup("#cmbMonth");
			ComboBox<String> cmbDay = (ComboBox) root.lookup("#cmbDay");
			Label lbRentalBook = (Label) root.lookup("#lbRentalBook");
			cmbEtc.setItems(FXCollections.observableArrayList("정상", "연체"));

			Member selectUser = obLMember.get(userTableSelectIndex);

			btnCancel.setOnAction(eve -> addPopup.close());

			txtId.setText(selectUser.getId());
			txtName.setText(selectUser.getName());
			txtPass.setText(selectUser.getPass());
			txtPhoneNumber.setText(selectUser.getPhoneNumber());
			if (selectUser.getRentalBook() == null)
				lbRentalBook.setText("대여중인 책 없음.");
			else
				lbRentalBook.setText(selectUser.getRentalBook());
			String[] birth = selectUser.getBirth().split("-");
			cmbYear.setValue(birth[0]);
			cmbMonth.setValue(birth[1]);
			cmbDay.setValue(birth[2]);
			cmbEtc.setValue(selectUser.getEtc());
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

			addPopup.show();

			btnOk.setOnAction(eve -> {
				Connection con1 = null;
				PreparedStatement preparedStatement = null;
				try {
					if (txtName.getText().trim().equals("") || txtPass.getText().trim().equals("")
							|| txtPhoneNumber.getText().trim().equals(""))
						throw new Exception();
					con1 = DBUtil.getConnection(); //
					String query = "update memberTBL set name=?,pass=?,phoneNumber=?,birth=?,etc=? where Id=?";
					preparedStatement = con1.prepareStatement(query);

					preparedStatement.setString(1, txtName.getText());
					preparedStatement.setString(2, txtPass.getText());
					preparedStatement.setString(3, txtPhoneNumber.getText());
					preparedStatement.setString(4,
							cmbYear.getValue() + "-" + cmbMonth.getValue() + "-" + cmbDay.getValue());
					preparedStatement.setString(5, cmbEtc.getValue().toString());
					preparedStatement.setString(6, selectUser.getId());
					selectUser.setName(txtName.getText());
					selectUser.setPass(txtPass.getText());
					selectUser.setPhoneNumber(txtPhoneNumber.getText());
					selectUser.setEtc(cmbEtc.getValue().toString());

					if (preparedStatement.executeUpdate() != 0) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("회원정보 수정");
						alert.setHeaderText("수정 완료");
						alert.showAndWait();
						addPopup.close();
						obLMember.set(userTableSelectIndex, selectUser);
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("에러");
						alert.setHeaderText("등록 실패");
						alert.showAndWait();
						throw new Exception();
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("모든 항목을 입력하세요.");
					alert.showAndWait();
				} finally {
					try {
						if (preparedStatement != null)
							preparedStatement.close();
						if (con1 != null)
							con1.close();
					} catch (SQLException e1) {
						System.out.println("RootController edit-save : " + e1.getMessage());
					}
				}
				tblUser.getSelectionModel().clearSelection();
				userTableSelectIndex = -1;
			});
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("데이터 미선택");
			alert.setContentText("수정할 데이터를 선택하세요.");
			alert.showAndWait();
		}

	}

	// 유저데이터 테이블뷰셋팅 메소드
	private void tblUserColumnSetting() {

		TableColumn colName = new TableColumn("이름");
		colName.setMaxWidth(150);
		colName.setCellValueFactory(new PropertyValueFactory("name"));
		TableColumn colId = new TableColumn("ID");
		colId.setMaxWidth(100);
		colId.setCellValueFactory(new PropertyValueFactory("id"));
		TableColumn colPhoneNumber = new TableColumn("전화번호");
		colPhoneNumber.setMaxWidth(200);
		colPhoneNumber.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
		TableColumn colBirth = new TableColumn("생년월일");
		colBirth.setMaxWidth(150);
		colBirth.setCellValueFactory(new PropertyValueFactory("birth"));
		TableColumn colRentalBook = new TableColumn("대여중인 도서");
		colRentalBook.setMaxWidth(300);
		colRentalBook.setCellValueFactory(new PropertyValueFactory("rentalBook"));
		TableColumn colEtc = new TableColumn("비고");
		colEtc.setMaxWidth(100);
		colEtc.setCellValueFactory(new PropertyValueFactory("etc"));

		tblUser.getColumns().addAll(colName, colId, colPhoneNumber, colBirth, colRentalBook, colEtc);

		MemberDAO dao = new MemberDAO();
		ArrayList<Member> userTBL = dao.getUserTbl();
		for (Member m : userTBL) {
			obLMember.add(m);
		}

		tblUser.setItems(obLMember);

	}

	/* ========================도서관리====================== */

	// 도서탭 수정 버튼 핸들러이벤트
	private void handleBtnBookEditAction(ActionEvent e) {

		try {

			if (bookTableSelectIndex == -1)
				throw new Exception("수정할 데이터를 선택하세요.");
			Parent root = FXMLLoader.load(getClass().getResource("/view/admin_BookAdd.fxml"));
			Scene s = new Scene(root);
			Stage addPopup = new Stage();
			addPopup.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			addPopup.initModality(Modality.WINDOW_MODAL);
			addPopup.initOwner(btnBookAdd.getScene().getWindow());
			addPopup.setTitle("도서 수정");
			addPopup.setScene(s);
			addPopup.show();
			Button btnOk = (Button) root.lookup("#btnOk");
			Button btnCancel = (Button) root.lookup("#btnCancel");
			Button btnFileSelect = (Button) root.lookup("#btnFileSelect");
			TextField txtISBN = (TextField) root.lookup("#txtISBN");
			TextField txtTitle = (TextField) root.lookup("#txtTitle");
			TextField txtWriter = (TextField) root.lookup("#txtWriter");
			ComboBox cmbCategory = (ComboBox) root.lookup("#cmbCategory");
			TextField txtCompany = (TextField) root.lookup("#txtCompany");
			TextField txtDate = (TextField) root.lookup("#txtDate");
			TextArea txaInformation = (TextArea) root.lookup("#txaInformation");
			ImageView imgV = (ImageView) root.lookup("#imgV");
			btnCancel.setOnAction(eve -> addPopup.close());
			//BookDAO dao = new BookDAO();
			cmbCategory.setItems(BookDAO.categoryList);
			Book book0 = obLBook.get(bookTableSelectIndex);
			selectFileName = book0.getFileimg();
			localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
			image = new Image(localUrl);
			imgV.setImage(image);

			txtISBN.setText(book0.getIsbn());
			txtISBN.setDisable(true);
			txtTitle.setText(book0.getTitle());
			txtWriter.setText(book0.getWriter());
			cmbCategory.getSelectionModel().select(book0.getCategory());
			txtCompany.setText(book0.getCompany());
			txtDate.setText(book0.getDate());
			txaInformation.setText(book0.getInformation());
			String fileName = "Book_" + book0.getIsbn() + "_" + book0.getTitle() + ".jpg";

			selectFile = new File(directorySave.getAbsolutePath() + "\\" + fileName.trim());

			btnFileSelect.setOnAction(eve1 -> {
				image = handleBtnImageFileAction(addPopup);
				imgV.setImage(image);
			});

			btnOk.setOnAction(eve -> {
				// String fileName1= null;
				Connection con1 = null;
				PreparedStatement preparedStatement = null;
				try {

					con1 = DBUtil.getConnection(); //
					String query = "update BookTBL set ISBN=?,title=?,writer=?,category=?,company=?,date=?,information=?,fileimg=? where ISBN=?";
					preparedStatement = con1.prepareStatement(query);

					preparedStatement.setString(1, txtISBN.getText());
					preparedStatement.setString(2, txtTitle.getText());
					preparedStatement.setString(3, txtWriter.getText());
					preparedStatement.setString(4, cmbCategory.getValue().toString());
					preparedStatement.setString(5, txtCompany.getText());
					preparedStatement.setString(6, txtDate.getText());
					preparedStatement.setString(7, txaInformation.getText());
					preparedStatement.setString(8, fileName);
					preparedStatement.setString(9, book0.getIsbn());
					book0.setIsbn(txtISBN.getText());
					book0.setTitle(txtTitle.getText());
					book0.setWriter(txtWriter.getText());
					book0.setCategory(cmbCategory.getValue().toString());
					book0.setCompany(txtCompany.getText());
					book0.setDate(txtDate.getText());
					book0.setInformation(txaInformation.getText());

					if (preparedStatement.executeUpdate() != 0) {
						// imageDelete(selectFileName);

						BufferedInputStream bis = null;// 파일을 읽을때 사용하는 클래스
						BufferedOutputStream bos = null;// 파일을 쓸때 사용하는 클래스
						try {
							book0.setFileimg(fileName);
							bis = new BufferedInputStream(new FileInputStream(selectFile));
							bos = new BufferedOutputStream(
									new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));
							int data = -1;
							while ((data = bis.read()) != -1) {
								bos.write(data);
								bos.flush();
							}
						} catch (Exception e1) {
							System.out.println("파일 복사에러 : " + e1.getMessage());
							return;
						} finally {
							try {
								book0.setFileimg(fileName);

								if (bis != null)
									bis.close();
								if (bos != null)
									bos.close();
							} catch (IOException e1) {
							}
						}

						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("등록 완료");
						alert.showAndWait();
						addPopup.close();
						obLBook.set(bookTableSelectIndex, book0);
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("등록 실패 : DB에러");
					alert.setContentText(e1.getMessage());
					alert.showAndWait();
				} finally {
					try {
						if (preparedStatement != null)
							preparedStatement.close();
						if (con1 != null)
							con1.close();
					} catch (SQLException e1) {
						e1.getMessage();
					}
				}
				tblBook.getSelectionModel().clearSelection();
				bookTableSelectIndex = -1;
			});
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("데이터 미선택");
			alert.setContentText("수정할 데이터를 선택하세요.");
			alert.showAndWait();
		}

	}

	// 도서탭 추가 버튼 핸들러이벤트
	private void handleBtnBookAddAction(ActionEvent e) {
		try {
			BookDAO dao = new BookDAO();
			Parent root = FXMLLoader.load(getClass().getResource("/view/admin_BookAdd.fxml"));
			Stage addPopup = new Stage();
			addPopup.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			addPopup.initModality(Modality.WINDOW_MODAL);
			addPopup.initOwner(btnBookAdd.getScene().getWindow());
			Scene s = new Scene(root);
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			addPopup.setScene(s);
			addPopup.setTitle("도서 추가");
			addPopup.show();

			Button btnOk = (Button) s.lookup("#btnOk");
			Button btnCancel = (Button) s.lookup("#btnCancel");
			Button btnFileSelect = (Button) s.lookup("#btnFileSelect");
			TextField txtISBN = (TextField) s.lookup("#txtISBN");
			TextField txtTitle = (TextField) s.lookup("#txtTitle");
			TextField txtWriter = (TextField) s.lookup("#txtWriter");
			ComboBox cmbCategory = (ComboBox) s.lookup("#cmbCategory");
			TextField txtCompany = (TextField) s.lookup("#txtCompany");
			TextField txtDate = (TextField) s.lookup("#txtDate");
			TextArea txaInformation = (TextArea) s.lookup("#txaInformation");
			ImageView imgV = (ImageView) s.lookup("#imgV");
			cmbCategory.setItems(dao.categoryList);

			btnFileSelect.setOnAction(eve1 -> {
				Image image = handleBtnImageFileAction(addPopup);
				imgV.setImage(image);
			});

			btnOk.setOnAction(eve -> {
				Book book1 = null;
				String fileName = null;
				try {
					book1 = new Book(txtISBN.getText(), txtTitle.getText(), cmbCategory.getValue().toString(),
							txtWriter.getText(), txtCompany.getText(), txtDate.getText(), null,
							txaInformation.getText(), false);
					if (txtISBN.getText().trim().equals("") || txtTitle.getText().trim().equals("")
							|| txtWriter.getText().trim().equals("") || txtCompany.getText().trim().equals("")
							|| txtDate.getText().trim().equals("") || txaInformation.getText().trim().equals(""))
						throw new Exception();
					fileName = "Book_" + book1.getIsbn() + "_" + book1.getTitle() + ".jpg";
					book1.setFileimg(fileName);
				//	BookDAO dao = new BookDAO();
					if (selectFile == null) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("문제발생");
						alert.setHeaderText("이미지 파일을 선택하세요");
						alert.setContentText("다음에는 주의하세요.");
						alert.showAndWait();
						return;
					}
					int returnValue = dao.addBook(book1);
					if (returnValue != 0) {

						obLBook.add(book1);

						BufferedInputStream bis = null;// 파일을 읽을때 사용하는 클래스
						BufferedOutputStream bos = null;// 파일을 쓸때 사용하는 클래스
						try {

							bis = new BufferedInputStream(new FileInputStream(selectFile));
							bos = new BufferedOutputStream(
									new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));
							int data = -1;
							while ((data = bis.read()) != -1) {
								bos.write(data);
								bos.flush();
							}
						} catch (Exception e1) {
							System.out.println("파일 복사에러 : " + e1.getMessage());
							return;
						} finally {
							try {
								book1.setFileimg(fileName);
								selectFile = null;
								if (bis != null)
									bis.close();
								if (bos != null)
									bos.close();
							} catch (IOException e1) {
							}
						}

						addPopup.close();
					}
				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("오류");
					alert.setHeaderText("모든 항목을 작성하세요");
					alert.showAndWait();
					return;
				}

			});
			btnCancel.setOnAction(eve -> addPopup.close());
		} catch (Exception e1) {
		}
	}

	// 도서탭 삭제 버튼 핸들러이벤트
	private void handleBtnBookDeleteAction(ActionEvent e) {

		if (bookTableSelectIndex == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("데이터 미선택");
			alert.setContentText("삭제할 데이터를 선택하세요.");
			alert.showAndWait();
			return;
		}
		Book selectBook = obLBook.get(bookTableSelectIndex);
		selectFileName = selectBook.getFileimg();
		localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
		localImage = new Image(localUrl, false);
		BookDAO dao = new BookDAO();
		int returnValue = dao.deleteBook(selectBook);
		if (returnValue != 0) {
			imageDelete(selectFileName);
			obLBook.remove(bookTableSelectIndex);
			bookTableSelectIndex = -1;
		}
	}

	// 도서 테이블 검색 버튼 이벤트
	private void handleBtnBookSearchAction(ActionEvent e) {
		BookDAO dao = new BookDAO();
		ArrayList<Book> arrayList = dao.searchBook(txtBookSearch.getText(), "title");
		obLBook.clear();
		for (Book b : arrayList) {
			obLBook.add(b);

		}

	}

	// 도서 테이블 셋팅 메소드
	private void tblBookColumnSetting() {
		TableColumn colISBN = new TableColumn("ISBN");
		colISBN.setMaxWidth(150);
		colISBN.setCellValueFactory(new PropertyValueFactory("isbn"));

		TableColumn colTitle = new TableColumn("제목");
		colTitle.setMaxWidth(200);
		colTitle.setCellValueFactory(new PropertyValueFactory("title"));

		TableColumn colCategory = new TableColumn("장르");
		colCategory.setMaxWidth(80);
		colCategory.setCellValueFactory(new PropertyValueFactory("category"));

		TableColumn colWriter = new TableColumn("저자");
		colWriter.setMaxWidth(200);
		colWriter.setCellValueFactory(new PropertyValueFactory("writer"));

		TableColumn colCompany = new TableColumn("출판사");
		colCompany.setMaxWidth(2000);
		colCompany.setCellValueFactory(new PropertyValueFactory("company"));

		TableColumn colDate = new TableColumn("출판일");
		colDate.setMaxWidth(150);
		colDate.setCellValueFactory(new PropertyValueFactory("date"));

		TableColumn colRental = new TableColumn("대여여부");
		colRental.setMaxWidth(90);
		colRental.setCellValueFactory(new PropertyValueFactory("rental"));

		TableColumn colInformation = new TableColumn("책 소개");
		colInformation.setMaxWidth(400);
		colInformation.setCellValueFactory(new PropertyValueFactory("information"));

		tblBook.getColumns().addAll(colISBN, colTitle, colCategory, colWriter, colCompany, colDate, colRental,
				colInformation);
		BookDAO dao = new BookDAO();

		HashSet<Book> bookTBL = dao.getBookTbl();
		for (Book b : bookTBL) {
			obLBook.add(b);
		}
		tblBook.setItems(obLBook);

	}

	// 바차트 막대그래프 출력
	private void handelBtnBarChartAction(Event e) {
		Parent root;
		try {
			BookDAO dao = new BookDAO();
			root = FXMLLoader.load(getClass().getResource("/view/admin_BookCategoryChart.fxml"));
			Stage addPopup = new Stage();
			Scene s = new Scene(root);
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			addPopup.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			BarChart barChart = (BarChart) root.lookup("#barChart");
			Button btnExit = (Button) root.lookup("#btnExit");
			addPopup.initModality(Modality.WINDOW_MODAL);
			addPopup.initOwner(btnBookAdd.getScene().getWindow());
			addPopup.setTitle("장르별 도서 권 수");
			addPopup.setScene(s);
			btnExit.setOnAction(e1 -> addPopup.close());

			XYChart.Series series1 = new XYChart.Series();
			series1.setName(dao.categoryList.get(0));
			XYChart.Series series2 = new XYChart.Series();
			series2.setName(dao.categoryList.get(1));
			XYChart.Series series3 = new XYChart.Series();
			series3.setName(dao.categoryList.get(2));
			XYChart.Series series4 = new XYChart.Series();
			series4.setName(dao.categoryList.get(3));
			XYChart.Series series5 = new XYChart.Series();
			series5.setName(dao.categoryList.get(4));
			XYChart.Series series6 = new XYChart.Series();
			series6.setName(dao.categoryList.get(5));
			XYChart.Series series7 = new XYChart.Series();
			series7.setName(dao.categoryList.get(6));

			series1.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series1.getName(), "category").size())));
			barChart.getData().add(series1);
			series2.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series2.getName(), "category").size())));
			barChart.getData().add(series2);
			series3.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series3.getName(), "category").size())));
			barChart.getData().add(series3);
			series4.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series4.getName(), "category").size())));
			barChart.getData().add(series4);
			series5.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series5.getName(), "category").size())));
			barChart.getData().add(series5);
			series6.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series6.getName(), "category").size())));
			barChart.getData().add(series6);
			series7.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series7.getName(), "category").size())));
			barChart.getData().add(series7);
			addPopup.show();
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("데이터 에러");
			alert.setHeaderText("데이터가 존재하지않습니다.");
			alert.setContentText(e1.getMessage());
			alert.showAndWait();
		}

	}

	/* ========================자료요청관리====================== */

	// 자료요청 테이블 셋팅 메소드
	private void tblRequestColumnSetting() {

		TableColumn colName = new TableColumn("작성자");
		colName.setMaxWidth(80);
		colName.setCellValueFactory(new PropertyValueFactory("name"));

		TableColumn colNo = new TableColumn("No");
		colNo.setMaxWidth(80);
		colNo.setCellValueFactory(new PropertyValueFactory("no"));

		TableColumn colContent = new TableColumn("내용");
		colContent.setPrefWidth(300);
		colContent.setCellValueFactory(new PropertyValueFactory("content"));

		TableColumn colTitle = new TableColumn("제목");
		colTitle.setPrefWidth(250);
		colTitle.setCellValueFactory(new PropertyValueFactory("title"));
		TableColumn colDate = new TableColumn("날짜");
		colDate.setMaxWidth(200);
		colDate.setCellValueFactory(new PropertyValueFactory("date"));

		tblRequest.getColumns().addAll(colNo, colTitle, colContent, colName, colDate);

		RequestDAO dao = new RequestDAO();
		ArrayList<RequestBook> requestTBL = dao.getRequestTbl();
		for (RequestBook r : requestTBL) {
			obLRequest.add(r);
		}
		tblRequest.setItems(obLRequest);

	}

	// 요청 테이블 책 더블클릭시 내용창
	private void handelTblRequestDoubleClickAction(MouseEvent e) {
		if (e.getClickCount() != 2)
			return;
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/user_Request.fxml"));
			Stage addPopup = new Stage();
			Scene s = new Scene(root);
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			addPopup.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			addPopup.initModality(Modality.WINDOW_MODAL);
			addPopup.initOwner(btnBookAdd.getScene().getWindow());
			Button btnAdd = (Button) root.lookup("#btnAdd");
			Button btnBack = (Button) root.lookup("#btnBack");
			TextField txtTitle = (TextField) root.lookup("#txtTitle");
			Label lbName = (Label) root.lookup("#lbName");
			Label lbDate = (Label) root.lookup("#lbDate");
			TextArea txaContent = (TextArea) root.lookup("#txaContent");
			RequestBook request = obLRequest.get(requestTableSelectIndex);
			txtTitle.setText(request.getTitle());
			lbName.setText(request.getName());
			txaContent.setText(request.getContent());
			txtTitle.setEditable(false);
			lbDate.setText(request.getDate());
			txaContent.setEditable(false);
			addPopup.setTitle("자료 요청 내용");
			addPopup.setScene(s);
			addPopup.show();

			btnAdd.setOnAction(eve -> {
				try {
					//BookDAO dao = new BookDAO();
					Parent root1 = FXMLLoader.load(getClass().getResource("/view/admin_BookAdd.fxml"));
					Stage addPopup1 = new Stage();
					addPopup1.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
					addPopup1.initModality(Modality.NONE);
					addPopup1.initOwner(btnAdd.getScene().getWindow());
					Scene s1 = new Scene(root1);
					s1.getStylesheets().add(getClass().getResource("/application/main.css").toString());
					addPopup1.setTitle("도서 추가");
					addPopup1.setScene(s1);
					addPopup1.show();
					Button btnOk = (Button) s1.lookup("#btnOk");
					Button btnCancel = (Button) s1.lookup("#btnCancel");
					Button btnFileSelect = (Button) s1.lookup("#btnFileSelect");
					TextField txtISBN = (TextField) s1.lookup("#txtISBN");
					TextField txtTitle1 = (TextField) s1.lookup("#txtTitle");
					TextField txtWriter = (TextField) s1.lookup("#txtWriter");
					ComboBox cmbCategory = (ComboBox) s1.lookup("#cmbCategory");
					TextField txtCompany = (TextField) s1.lookup("#txtCompany");
					TextField txtDate = (TextField) s1.lookup("#txtDate");
					TextArea txaInformation = (TextArea) s1.lookup("#txaInformation");
					ImageView imgV = (ImageView) s1.lookup("#imgV");
					cmbCategory.setItems(BookDAO.categoryList);

					btnFileSelect.setOnAction(eve1 -> {
						Image image = handleBtnImageFileAction(addPopup);
						imgV.setImage(image);
					});

					btnOk.setOnAction(eve1 -> {
						Book book1 = null;
						try {
							if (txtISBN.getText().trim().equals("") || txtTitle1.getText().trim().equals("")
									|| txtWriter.getText().trim().equals("") || txtCompany.getText().trim().equals("")
									|| txtDate.getText().trim().equals("")
									|| txaInformation.getText().trim().equals(""))
								throw new Exception();
							book1 = new Book(txtISBN.getText(), txtTitle1.getText(), cmbCategory.getValue().toString(),
									txtWriter.getText(), txtCompany.getText(), txtDate.getText(), null,
									txaInformation.getText(), false);
						} catch (Exception e1) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("항목 누락");
							alert.setHeaderText("모든 항목을 입력하세요.");
							alert.showAndWait();
							return;
						}
						if (selectFile == null) {
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("이미지 파일 미선택");
							alert.setHeaderText("이미지 파일을 선택하세요");
							alert.showAndWait();
							return;
						}
						BufferedInputStream bis = null;// 파일을 읽을때 사용하는 클래스
						BufferedOutputStream bos = null;// 파일을 쓸때 사용하는 클래스
						String fileName = null;
						try {
							fileName = "Book_" + book1.getIsbn() + "_" + book1.getTitle() + ".jpg";
							bis = new BufferedInputStream(new FileInputStream(selectFile));
							bos = new BufferedOutputStream(
									new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));
							int data = -1;
							while ((data = bis.read()) != -1) {
								bos.write(data);
								bos.flush();
							}
						} catch (Exception e1) {
							System.out.println("파일 복사에러 : " + e1.getMessage());
							return;
						} finally {
							try {
								book1.setFileimg(fileName);
								selectFile = null;
								if (bis != null)
									bis.close();
								if (bos != null)
									bos.close();
							} catch (IOException e1) {
							}
						}
						// BookDAO dao = new BookDAO();
						int returnValue = dao.addBook(book1);
						if (returnValue != 0) {
							obLBook.add(book1);
							addPopup.close();
						}
					});
					btnCancel.setOnAction(eve1 -> addPopup.close());

				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
			});

			btnBack.setOnAction(eve -> addPopup.close());

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	// 요청테이블 삭제 버튼 이벤트
	private void handleBtnRequestDeleteAction(ActionEvent e) {
		try {
			RequestBook selectRequest = obLRequest.get(requestTableSelectIndex);
			RequestDAO dao = new RequestDAO();
			int returnValue = dao.deleteRequest(selectRequest);
			if (returnValue != 0) {
				obLRequest.remove(requestTableSelectIndex);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	/* ========================서브함수들 ====================== */

	// 이미지파일 삭제 메소드
	public boolean imageDelete(String fileName) {
		boolean result = false;
		File fileDelete = null;
		try {
			fileDelete = new File(directorySave.getAbsolutePath() + "\\" + fileName); // 삭제이미지 파일
			if (fileDelete.exists() && fileDelete.isFile()) {
				result = fileDelete.delete();
			}
		} catch (Exception ie) {
			System.out.println(ie.getMessage());
			result = false;
		}
		return result;
	}

	// 이미지파일선택 버튼 이벤트 등록 핸들러 함수 처리
	private Image handleBtnImageFileAction(Stage stage) {
		Image image = null;
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
		// 선택한 이미지파일을 파일형식으로 돌려준다.
		selectFile = fileChooser.showOpenDialog(stage);
		try {
			if (selectFile != null) {
				String localURL = selectFile.toURI().toURL().toString();// 파일의 실제 경로명!!알아두자
				image = new Image(localURL, false);

			} else {

			}
		} catch (MalformedURLException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("사진 가져오기");
			alert.setHeaderText("사진 가져오기 문제 발생");
			alert.showAndWait();
		}
		return image;
	}

	// 이미지저장 디렉토리 생성 메소드
	private void setDirectorySaveImage() {
		// 도서 폴더
		directorySave = new File("C:/images/Library_BookData");
		if (!directorySave.exists()) {
			directorySave.mkdir();
			System.out.println("도서 디렉토리 생성");
		}

	}

}

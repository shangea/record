package com.mays.record.view;

import com.mays.record.dao.UserDao;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * 添加人员
 * 
 * @author mays
 * @date 2018年4月13日
 */
public class AddUserStage {

	private GridPane mGrid;
	private Stage mStage;
	private TextField mTfName;
	private RadioButton mRbBuyer;
	private RadioButton mRbWorker;
	private Button mBtnSubmit;
	private UserStage mUserStage;

	public AddUserStage(UserStage userStage) {
		mUserStage = userStage;
		mGrid = new GridPane();
		// mGrid.setGridLinesVisible(true);
		mGrid.setHgap(15);
		mGrid.setVgap(25);
		mGrid.setAlignment(Pos.CENTER);
		mStage = new Stage();
		mStage.setTitle("添加人员");
		mStage.setScene(new Scene(mGrid, 400, 300));

		initView();
		initListener();
	}

	private void initView() {
		ColumnConstraints col1 = new ColumnConstraints(80);
		ColumnConstraints col2 = new ColumnConstraints(50, 150, 300);
		mGrid.getColumnConstraints().addAll(col1, col2);

		Label llName = ViewBuilder.getLabel("姓名：");
		Label llType = ViewBuilder.getLabel("类型：");
		GridPane.setHalignment(llName, HPos.RIGHT);
		GridPane.setHalignment(llType, HPos.RIGHT);
		mGrid.addColumn(0, llName, llType);

		mTfName = new TextField();
		mTfName.setFont(Font.font("System Regular", FontWeight.NORMAL, 16));
		mGrid.add(mTfName, 1, 0);

		ToggleGroup group = new ToggleGroup();
		mRbBuyer = new RadioButton("客户");
		mRbBuyer.setToggleGroup(group);
		mRbBuyer.setSelected(true);
		mRbBuyer.setFont(Font.font("System Regular", FontWeight.NORMAL, 16));
		mRbWorker = new RadioButton("员工");
		mRbWorker.setToggleGroup(group);
		mRbWorker.setFont(Font.font("System Regular", FontWeight.NORMAL, 16));
		HBox hb = new HBox(20);
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().add(mRbBuyer);
		hb.getChildren().add(mRbWorker);
		mGrid.add(hb, 1, 1);

		mBtnSubmit = ViewBuilder.getButton("提交");
		GridPane.setHalignment(mBtnSubmit, HPos.CENTER);
		mGrid.add(mBtnSubmit, 0, 2, 2, 1);
	}

	private void initListener() {
		mBtnSubmit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (mTfName.getText().isEmpty()) {
					ViewBuilder.showAlert("请填写姓名！");
					return;
				}
				try {
					if (1 == UserDao.insertUserInfo(mTfName.getText(), mRbBuyer.isSelected() ? 1 : 0)) {
						ViewBuilder.showAlert("添加成功");
						mTfName.setText("");
						if (mUserStage != null) {
							mUserStage.initData();
						}
					} else {
						ViewBuilder.showAlert("添加失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ViewBuilder.showAlert("添加失败");
				}
			}
		});
	}

	public void show() {
		mStage.show();
	}

}

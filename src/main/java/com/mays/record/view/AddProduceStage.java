package com.mays.record.view;

import java.time.LocalDate;
import java.util.List;

import com.mays.record.dao.ProduceDao;
import com.mays.record.dao.UserDao;
import com.mays.record.util.Constant;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * 添加生产记录窗口
 * 
 * @author mays
 * @date 2018年4月17日
 */
public class AddProduceStage {

	private ProduceStage mProduceStage;
	private GridPane mGrid;
	private Stage mStage;
	private ChoiceBox<String> mCbType;
	private List<String> workerList;
	private ChoiceBox<String> mCbWorker;
	private TextField mTfNum;
	private DatePicker mDatePicker;
	private Button mBtnAdd;

	public AddProduceStage(ProduceStage produceStage) {
		mProduceStage = produceStage;
		mGrid = new GridPane();
		// mGrid.setGridLinesVisible(true);
		mGrid.setHgap(5);
		mGrid.setVgap(15);
		mGrid.setAlignment(Pos.CENTER);
		mStage = new Stage();
		mStage.setTitle("添加生产记录");
		mStage.setScene(new Scene(mGrid, 400, 400));
		initData();
		initView();
		initListener();
	}

	private void initData() {
		workerList = UserDao.selectUserByType(0);
	}

	private void initView() {
		ColumnConstraints col1 = new ColumnConstraints(90);
		ColumnConstraints col2 = new ColumnConstraints(100, 150, 300);
		mGrid.getColumnConstraints().addAll(col1, col2);

		Label llWorker = ViewBuilder.getLabelRight("员工：");
		Label llType = ViewBuilder.getLabelRight("种类：");
		Label llNum = ViewBuilder.getLabelRight("包数：");
		Label llTime = ViewBuilder.getLabelRight("时间：");
		mGrid.addColumn(0, llWorker, llType, llNum, llTime);

		mCbWorker = new ChoiceBox<>(FXCollections.observableArrayList(workerList));
		mCbWorker.setValue(workerList.get(0));
		mCbType = new ChoiceBox<>(FXCollections.observableArrayList(Constant.TYPES));
		mCbType.setValue(Constant.TYPES[0]);
		mTfNum = ViewBuilder.getTextFieldNumber();
		mTfNum.setPrefColumnCount(150);
		mDatePicker = new DatePicker(LocalDate.now());
		mGrid.addColumn(1, mCbWorker, mCbType, mTfNum, mDatePicker);

		mBtnAdd = ViewBuilder.getButton("添加");
		GridPane.setHalignment(mBtnAdd, HPos.CENTER);
		mGrid.add(mBtnAdd, 0, 5, 2, 1);
	}

	private void initListener() {
		mBtnAdd.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				add();
			}
		});
	}

	private void add() {
		if (mTfNum.getText().isEmpty()) {
			ViewBuilder.showAlert("请填写包数！");
			return;
		}
		if (mDatePicker.getValue() == null) {
			ViewBuilder.showAlert("请填写时间！");
			return;
		}
		try {
			if (1 == ProduceDao.insertProduceInfo(mCbType.getValue(), Integer.valueOf(mTfNum.getText()),
					mCbWorker.getValue(), mDatePicker.getValue().toString())) {
				ViewBuilder.showAlert("添加成功");
				mTfNum.setText("");
				// 添加成功调用查询
				if (mProduceStage != null) {
					mProduceStage.addSuccess();
				}
			} else {
				ViewBuilder.showAlert("添加失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ViewBuilder.showAlert("程序异常");
		}
	}

	public void show() {
		mStage.show();
	}

}

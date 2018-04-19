package com.mays.record.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.mays.record.dao.SalesDao;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * 添加销售记录窗口
 * 
 * @author mays
 * @date 2018年4月19日
 */
public class AddSalesStage {

	private SalesStage mSalesStage;
	private GridPane mGrid;
	private Stage mStage;
	private ChoiceBox<String> mCbType;
	private List<String> buyerList;
	private ChoiceBox<String> mCbBuyer;
	private TextField mTfWeight;
	private DatePicker mDatePicker;
	private Button mBtnAdd;

	public AddSalesStage(SalesStage salesStage) {
		mSalesStage = salesStage;
		mGrid = new GridPane();
//		 mGrid.setGridLinesVisible(true);
		mGrid.setHgap(5);
		mGrid.setVgap(15);
		mGrid.setAlignment(Pos.CENTER);
		mStage = new Stage();
		mStage.setTitle("添加销售记录");
		mStage.setScene(new Scene(mGrid, 400, 400));
		initData();
		initView();
		initListener();
	}

	private void initData() {
		buyerList = UserDao.selectUserByType(1);
	}

	private void initView() {
		ColumnConstraints col1 = new ColumnConstraints(100);
		ColumnConstraints col2 = new ColumnConstraints(100, 150, 300);
		mGrid.getColumnConstraints().addAll(col1, col2);

		mGrid.addColumn(0, ViewBuilder.getLabelRight("客户："), ViewBuilder.getLabelRight("种类："),
				ViewBuilder.getLabelRight("重量(吨)："), ViewBuilder.getLabelRight("时间："));

		mCbBuyer = new ChoiceBox<>(FXCollections.observableArrayList(buyerList));
		mCbBuyer.setValue(buyerList.get(0));
		mCbType = new ChoiceBox<>(FXCollections.observableArrayList(Constant.TYPES));
		mCbType.setValue(Constant.TYPES[0]);
		mTfWeight = ViewBuilder.getTextFieldNumber();
		mTfWeight.setPrefColumnCount(150);
		mDatePicker = new DatePicker(LocalDate.now());
		mGrid.addColumn(1, mCbBuyer, mCbType, mTfWeight, mDatePicker);

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
		if (mTfWeight.getText().isEmpty()) {
			ViewBuilder.showAlert("请填写重量！");
			return;
		}
		BigDecimal weight;
		try {
			weight = new BigDecimal(mTfWeight.getText()).multiply(new BigDecimal(1000));
		} catch (Exception e) {
			ViewBuilder.showAlert("重量格式不正确");
			return;
		}
		if (mDatePicker.getValue() == null) {
			ViewBuilder.showAlert("请填写时间！");
			return;
		}
		try {
			if (1 == SalesDao.insertSalesInfo(mCbType.getValue(), weight.intValue(),
					mCbBuyer.getValue(), mDatePicker.getValue().toString())) {
				ViewBuilder.showAlert("添加成功");
				mTfWeight.setText("");
				// 添加成功调用查询
				if (mSalesStage != null) {
					mSalesStage.addSuccess();
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

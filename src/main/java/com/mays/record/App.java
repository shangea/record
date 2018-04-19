package com.mays.record;

import java.io.IOException;

import com.mays.record.dao.ProduceDao;
import com.mays.record.dao.SalesDao;
import com.mays.record.dao.UserDao;
import com.mays.record.util.DBUtil;
import com.mays.record.view.ProduceStage;
import com.mays.record.view.SalesStage;
import com.mays.record.view.UserStage;
import com.mays.record.view.ViewBuilder;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * 程序主入口
 * @author	mays
 * @date	2018年4月11日
 */
public class App extends Application implements EventHandler<ActionEvent> {

	private Button mBtnProduce;
	private Button mBtnSales;
	private Button mBtnUser;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initMysql();
		//布局设置
		GridPane grid = new GridPane();
//		grid.setGridLinesVisible(true);
		grid.setPadding(new Insets(20));
		grid.setHgap(20);
		grid.setVgap(40);
		grid.setAlignment(Pos.CENTER);
		mBtnProduce = ViewBuilder.getBigButton("生产记录");
		grid.add(mBtnProduce, 0, 0);
		mBtnSales = ViewBuilder.getBigButton("销售记录");
		grid.add(mBtnSales, 0, 1);
		mBtnUser = ViewBuilder.getBigButton("人员管理");
		grid.add(mBtnUser, 0, 2);
		//点击事件
		mBtnProduce.setOnAction(this);
		mBtnSales.setOnAction(this);
		mBtnUser.setOnAction(this);
		//窗口显示
		primaryStage.setTitle("P&S - by mays");
		primaryStage.setScene(new Scene(grid, 320, 480));
		primaryStage.show();
	}

	/**
	 * 初始化数据库
	 * @author	mays
	 * @date	2018年4月12日
	 */
	public void initMysql() {
		try {
			DBUtil.init();
			UserDao.init();
			ProduceDao.init();
			SalesDao.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(ActionEvent event) {
		if (mBtnProduce.equals(event.getSource())) {
			//打开'生产记录'窗口
			new ProduceStage().show();
		} else if (mBtnSales.equals(event.getSource())) {
			//打开'销售记录'窗口
			new SalesStage().show();
		} else if (mBtnUser.equals(event.getSource())) {
			//打开'人员管理'窗口
			new UserStage().show();
		}
	}

}

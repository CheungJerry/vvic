package com.mofong.vvic.bean;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Log {
	private static final String INFO = "INFO";

	private static final String ERROR = "ERROR";

	private String status;

	private String operation;

	private String operation_detail;

	private Timestamp operate_time;

	private Log(String status) {
		this.status = status;
		this.operate_time = Timestamp.valueOf(LocalDateTime.now());
	}

	public String getStatus() {
		return this.status;
	}

	public String getOperation() {
		return this.operation;
	}

	public com.mofong.vvic.bean.Log setOperation(String operation) {
		this.operation = operation;
		return this;
	}

	public String getOperation_detail() {
		return this.operation_detail;
	}

	public com.mofong.vvic.bean.Log setOperation_detail(String operation_detail) {
		this.operation_detail = operation_detail;
		return this;
	}

	public Timestamp getOperate_time() {
		return this.operate_time;
	}

	public static com.mofong.vvic.bean.Log info() {
		return new com.mofong.vvic.bean.Log("INFO");
	}

	public static com.mofong.vvic.bean.Log error() {
		return new com.mofong.vvic.bean.Log("ERROR");
	}
}

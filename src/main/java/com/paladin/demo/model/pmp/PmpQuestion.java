package com.paladin.demo.model.pmp;

import javax.persistence.Id;

public class PmpQuestion {

	// 
	@Id
	private String groupName;

	// 
	@Id
	private Integer indexNum;

	// 
	private String questionE;

	// 
	private String questionC;

	// 
	private String answerAE;

	// 
	private String answerBE;

	// 
	private String answerCE;

	// 
	private String answerDE;

	// 
	private String answerAC;

	// 
	private String answerBC;

	// 
	private String answerCC;

	// 
	private String answerDC;

	// 
	private String rightAnswer;
	
	//
	private String answerAnalysis;


	public String getQuestionE() {
		return questionE;
	}

	public void setQuestionE(String questionE) {
		this.questionE = questionE;
	}

	public String getQuestionC() {
		return questionC;
	}

	public void setQuestionC(String questionC) {
		this.questionC = questionC;
	}

	public String getAnswerAE() {
		return answerAE;
	}

	public void setAnswerAE(String answerAE) {
		this.answerAE = answerAE;
	}

	public String getAnswerBE() {
		return answerBE;
	}

	public void setAnswerBE(String answerBE) {
		this.answerBE = answerBE;
	}

	public String getAnswerCE() {
		return answerCE;
	}

	public void setAnswerCE(String answerCE) {
		this.answerCE = answerCE;
	}

	public String getAnswerDE() {
		return answerDE;
	}

	public void setAnswerDE(String answerDE) {
		this.answerDE = answerDE;
	}

	public String getAnswerAC() {
		return answerAC;
	}

	public void setAnswerAC(String answerAC) {
		this.answerAC = answerAC;
	}

	public String getAnswerBC() {
		return answerBC;
	}

	public void setAnswerBC(String answerBC) {
		this.answerBC = answerBC;
	}

	public String getAnswerCC() {
		return answerCC;
	}

	public void setAnswerCC(String answerCC) {
		this.answerCC = answerCC;
	}

	public String getAnswerDC() {
		return answerDC;
	}

	public void setAnswerDC(String answerDC) {
		this.answerDC = answerDC;
	}

	public String getRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(String rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	public String getAnswerAnalysis() {
		return answerAnalysis;
	}

	public void setAnswerAnalysis(String answerAnalysis) {
		this.answerAnalysis = answerAnalysis;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getIndexNum() {
		return indexNum;
	}

	public void setIndexNum(Integer indexNum) {
		this.indexNum = indexNum;
	}

}
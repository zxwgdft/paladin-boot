package com.paladin.demo.model.pmp;

import javax.persistence.Id;

public class PmpTest {

	// 
	@Id
	private String questionGroup;

	// 
	@Id
	private Integer questionIndex;

	// 
	@Id
	private String userId;

	// 
	private String answer;

	public String getQuestionGroup() {
		return questionGroup;
	}

	public void setQuestionGroup(String questionGroup) {
		this.questionGroup = questionGroup;
	}

	public Integer getQuestionIndex() {
		return questionIndex;
	}

	public void setQuestionIndex(Integer questionIndex) {
		this.questionIndex = questionIndex;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
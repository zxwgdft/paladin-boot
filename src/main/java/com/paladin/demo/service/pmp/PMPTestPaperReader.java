package com.paladin.demo.service.pmp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.paladin.demo.model.pmp.PmpQuestion;

public class PMPTestPaperReader {

	private BufferedReader questionReader;
	private BufferedReader answerReader;

	private String group;

	public PMPTestPaperReader(String group, String questionFile, String answerFile) throws IOException {
		questionReader = new BufferedReader(new FileReader(questionFile));
		answerReader = new BufferedReader(new FileReader(answerFile));
		this.group = group;
	}

	private boolean read = false;
	private List<PmpQuestion> questions;

	public List<PmpQuestion> read() throws IOException {
		if (!read) {
			questions = readQuestion();
			read = true;
		}

		return questions;
	}

	private String cacheLine = "";

	private List<PmpQuestion> readQuestion() throws IOException {

		List<PmpQuestion> qs = new ArrayList<>(200);

		for (int i = 1; i <= 200;) {
			PmpQuestion pq = new PmpQuestion();

			pq.setGroupName(group);
			pq.setIndexNum(i);

			String is = i + ".";

			String qe = readUntil(questionReader, is, "A.", false);
			String aae = readUntil(questionReader, "A.", "B.", true);
			String abe = readUntil(questionReader, "B.", "C.", true);
			String ace = readUntil(questionReader, "C.", "D.", true);
			String ade = readUntil(questionReader, "D.", is, false);

			String qc = readUntil(questionReader, is, "A.", false);
			String aac = readUntil(questionReader, "A.", "B.", true);
			String abc = readUntil(questionReader, "B.", "C.", true);
			String acc = readUntil(questionReader, "C.", "D.", true);
			String adc = readUntil(questionReader, "D.", (++i) + ".", false);

			if (qc.length() == 0 || aac.length() == 0 || abc.length() == 0 || acc.length() == 0 || adc.length() == 0) {
				System.currentTimeMillis();
			}
			
			if(qc.equals("")) {
				System.out.println(i);
				System.currentTimeMillis();
			}

			pq.setQuestionE(qe);
			pq.setQuestionC(qc);
			pq.setAnswerAC(aac);
			pq.setAnswerAE(aae);
			pq.setAnswerBC(abc);
			pq.setAnswerBE(abe);
			pq.setAnswerCC(acc);
			pq.setAnswerCE(ace);
			pq.setAnswerDC(adc);
			pq.setAnswerDE(ade);

			qs.add(pq);
		}

		cacheLine = "";
		readAnswer(qs);
		return qs;
	}

	private String readUntil(BufferedReader reader, String beginStr, String endStr, boolean sameline) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line = cacheLine;

		do {
			line = line.trim();
			if (line.equals("")) {
				continue;
			}

			if (line.startsWith(beginStr)) {
				if (sameline) {
					int index = line.indexOf(endStr);
					if (index != -1) {
						sb.append(line.substring(beginStr.length(), index));
						cacheLine = line.substring(index);
						break;
					}
				}

				sb.append(line.substring(beginStr.length()));
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.equals("")) {
						continue;
					}

					if (line.startsWith(endStr)) {
						cacheLine = line;
						break;
					}

					sb.append(line);
				}

				break;
			}
		} while ((line = reader.readLine()) != null);

		return sb.toString();
	}

	private String readAnswerUntil(BufferedReader reader, int index) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line = cacheLine;

		String beginStr = index + "";
		String endStr = index + 1 + "";

		do {
			line = line.trim();
			if (line.equals("")) {
				continue;
			}

			if (line.startsWith(beginStr)) {
				sb.append(line.substring(beginStr.length() + 1));
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.equals("")) {
						continue;
					}

					if (line.startsWith(endStr)) {
						cacheLine = line;
						break;
					}

					sb.append(line);
				}

				break;
			}
		} while ((line = reader.readLine()) != null);

		return sb.toString();
	}

	private void readAnswer(List<PmpQuestion> questions) throws IOException {
		for (int i = 1; i <= 200; i++) {
			PmpQuestion pq = questions.get(i - 1);

			String an = readAnswerUntil(answerReader, i);

			an = an.substring(an.indexOf("解析") + 2).trim();

			String answer = an.substring(1, 2);
			String analysis = an.substring(3).trim();

			pq.setRightAnswer(answer);
			pq.setAnswerAnalysis(analysis);
		}
	}

	
	public static void main(String[] args) throws IOException {
		PMPTestPaperReader reader = new PMPTestPaperReader("测试七","D:/1912复习测试题七.txt","D:/解析-1912复习测试七.txt");
		List<PmpQuestion> pqs = reader.read();
		System.out.println(pqs);
	}
	
}

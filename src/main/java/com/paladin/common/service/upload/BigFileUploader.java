package com.paladin.common.service.upload;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paladin.framework.core.exception.BusinessException;

public class BigFileUploader {

	public static final int UPLOAD_COMPLETED = 9;
	public static final int UPLOAD_REUPLOAD = 8;
	public static final int UPLOAD_ERROR = 2;
	public static final int UPLOAD_SUCCESS = 1;

	private String id;
	private int chunkCount;
	private long chunkSize;
	private String storagePath;
	private String filePath;
	private long lastUpdateTime;
	private String fileName;

	private volatile boolean completed;

	public BigFileUploader(String id, int chunkCount, long chunkSize, String storagePath, String fileName) {
		if (id == null || id.trim().length() == 0) {
			throw new BusinessException("大文件上传ID不能为空");
		}

		if (fileName == null || fileName.trim().length() == 0) {
			throw new BusinessException("文件名称不能为空");
		}

		if (chunkCount <= 0) {
			throw new BusinessException("大文件上传块数必须大于0");
		}

		this.id = id.trim();
		this.chunkCount = chunkCount;
		this.chunkSize = chunkSize;
		this.storagePath = storagePath;
		this.fileName = fileName.trim();

		initialize();
	}

	private void initialize() {
		Path path = null;
		try {
			storagePath = storagePath + id + "/";
			path = Paths.get(storagePath);
			if (Files.notExists(path)) {
				Files.createDirectory(path);
			} else if (!Files.isDirectory(path)) {
				Files.deleteIfExists(path);
				Files.createDirectory(path);
			}
		} catch (FileAlreadyExistsException e1) {
			if (!Files.isDirectory(path)) {
				try {
					Files.deleteIfExists(path);
					Files.createDirectory(path);
				} catch (IOException e) {
					throw new BusinessException("创建临时文件失败", e);
				}
			}
		} catch (IOException e) {
			throw new BusinessException("创建临时文件失败", e);
		}

		filePath = storagePath + fileName;
		if (Files.exists(Paths.get(filePath))) {
			throw new BusinessException("该文件已经上传过");
		}

		lastUpdateTime = System.currentTimeMillis();
	}

	public static boolean existFile(String id, String storagePath, String fileName) {
		return Files.exists(Paths.get(storagePath + get(id, fileName)));
	}

	public static String get(String id, String fileName) {
		return id + "/" + fileName;
	}

	private Map<Integer, FileChunk> fileChunkMap = new HashMap<>();

	public boolean checkFileChunk(int index) {
		FileChunk chunk = fileChunkMap.get(index);
		return chunk != null && chunk.uploaded;
	}

	public int uploadFileChunk(int index, byte[] data) {
		if (completed) {
			return UPLOAD_COMPLETED;
		}

		if (index < 0) {
			return UPLOAD_ERROR;
		}

		FileChunk chunk = null;
		synchronized (fileChunkMap) {
			chunk = fileChunkMap.get(index);
			if (chunk == null) {
				chunk = new FileChunk();
				chunk.setIndex(index);
				fileChunkMap.put(index, chunk);
			}
		}

		synchronized (chunk) {
			if (!chunk.uploaded) {
				File tmpFile = new File(filePath + ".temp");
				try (RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw")) {
					long offset = chunkSize * chunk.getIndex();
					accessTmpFile.seek(offset);
					accessTmpFile.write(data);

					chunk.setUploaded(true);
					lastUpdateTime = System.currentTimeMillis();
				} catch (IOException e) {
					e.printStackTrace();
					return UPLOAD_ERROR;
				}
				checkComplete();
			}
		}

		return UPLOAD_SUCCESS;
	}

	private void checkComplete() {
		synchronized (fileChunkMap) {
			if (completed) {
				return;
			}

			boolean b = true;
			for (int i = 0; i < chunkCount; i++) {
				FileChunk chunk = fileChunkMap.get(i);
				if (chunk == null || !chunk.uploaded) {
					b = false;
					break;
				}
			}

			if (b) {
				File tmpFile = new File(filePath + ".temp");
				File newFile = new File(filePath);
				tmpFile.renameTo(newFile);
				completed = true;
			}
		}
	}

	public boolean isCompleted() {
		return completed;
	}

	public List<Integer> getUploadedChunk() {
		List<Integer> result = new ArrayList<>(fileChunkMap.size());
		for (FileChunk fc : fileChunkMap.values()) {
			if (fc.uploaded) {
				result.add(fc.index);
			}
		}
		return result;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public static class FileChunk {
		private int index;
		private boolean uploaded;

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public boolean isUploaded() {
			return uploaded;
		}

		public void setUploaded(boolean uploaded) {
			this.uploaded = uploaded;
		}
	}

}

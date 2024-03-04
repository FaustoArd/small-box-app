package com.lord.small_box.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.google.cloud.documentai.v1.Document;
import com.google.protobuf.Descriptors.FieldDescriptor;

public interface DocumentAIService {
	
	public String quickStart(String projectId, String location, String processorId, String filePath)throws IOException, InterruptedException, ExecutionException, TimeoutException;
	
	public String getText(Document.TextAnchor textAnchor,String text);
	
	public String getParsedDocument();

}

package com.lord.small_box.services_impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.documentai.v1.Document;
import com.google.cloud.documentai.v1.Document.TextAnchor;
import com.google.cloud.documentai.v1.DocumentProcessorServiceClient;
import com.google.cloud.documentai.v1.DocumentProcessorServiceSettings;
import com.google.cloud.documentai.v1.ProcessRequest;
import com.google.cloud.documentai.v1.ProcessResponse;
import com.google.cloud.documentai.v1.RawDocument;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.lord.small_box.dtos.ReceiptDto;
import com.lord.small_box.services.DocumentAIService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentAIServiceImpl {
	
	
	

	public Multimap<String, String> quickStart() throws IOException, InterruptedException, ExecutionException, TimeoutException {

		String projectId = "";
		String location = "us";
		String processorId = "";
		String filePath = "D:\\filetest\\oeste-receipt-test1.pdf";
		String endpoint = String.format("%s-documentai.googleapis.com:443", location);
		
		DocumentProcessorServiceSettings settings = DocumentProcessorServiceSettings.newBuilder().setEndpoint(endpoint)
				.build();
		try (DocumentProcessorServiceClient client = DocumentProcessorServiceClient.create(settings)) {
			String name = String.format("", projectId,
					location, processorId);

			// Read the file.
			byte[] imageFileData = Files.readAllBytes(Paths.get(filePath));

			// Convert the image data to a Buffer and base64 encode it.
			ByteString content = ByteString.copyFrom(imageFileData);

			// Configure the process request.
			RawDocument document = RawDocument.newBuilder().setContent(content).setMimeType("application/pdf").build();
			ProcessRequest request = ProcessRequest.newBuilder().setName(name).setRawDocument(document).build();

			// Recognizes text entities in the PDF document
			ProcessResponse result = client.processDocument(request);
			Document documentResponse = result.getDocument();
			List<Document.Entity> entList = documentResponse.getEntitiesList();
			
			//List<String> strList = entList.stream().map(e -> e.getType() + " "  + e.getMentionText()).toList();
			
			 
			 Multimap<String, String> mapEntity = HashMultimap.create();
			  entList.stream().forEach(e -> {
				mapEntity.put(e.getType(), e.getMentionText());
			});
			 
			  return mapEntity;
		
			// Get all of the document text as one big string
			/*String text = documentResponse.getText();*/

			// Read the text recognition output from the processor

			/*Document.Page firstPage = documentResponse.getPages(0);
			List<Document.Page.Paragraph> paragraphs = firstPage.getParagraphsList();*/

			/*String paragraphText = "";
			for (Document.Page.Paragraph paragraph : paragraphs) {
				paragraphText = paragraphText + getText(paragraph.getLayout().getTextAnchor(), text);
			}*/

			
		}

	}
	
	Map<FieldDescriptor, Object> mapToMap(FieldDescriptor fieldDescriptor, Object object){
		Map<FieldDescriptor, Object> result = new HashMap<>();
		result.put(fieldDescriptor, result);
		return result;
	}

	// Extract shards from the text field

	public String getText(TextAnchor textAnchor, String text) {
		if (textAnchor.getTextSegmentsList().size() > 0) {
			int startIdx = (int) textAnchor.getTextSegments(0).getStartIndex();
			int endIdx = (int) textAnchor.getTextSegments(0).getEndIndex();
			return text.substring(startIdx, endIdx);
		}
		return "[NO TEXT]";
	}

	/*
	 * public String getParsedDocument() { try { String result =
	 * quickStart(projectId, location, processorId, filePath); return result; }
	 * catch (IOException | InterruptedException | ExecutionException |
	 * TimeoutException e) { // TODO Auto-generated catch block e.printStackTrace();
	 * } return null; }
	 */

}

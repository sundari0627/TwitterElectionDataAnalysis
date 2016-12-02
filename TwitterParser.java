import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class TwitterParser {
	static String name=null;
	static String text=null;
	static String language="";
	static boolean text_first=false;
	static boolean retweeted_flag=false;
	static boolean retweete_text=false;
	static boolean lan=false;
	
	static boolean outerFlag=false;
	static boolean innerFlag=false;
	static boolean userFlag=false;
	static boolean userEnd=false;
	static boolean defaultProfileFlag=false;
	static String defaultProfileValue="";
	static int id=0;
	
	static FileWriter output=null;
	
	public static void main(String ar[]) throws IOException{
		final File folder = new File("/home/polu1t/Twitter-Data");
		output = new FileWriter("/home/polu1t/JsonParser/TwitterData.csv");
		listFilesForFolder(folder);
		output.close();
	}
	public static void  listFilesForFolder(final File folder) {
		String filename="" ;
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	        	filename=fileEntry.getName();
	        	
	        	try {
					main1(folder+"/"+filename);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}finally{
					
//					System.out.println("*****************************");
				}
	        }
	    }
	}
	public static void main1(String filename) throws IOException {
		
		JsonFactory factory = new JsonFactory();
		
		File f1=new File(filename);
		System.out.println(f1.getAbsolutePath());
		JsonParser parser=factory.createParser(f1);
		
		while (!parser.isClosed()) {
			// read the next element
			JsonToken token = parser.nextToken();
			//         // if the call to nextToken returns null, the end of the file has been reached
			if (token == null)
				break;

			// Process the element
			String indent = "";
			String result = "";
			
			processJSONValue(token, parser, indent);
			if((language !=null)&& language.equalsIgnoreCase("en")){
				if(((name!=null) && (text!= null&&text.trim().length()>0))){
//					System.out.println(name+","+text);
					output.write(name+","+text.replaceAll("[,\r\n]", " ")+"\n");
					
					name=null;
					text=null;
				}				
			}
			outerFlag=false;
			innerFlag=false;
			text_first=false;
			retweeted_flag=false;
			retweete_text=false;
			userFlag=false;
			userEnd=true;
			id=0;
			lan=false;
			defaultProfileFlag=false;
			defaultProfileValue="";
			name=null;
			text=null;
		}   
	}
	private static void processJSONValue (JsonToken token, JsonParser parser, String indent)
			throws IOException {
			
		 if (JsonToken.START_OBJECT.equals(token)) {
			id++;
			processJSONObject(parser, indent);
		}
		else if (JsonToken.START_ARRAY.equals(token)) {
			processJSONArray(parser, indent);
		} 
	
	}
	private static void processJSONObject(JsonParser parser, String indent)
			throws IOException {
		
		indent += " ";
		try{
			while (!parser.isClosed()) {
				JsonToken token = parser.nextToken();
				if (JsonToken.END_OBJECT.equals(token)) {
					//The end of the JSON object has been reached
					
					if(userFlag){
						userEnd=true;
					}
					id--;
					break;
				}
				if (!JsonToken.FIELD_NAME.equals(token)) {
					System.out.println("Error. Expected a field name");
					break;
				}
	
				token = parser.nextToken();
				if(id==1&& parser.getCurrentName().equalsIgnoreCase("lang")){
					language=parser.getValueAsString();
					if(language.equalsIgnoreCase("en")){
						lan=true;
					}
					else
						lan=false;
				}
				
				if(parser.getCurrentName().equalsIgnoreCase("user")){
					userFlag=true;
					userEnd=false;
				}

//				if(userFlag&&!userEnd&&parser.getCurrentName().equalsIgnoreCase("default_profile")){
//					defaultProfileValue=parser.getValueAsString();
//					if(defaultProfileValue!=null&&defaultProfileValue.trim().length()>0&&defaultProfileValue.equalsIgnoreCase("true")){
//						defaultProfileFlag=true;
//					}
//				}
//				if(userFlag&&!userEnd&&defaultProfileFlag&&parser.getCurrentName().equalsIgnoreCase("screen_name")){
				if(userFlag&&!userEnd&&parser.getCurrentName().equalsIgnoreCase("screen_name")){
					name=parser.getValueAsString();
					defaultProfileFlag=false;
					
				}
				else if(lan  && parser.getCurrentName().equalsIgnoreCase("text")){
					text = parser.getValueAsString();
					lan=false;
//					text_first=true;
				}			
				processJSONValue(token, parser, indent);			
			}
		}

		catch(Exception e){
			System.out.println("Error occured"+e.getMessage());
		}
	}

	private static void processJSONArray (JsonParser parser, String margin)
			throws IOException {
		
		margin += " ";
		while (!parser.isClosed()) {
			JsonToken token = parser.nextToken();
			if (JsonToken.END_ARRAY.equals(token)) {
				// The en of the array has been reached
				break;
			}
			processJSONValue(token, parser, margin);
		}
	}

} 

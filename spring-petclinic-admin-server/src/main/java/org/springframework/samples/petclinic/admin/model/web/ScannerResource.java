package org.springframework.samples.petclinic.admin.model.web;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.samples.petclinic.admin.model.Alert;
import org.springframework.samples.petclinic.admin.model.ScannerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import lombok.RequiredArgsConstructor;

@RequestMapping("/getreport")
@RestController
@RequiredArgsConstructor
public class ScannerResource {
	
	private final static String USER_AGENT = "Mozilla/5.0";
	private static final String ZAP_ADDRESS = "localhost";
	private static final int ZAP_PORT = 8181;
	private static final String ZAP_API_KEY = null; 	
    private final ScannerRepository scannerRepository;

	
	 @GetMapping
	    public List<Alert> showResourcesAlertList() {
	        return scannerRepository.findAll();
	    }
	 
	 public static void preRegistrationSecurityTest(String swaggerUrl) throws JSONException, ClientApiException{

//			String url = "http://10.0.75.1:2222";
			ClientApi api2 = new ClientApi(ZAP_ADDRESS, ZAP_PORT);
			String reportAggregator = "http://localhost:8081/EventService02/getreport";
			
		
			DefaultHttpClient client = new DefaultHttpClient();
			System.out.println("starting ...... ");
			Map<String, String> map = new HashMap<>();
			 map.put("url", swaggerUrl+"/v2/api-docs");
			ApiResponse openApiResp = 
					api2.callApi("openapi", "action", "importUrl", map); //importUrl
			System.out.println("exploring the api for using OpenAPI " + swaggerUrl);
			

			String scanResult = null;
			System.out.println("prepping to test " + swaggerUrl);
			try {
				// Start spidering the target TODO - fetch the OpenAPI and scan the target application
				System.out.println("Spider : " + swaggerUrl);
				ApiResponse resp = api2.spider.scan(ZAP_API_KEY, swaggerUrl, null, null, null, null);
				String scanid;
				int progress;

				scanid = ((ApiResponseElement) resp).getValue();
				System.out.println("Scan Id for spiderring : => " + scanid);
			

				// Poll the status until it completes
				while (true) {
					Thread.sleep(1000);
					progress = Integer.parseInt(((ApiResponseElement) api2.spider.status(scanid)).getValue());
					System.out.println("Spider progress : " + progress + "%");
					if (progress >= 100) {
						break;
					}
				}
				System.out.println("Spider complete");

				// Give the passive scanner a chance to complete
				Thread.sleep(2000);
				

				System.out.println("Active scan : " + swaggerUrl);
				resp = api2.ascan.scan(swaggerUrl, null, null, null, null, null);

				// The scan now returns a scan id to support concurrent scanning
				scanid = ((ApiResponseElement) resp).getValue(); //getValue();
				System.out.println("Scan Id active scan : => " + scanid );

				// Poll the status until it completes
				while (true) {
					Thread.sleep(5000);
					progress = Integer.parseInt(((ApiResponseElement) api2.ascan.status(scanid)).getValue());
					System.out.println("Active Scan progress : " + progress + "%");
					if (progress >= 100) {
						break;
					}
				}
				System.out.println("Active Scan complete");
				
				
				// trigger the scan report retrieval		
				HttpPost post = new HttpPost(reportAggregator);
				post.addHeader("User-Agent", USER_AGENT);
				JSONObject objecty = new JSONObject();
				objecty.put("target", swaggerUrl);//http://localhost:8761/
				String request = objecty.toString();
				
				
				StringEntity input = null;
				 
				try {
					input = new StringEntity(request);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				input.setContentType(MediaType.APPLICATION_JSON);
				post.setEntity(input); 

				HttpResponse response = client.execute(post);	
				System.out.println("\nSending 'POST' request to URL : " + reportAggregator);
				System.out.println("Post parameters : " + post.getEntity());
				System.out.println("Response Code : " +
		                                    response.getStatusLine().getStatusCode());

				BufferedReader rd = new BufferedReader(
		                        new InputStreamReader(response.getEntity().getContent()));

				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}

				System.out.println(result.toString());
			} catch (Exception e) {
				System.out.println("Exception : " + e.getMessage());
				e.printStackTrace();
			}
		}
	 
	 
	 
	 @GetMapping
	 public Response scanner(String target) throws ClientProtocolException, IOException, ParseException, JSONException {

			
			System.out.println("got message for posting : -- " + target);
			getJsonRReport(target);
			String scanResult = "fine";
			// TODO - logic for comparing the results with the company security
			// policy and determining whether to allow the service to be registered
			// or not .i.e. boolean response

			return Response.status(200).entity(scanResult).build();

		}
	 
	 public static void getJsonRReport(String intarget) throws ClientProtocolException, IOException, ParseException, JSONException{

		 //TODO integrate Spring data or dataaccess for getting reports/alerts
		 //			ScannerDAO.em.getTransaction().begin();

			
			
			JSONObject object = new JSONObject(intarget);
			String target = object.getString("target");
			
			System.out.println("----------------   getting the report for " + target + "-----------------------");
//			String target = "localhost:8761";
			String reportUrl = "http://" + ZAP_ADDRESS + ":" + ZAP_PORT+  "/JSON/core/view/alerts/baseurl=" + target + "&start=&count=";
			System.out.println(reportUrl);
//			String url = "http://localhost:8080/greeting";
			
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(reportUrl);
			
			
			// add a request header
			request.addHeader("user-agent", USER_AGENT);
			HttpResponse response = client.execute(request);
			
			// get your response status code 
			System.out.println("Response from ZAP reports endpoint: " + response.getStatusLine().getStatusCode());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			StringBuffer result = new StringBuffer();
			String line = "";
			
			
//			ObjectMapper mapper = new ObjectMapper();
//			ZapJsonReport zapJsonReport = new ZapJsonReport (); 
//			List<Alerts> alerts ;
////			Alerts [] al ;
//			while((line = reader.readLine()) != null) {
//				result.append(line);
//				System.out.println("Here is the report from ZAP reports endpoint :  \n" +  line);
//				zapJsonReport = mapper.readValue(line, ZapJsonReport.class);
////				List<JSONObject> jsonReport = ReadJSON(zapJsonReport.toString() ,"UTF-8");
////				System.out.println("======  persisting the report for target  "  + jsonReport );
//				
//			
//			
////				System.out.println(zapJsonReport.toString());
//				//			zapJsonReport = mapper.readValue(response.getEntity().getContent(), ZapJsonReport.class);
////				 List<Alerts> asList = mapper.readValue(response.getEntity().getContent(), List.class);
////				 List<Alerts> asList = mapper.readValue(response.getEntity().getContent(), List.class);
//
//				Alerts alertR  = new Alerts();
//				alerts = zapJsonReport.getAlerts();
//				int count = 0;
//				for (Alerts aa : alerts) {
//					aa = alerts.get(count);
//				
////					while (count<=alerts.size())
//					
//					
//					System.out.println("Print the alert === " + aa.getAlert());
//					
//				
//				System.out.println("alert2.getAlert()  " +  alerts.size());
//				System.out.println("-------------------- no of alerts " +  alerts.size() + "--------------------------- ");
//		
//				
//
//					alertR.setAlert(aa.getAlert());
//					alertR.setAttack(aa.getAttack());
//					System.out.println("attack : " + aa.getAttack());
//					alertR.setSourceid(aa.getSourceid());
//					alertR.setOther(aa.getOther());
//					alertR.setMethod(aa.getMethod());
//					alertR.setEvidence(aa.getEvidence());
//					System.out.println("Evidence : " + aa.getEvidence());
//					alertR.setPluginId(aa.getPluginId());
//					alertR.setCweid(aa.getCweid());
//					alertR.setConfidence(aa.getConfidence());
//					alertR.setWascid(aa.getWascid());				
//					alertR.setDescription(aa.getDescription());
//					alertR.setMessageId(aa.getMessageId());
//					alertR.setUrl(aa.getUrl());
//					alertR.setReference(aa.getReference());
//					
//					
//					alertR.setSolution(aa.getSolution());
//					alertR.setParam(aa.getParam());
//					alertR.setName(aa.getName());
//					alertR.setRisk(aa.getRisk());
//					alertR.setTimestamp(getTime());				
////					alertR.setSolution(al.getSolution());
//					ScannerDAO.em.persist(alertR);	
//					System.out.println("presisting count : " + count );
//					count++;
//				}
//				
//	}
////				ScannerDAO.em.close();
//				System.out.println("all persisted !!");
////				System.out.println(alert.get);
////				JSONObject obj = new JSONObject(line);
////				System.out.println(zapJsonReport); 
//				
////			}
//			
//			ScannerDAO.em.getTransaction().commit();
//			
			
		}
		
		
		public static synchronized ArrayList<JSONObject> ReadJSON(String liner,String Encoding) throws FileNotFoundException, ParseException {
		    Scanner scn=new Scanner(liner);
		    ArrayList<JSONObject> json=new ArrayList<JSONObject>();
		//Reading and Parsing Strings to Json
		    while(scn.hasNext()){
		        JSONObject obj= (JSONObject) new JSONParser().parse(scn.nextLine());
		        json.add(obj);
		    }
		//Here Printing Json Objects
//		    for(JSONObject obj : json){
//		        System.out.println((String)obj.get("id_user")+" : "+(String)obj.get("level")+" : "+(String)obj.get("text"));
//		    }
		    return json;
		}
		

}

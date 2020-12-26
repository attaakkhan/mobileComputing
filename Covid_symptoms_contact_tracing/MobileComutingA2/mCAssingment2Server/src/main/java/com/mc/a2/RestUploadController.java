package com.mc.a2;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mc.a2.UploadModel;

//import java.awt.PageAttributes.MediaType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

@RestController
public class RestUploadController {

    private final Logger logger = LoggerFactory.getLogger(RestUploadController.class);
    static String userDirectory = System.getProperty("user.dir");

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = userDirectory+"//uploaded_files//";
    
    
    
    
    
    
    
    
    
    
    
    
    @GetMapping("/getGraph")
    // If not @RestController, uncomment this
    //@ResponseBody
    public ResponseEntity<?> getGraph(
           ) throws IOException {
        
//      String prg = "import sys";
//      BufferedWriter out = new BufferedWriter(new FileWriter("p1.py"));
//      out.write(prg);
//      out.close();
      Process p = Runtime.getRuntime().exec("python3 //home//atta//eclipse-workspace//mCAssingment2Server//src/main//resources//p1.py");
      BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String ret = in.readLine();
      System.out.println("value is : "+ret);

      

        return new ResponseEntity("Successfuasdasdsadlly uploaded - "+ret, new HttpHeaders(), HttpStatus.OK);

    }
  
    
    private static final String DIRECTORY = userDirectory+"//uploaded_files//";
    private static final String DEFAULT_FILE_NAME = "contact_graph.txt";
    public static MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        // application/pdf
        // application/xml
        // image/gif, ...
        String mineType = servletContext.getMimeType(fileName);
        try {
            MediaType mediaType = MediaType.parseMediaType(mineType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

 
    @Autowired
    private ServletContext servletContext;
    
 
    // http://localhost:8080/download2?fileName=abc.zip
    // Using ResponseEntity<ByteArrayResource>
    @GetMapping("/contactGraph")
    public ResponseEntity<ByteArrayResource> downloadFile2(
            @RequestParam(defaultValue = DEFAULT_FILE_NAME) String fileName, @RequestParam("date") String date,  @RequestParam("id") String id ) throws IOException, InterruptedException {
 
        MediaType mediaType = getMediaTypeForFileName(this.servletContext, fileName);
        System.out.println("fileName: " + fileName);
        System.out.println("mediaType: " + mediaType);
        System.out.println("date: " + date);
        System.out.println("id: " + id);
        exec(date,id);
        Path path = Paths.get(DIRECTORY + "/" + DEFAULT_FILE_NAME);
        byte[] data = Files.readAllBytes(path);
        ByteArrayResource resource = new ByteArrayResource(data);
 
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                // Content-Type
                .contentType(mediaType) //
                // Content-Lengh
                .contentLength(data.length) //
                .body(resource);
    }
    
    
    
    

    public void exec(String date, String id ) throws IOException, InterruptedException {
//    	String prg = "import sys";
//        BufferedWriter out = new BufferedWriter(new FileWriter("python3 //home//atta//eclipse-workspace//mCAssingment2Server//src/main//resources//p1.py 1 20110620115716"));
//        out.write(prg);
//        out.close();

		  String userDirectory = System.getProperty("user.dir");
    	
    	String[] cmdArray = new String[5];
    	cmdArray[0]="python3";
    	cmdArray[1]=userDirectory+"//python//contact.py";
    	cmdArray[2]=id;
    	cmdArray[4]=userDirectory;
    			cmdArray[3]=date;
    			
    			  
    		        System.out.println(userDirectory);
    			
    			
    			
    			
    			
    			
    			
    			
    			
    			
    			
    			
    			
    			
    			
    			
    			String ret="";
    			
    			
    			String command = "python3 //home//atta//eclipse-workspace//mCAssingment2Server//src//main//resources//p1.py 1 20110620115716";
    		    Process p = Runtime.getRuntime().exec(cmdArray);
    		    p.waitFor();
    		    BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
    		    BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
    		          String line;
    		        while ((line = bri.readLine()) != null) {
    		            ret=ret+line+"\n";
    		          }
    		          bri.close();
    		          while ((line = bre.readLine()) != null) {
    		            System.out.println(line);
    		          }
    		          bre.close();
    		          p.waitFor();
    		          System.out.println("Done.");

    		    p.destroy();
    		    
    		    
    		    
    		    
    		    
    		    try {

    		         File file = new File(userDirectory+"//uploaded_files//contact_graph.txt");
    		       
    		            file.createNewFile();
    		           		         FileWriter fw = new FileWriter(file.getAbsoluteFile());
    		         BufferedWriter bw = new BufferedWriter(fw);
    		         bw.write(ret);
    		         bw.close();
    		         
    		         System.out.println("Done");
    		      } catch (IOException e) {
    		         e.printStackTrace();
    		      } 
    		    
    		    
//        Process p = Runtime.getRuntime().exec(cmdArray);
////    			ProcessBuilder pb=new ProcessBuilder(cmdArray);
////    					Process p=pb.start();
////        while(p.isAlive()) {
////        	
////        	
////        	Thread.sleep(1000);
////        }
////        try {
////			p.();
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//      
//   
//       BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
////        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
////        out.write(prg);
////      	out.close();
//        String s="";
//        String ret="";
//        
//        for (int i=0;i<100000;i++) {
//        	s=in.readLine();
//        	in.
//        	 System.out.println("value**** : "+s);
//
//        	try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        	
        	
        	
        	
        	
        
//        while((in.readLine())!=null){
//        	ret=ret+s;
//            
//     }
       
//        System.out.println("value i	s : "+ret);
//        return new ResponseEntity("Successfully uploaded - "
//                +ret, HttpStatus.OK);
   
    }
    
    
    
    
    
    
    
    

    //Single file upload
    @PostMapping("/api/upload")
    // If not @RestController, uncomment this
    //@ResponseBody
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile uploadfile) {

        System.out.println("Single &^^^^^^^^^^^^^^^^^file upload!");

        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {

            saveUploadedFiles(Arrays.asList(uploadfile));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }

    // Multiple file upload
    @PostMapping("/api/upload/multi")
    public ResponseEntity<?> uploadFileMulti(
            @RequestParam("extraField") String extraField,
            @RequestParam("files") MultipartFile[] uploadfiles) {

        System.out.println("Multiple file upload!");

        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {

            saveUploadedFiles(Arrays.asList(uploadfiles));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - "
                + uploadedFileName, HttpStatus.OK);

    }

    // maps html form to a Model
    @PostMapping("/api/upload/multi/model")
    public ResponseEntity<?> multiUploadFileModel(@ModelAttribute UploadModel model) {

        logger.debug("Multiple file upload! With UploadModel");

        try {

            saveUploadedFiles(Arrays.asList(model.getFiles()));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded!", HttpStatus.OK);

    }

    //save file
    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

        }

    }
}

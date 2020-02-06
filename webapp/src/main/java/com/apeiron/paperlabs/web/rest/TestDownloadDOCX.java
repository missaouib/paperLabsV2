package com.apeiron.paperlabs.web.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
public class TestDownloadDOCX {
	
	
	
	@RequestMapping
	(value = "downloadFile", method = RequestMethod.GET)
    public StreamingResponseBody getSteamingFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"outPutDoc.txt\"");
        InputStream inputStream = new FileInputStream(new File("d:\\outPutDoc.txt"));
        return outputStream -> {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                System.out.println("Writing some bytes..");
                outputStream.write(data, 0, nRead);
            }
        };

    }

}

package org.bonitasoft.demo.esb;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.camel.Exchange;
import org.apache.camel.component.bonita.api.model.FileInput;

public class ExtractFromMail {
    
    public void process(Exchange exchange) throws Exception {
        // the API is a bit clunky so we need to loop
        Map<String, DataHandler> attachments = exchange.getIn().getAttachments();
        HashMap<String, Serializable> bonitaInput = new HashMap<String, Serializable>();
        FileInput file = null;
        if (attachments.size() > 0) {
            for (String name : attachments.keySet()) {
                DataHandler dh = attachments.get(name);
                // get the file name
                String filename = dh.getName();
    
                // get the content and convert it to byte[]
                byte[] data = exchange.getContext().getTypeConverter()
                                  .convertTo(byte[].class, dh.getInputStream());
                file = new FileInput(filename, data);
           }
        }
        if (file != null) {
            bonitaInput.put("document", file);
        }
        bonitaInput.put("subject", (String) exchange.getIn().getHeader("Subject"));
        exchange.getOut().setBody(bonitaInput);
   }
}
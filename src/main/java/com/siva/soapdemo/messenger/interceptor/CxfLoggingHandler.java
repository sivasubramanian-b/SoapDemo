package com.siva.soapdemo.messenger.interceptor;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Set;

public class CxfLoggingHandler implements SOAPHandler<SOAPMessageContext> {

private static final String SOAP_REQUEST_MSG_KEY = "REQ_MSG";

public Set<QName> getHeaders() {
    return Collections.emptySet();
}

public boolean handleMessage(SOAPMessageContext context) {
    Boolean outgoingMessage = (Boolean) context.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    if (outgoingMessage) {
        // it is outgoing message. let's work
        SOAPPart request = (SOAPPart)context.get(SOAP_REQUEST_MSG_KEY);
        String requestString = convertDomToString(request);
        String responseString = convertDomToString(context.getMessage().getSOAPPart());
        String soapActionURI = ((QName)context.get(MessageContext.WSDL_OPERATION)).getLocalPart();
        System.out.println("Action:"+soapActionURI);
        System.out.println("Request:"+requestString); 
        System.out.println("Response:"+responseString); 
        // now you can output your request, response, and ws-operation    
    } else {
        // it is incoming message, saving it for future
        context.put(SOAP_REQUEST_MSG_KEY, context.getMessage().getSOAPPart());
    }
    return true;
}

public boolean handleFault(SOAPMessageContext context) {        
    return handleMessage(context);
}

private String convertDomToString(SOAPPart soap){
    final StringWriter sw = new StringWriter();
    try {
        TransformerFactory.newInstance().newTransformer().transform(
                new DOMSource(soap),
                new StreamResult(sw));
    } catch (TransformerException e) {
        // do something
    }
    return sw.toString();
}

@Override
public void close(MessageContext context) {
	// TODO Auto-generated method stub
	
}
}
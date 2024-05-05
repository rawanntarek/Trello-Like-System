package jms;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jms.JMSClient;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)

public class MessageService {

    @Inject
    JMSClient jmsClient;
   
   
}

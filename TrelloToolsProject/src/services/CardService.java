package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import entities.Card;

@Stateless
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CardService {
	
	@PersistenceContext(unitName="pu")
	private EntityManager entityManager;
	
	@Path("getCardDetails")
	@GET
	public Response getCardDetails(@QueryParam("cardId") long cardId) {
	    // Fetch the card based on the card ID
	    Card card = entityManager.find(Card.class, cardId);

	    // Validate that the card exists
	    if (card == null) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Card not found").build();
	    }

	    // Construct the response with card details
	    String cardDetails = "Card ID: " + card.getId() +
	            "\nCard Name: " + card.getname() +
	            "\nDescription: " + card.getdescription() +
	            "\nComments: " + card.getcomments() +
	            "\nStatus: " + card.getStatus();

	    return Response.status(Response.Status.OK)
	            .entity(cardDetails)
	            .build();
	}

	

}

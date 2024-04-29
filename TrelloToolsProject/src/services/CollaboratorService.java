package services;

import entities.Card;
import entities.Lists;

import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Stateless
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CollaboratorService {
    @PersistenceContext(unitName = "pu")
    private EntityManager entityManager;

    @Path("createCard")
    @POST
    public Response createCard(@QueryParam("listId") long ListId,
                               @QueryParam("cardName") String cardName) {
        // Find the list based on the list ID
        Lists list = entityManager.find(Lists.class, ListId);
        if (list == null)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("List not found").build();
        } else {
            Card newcard = new Card();
            newcard.setName(cardName);
            newcard.setLists(list);;

            //persist the card
            entityManager.persist(newcard);

            return Response.status(Response.Status.OK)
                    .entity("Card created successfully within the list!")
                    .build();

        }
    }
}
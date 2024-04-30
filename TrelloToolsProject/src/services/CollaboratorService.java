package services;

import entities.Board;
import entities.Card;
import entities.Collaborator;
import entities.Lists;
import entities.TeamLeader;
import entities.User;

import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
                               @QueryParam("cardName") String cardName,
                               @QueryParam("userId") long userId) {
        // Find the list based on the list ID
        Lists list = entityManager.find(Lists.class, ListId);
        if (list == null)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("List not found").build();
        } else {
        	if (!isAuthorized(userId, list)) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not authorized to create card").build();
            }

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
    @Path("moveCard")
    @POST
    public Response moveCard(@QueryParam("cardId") long cardId,
                             @QueryParam("targetListId") long targetListId,
                             @QueryParam("userId") long userId) {
        // Fetch the card and the target list
        Card card = entityManager.find(Card.class, cardId);
        Lists targetList = entityManager.find(Lists.class, targetListId);

        // Validate that the card and target list exist
        if (card == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Card not found").build();
        }
        if (targetList == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Target list not found").build();
        }
        else {
        	Lists originalList = card.getlists();
        	if (!isAuthorized(userId, originalList) || !isAuthorized(userId, targetList)) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not authorized to create card").build();
            }

        
        // Ensure the user is authorized to move the card (you might need additional authorization logic here)

        // Update the card's list reference
        card.setLists(targetList);

        // Persist the changes
        entityManager.merge(card);

        return Response.status(Response.Status.OK)
                .entity("Card moved successfully to the new list!")
                .build();
        }
    }
    @Path("updateCard")
    @POST
    public Response updateCard(@QueryParam("cardId") long cardId,
                               @QueryParam("description") String description,
                               @QueryParam("commentText") String commentText,
                               @QueryParam("userId") long userId) {
        // Fetch the card
        Card card = entityManager.find(Card.class, cardId);

        // Validate that the card exists
        if (card == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Card not found").build();
        }

        Lists list=card.getlists();
        if (!isAuthorized(userId, list)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authorized to create card").build();
        }
        // Flag to check if updates are performed
        boolean updated = false;

        // Update the card's description if provided
        if (description != null && !description.isEmpty()) {
            card.setdescription(description);
            updated = true;
        }

        // Add a new comment to the card if provided
        if (commentText != null && !commentText.isEmpty()) {
            card.setcomments(commentText);
            updated = true;
        }

        // Persist the card if any updates were made
        if (updated) {
            entityManager.merge(card);
        }

        return Response.status(Response.Status.OK)
                .entity("Card updated successfully!")
                .build();
    }
    private boolean isAuthorized(long userId, Lists list) {
        User user = entityManager.find(User.class, userId);
        if (user instanceof TeamLeader) {
        	TeamLeader teamLeader = (TeamLeader) user;
	        TypedQuery<Board> query = entityManager.createQuery("SELECT b FROM Board b WHERE b.teamLeader.id = :id", Board.class);
	        query.setParameter("id", teamLeader.getId());
	        List<Board> teamLeaderBoards = query.getResultList();
	        return teamLeaderBoards.contains(list.getBoard());
        } else if (user instanceof Collaborator) {
            Collaborator collaborator = (Collaborator) user;
            return collaborator.getBoards().contains(list.getBoard());
        }
        return false;
    }


}

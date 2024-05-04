package services;

import entities.Board;
import entities.Card;
import entities.Collaborator;
import entities.Lists;
import entities.TeamLeader;
import entities.User;
import jms.JMSClient;

import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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

    @Inject
    JMSClient jmsClient;
    @Path("createCard")
    @POST
    public Response createCard(@QueryParam("listId") long ListId,
                               @QueryParam("cardName") String cardName,
                               @QueryParam("storyPoints") int storyPoints,
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
            newcard.setLists(list);
            list.getcards().add(newcard);
            newcard.setStoryPoints(storyPoints); 
            list.setCards(list.getcards());
            //persist the card
            entityManager.persist(newcard);

            return Response.status(Response.Status.OK)
                    .entity("Card created successfully within the list!")
                    .build();

        }
    }
    @Path("addDetailsToCard")
    @POST
    public Response addDetailsToCard(@QueryParam("cardId") long cardId,
                                     @QueryParam("description") String description,
                                     @QueryParam("commentText") String commentText,
                                     @QueryParam("status") String status,
                                     @QueryParam("userId") long userId) {
        // Fetch the card
        Card card = entityManager.find(Card.class, cardId);

        // Validate that the card exists
        if (card == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Card not found").build();
        }

        Lists list = card.getlists();
        if (!isAuthorized(userId, list)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authorized to update card").build();
        }

        // Add description if provided
        if (description != null && !description.isEmpty()) {
            card.setdescription(description);
        }

        // Add comment if provided
        if (commentText != null && !commentText.isEmpty()) {
            card.addComment(commentText);
            String message = "New comment on card " + cardId + ": " + commentText;
            jmsClient.sendMessage(message);
        }

        // Update status if provided
        if (status != null && !status.isEmpty()) {
            card.setStatus(status);
        }

        // Persist the changes
        entityManager.merge(card);

        return Response.status(Response.Status.OK)
                .entity("Details added to card successfully!")
                .build();
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
    @PUT
    public Response updateCard(@QueryParam("cardId") long cardId,
                               @QueryParam("description") String description,
                               @QueryParam("commentText") String commentText,
                               @QueryParam("status") String status,
                               @QueryParam("userId") long userId, 
                               @QueryParam("commentIndex") int commentindex) {
    	
        // Fetch the card
        Card card = entityManager.find(Card.class, cardId);

        // Validate that the card exists
        if (card == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Card not found").build();
        }

        Lists list=card.getlists();
        if (!isAuthorized(userId, list)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authorized to update card").build();
        }
        // Flag to check if updates are performed
        boolean updated = false;

        // Update the card's description if provided
        if (description != null && !description.isEmpty()) {
            card.setdescription(description);
            updated = true;
            String message = "Description updated on card " + cardId + ": " + description;
            jmsClient.sendMessage(message);
        }

        // Add a new comment to the card if provided
        if (commentText != null && !commentText.isEmpty()) {
        	
        	 if (commentindex >= 0 && commentindex < card.getcomments().size()) {
     	        String message = "Comment updated on card " + cardId +" from "+card.getcomments().get(commentindex)+ " to: " + commentText;
        	        card.getcomments().set(commentindex, commentText);
        	        jmsClient.sendMessage(message);
        	    }
        	 updated=true;
        }

        // Update the card's status if provided
        if (status != null && !status.isEmpty()) {
            card.setStatus(status);
            updated = true;
            String message = "card status updated on card " + cardId + ": " + status;
            jmsClient.sendMessage(message);
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
    @Path("assignCard")
    @POST
    public Response assignCard(@QueryParam("cardId") long cardId, 
                               @QueryParam("userId") long userId, 
                               @QueryParam("assigneeId") long assigneeId) {
        // Fetch the card, user, and assignee
        Card card = entityManager.find(Card.class, cardId);
        User user = entityManager.find(User.class, userId);
        Collaborator assignee = entityManager.find(Collaborator.class, assigneeId);

        // Validate that the card, user, and assignee exist
        if (card == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Card not found").build();
        }
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        if (assignee == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Assignee not found").build();
        }

        // Ensure the user is authorized to assign the card
        Lists list = card.getlists();
        if (!isAuthorized(userId, list)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authorized to assign card").build();
        }

        // Ensure the assignee is part of the same board as the card
        Board board = list.getBoard();
        if (! assignee.getBoards().contains(board)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Assignee is not part of the board").build();
        }

        // Assign the card to the specified assignee
        card.setAssignee(assignee);

        // Persist the changes
        entityManager.merge(card);

        return Response.status(Response.Status.OK)
                .entity("Card assigned successfully to the specified assignee!")
                .build();
    }


}
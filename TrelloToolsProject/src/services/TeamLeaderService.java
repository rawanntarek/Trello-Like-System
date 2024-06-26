package services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import entities.Board;
import entities.Card;
import entities.Collaborator;
import entities.Lists;
import entities.Sprint;
import entities.TeamLeader;
import entities.User;

@Stateless
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeamLeaderService {
	@PersistenceContext(unitName="pu")
	private EntityManager entityManager;
	
	//unique name
	@Path("createBoard")
	@POST
	public Response createBoard(@QueryParam("id")long id,@QueryParam("board name") String boardName)
	{
		
			User user=entityManager.find(User.class, id);
			if(user==null|| !user.getRole().equals("Team Leader"))
			{
		        return Response.status(Response.Status.FORBIDDEN).entity("User is not authorized to create a board").build();

			}
			TeamLeader teamLeader = entityManager.find(TeamLeader.class, id);
		    if(teamLeader==null)
		    {
		        return Response.status(Response.Status.NOT_FOUND).entity("Team Leader not found").build();

		    }
		TypedQuery<Board> query = entityManager.createQuery("SELECT b FROM Board b WHERE b.name = :name", Board.class);
		    query.setParameter("name", boardName);
		    Board board=new Board();
		    try {
		    	 board=query.getSingleResult();
		    }
		    catch(NoResultException ex){
		    	
		 		board.setBoardName(boardName);
		 		board.setTeamLeader(teamLeader);
		 		entityManager.persist(board);
		 		createList(board,"todo");
		 		createList(board,"in progress");
		 		createList(board,"done");
		        return Response.status(Response.Status.OK).entity("Board Created successfully!").build();

		    }
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Board already exists").build();

		   
		
	        
	}
	private void createList(Board board, String listName) {
	    Lists listEntity = new Lists();
	    listEntity.setListName(listName);
	    listEntity.setBoard(board);
	    entityManager.persist(listEntity);
	}
	@Path("createSprint")
	@POST
	public Response createSprint(@QueryParam("id")long id,@QueryParam("sprint name") String sprintName,@QueryParam("start date") Date startDate,@QueryParam("end date")Date endDate,@QueryParam("BoardId") long boardId)
	{
		TeamLeader teamLeader = entityManager.find(TeamLeader.class, id);
	    if(teamLeader==null)
	    {
	        return Response.status(Response.Status.NOT_FOUND).entity("Team Leader not found").build();

	    }
	    Board board = entityManager.find(Board.class, boardId);
	    if (board == null) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Board not found").build();
	    }
	    Sprint newSprint= new Sprint();
	    newSprint.setName(sprintName);
	    newSprint.setStartDate(startDate);
	    newSprint.setEndDate(endDate);
	    newSprint.setBoard(board);
	    TypedQuery<Card> query = entityManager.createQuery(
	            "SELECT c FROM Card c WHERE c.lists.board.id = :boardId", Card.class);
	    query.setParameter("boardId", boardId);
	    List<Card> cards = query.getResultList();

	    newSprint.setCards(cards);
	    for(int i=0;i<cards.size();i++)
	    {
	    	cards.get(i).setSprint(newSprint);
	    }

	    entityManager.persist(newSprint);

	    TypedQuery<String> querynames = entityManager.createQuery(
	            "SELECT c.name FROM Card c WHERE c.lists.board.id = :boardId", String.class);

	        querynames.setParameter("boardId",boardId); // Assuming sprint has a board

	        List<String> cardsnames= querynames.getResultList();
	    
	    	 return Response.status(Response.Status.OK)
	 	            .entity("Sprint created with cards: " + cardsnames)
	 	            .build();	  
	    
	     
	}
	@Path("getBoards")
	@GET
	public Response getBoards (@QueryParam("id") long id)
	{
		 User useraccess = entityManager.find(User.class, id);
		    if(useraccess==null)
		    {
		        return Response.status(Response.Status.NOT_FOUND).entity("user not found").build();

		    }
		    List<Board> accessibleBoards = new ArrayList<>();

		    if (useraccess instanceof TeamLeader) {
		    	TeamLeader teamLeader = (TeamLeader) useraccess;
		        TypedQuery<Board> query = entityManager.createQuery("SELECT b FROM Board b WHERE b.teamLeader.id = :id", Board.class);
		        query.setParameter("id", teamLeader.getId());
		        List<Board> teamLeaderBoards = query.getResultList();
		        accessibleBoards.addAll(teamLeaderBoards);		  	    }
		    if (useraccess instanceof Collaborator) {
		        Collaborator collaborator = (Collaborator) useraccess;
		        List<Board> collaboratorBoards = collaborator.getBoards();
		        accessibleBoards.addAll(collaboratorBoards);
		    }

		    return Response.status(Response.Status.OK).entity(accessibleBoards).build();
		}
	@Path("getListsInBoard")
	@GET
	public Response getListsInBoard(@QueryParam("boardId") long boardId) {
	    Board board = entityManager.find(Board.class, boardId);
	    if (board == null) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Board not found").build();
	    }

	    TypedQuery<Lists> query = entityManager.createQuery("SELECT l FROM Lists l WHERE l.board.id = :boardId", Lists.class);
	    query.setParameter("boardId", boardId);
	    List<Lists> lists = query.getResultList();

	    List<String> listDetails = new ArrayList<>();
	    for (Lists list : lists) {
	        listDetails.add("List Name: " + list.getListName());
	    }

	    return Response.status(Response.Status.OK).entity(listDetails).build();
	}


	 
	
	@Path("invite")
	@POST
	public Response inviteCollaborator(@QueryParam("teamLeaderId") long teamLeaderId,@QueryParam("boardId") long boardId, @QueryParam("userId") long userId) {

		User user=entityManager.find(User.class, teamLeaderId);
		if(user==null|| !user.getRole().equals("Team Leader"))
		{
	        return Response.status(Response.Status.FORBIDDEN).entity("User is not authorized to invite a collaborator").build();

		}
	    TeamLeader teamLeader = entityManager.find(TeamLeader.class, teamLeaderId);
	    Board board = entityManager.find(Board.class, boardId);
	    Collaborator userToInvite = entityManager.find(Collaborator.class, userId);
	    
	    if (teamLeader == null) {
	        return Response.status(Response.Status.NOT_FOUND).entity("team leader not found").build();
	    }
	    if(board==null)
	    {
	        return Response.status(Response.Status.NOT_FOUND).entity("board not found").build();

	    }
	    if(userToInvite==null)
	    {
	        return Response.status(Response.Status.NOT_FOUND).entity("user not found").build();
	    }
	    
	    if (!board.getTeamLeader().equals(teamLeader)) {
	        return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
	    }

	    if (userToInvite.getBoards() != null && userToInvite.getBoards().contains(board)) {
	        return Response.status(Response.Status.CONFLICT).entity("User is already a collaborator").build();
	    }
	    board.getCollaborators().add(userToInvite);
	    userToInvite.getBoards().add(board);
	    entityManager.merge(board);
	    entityManager.merge(userToInvite);

	    return Response.status(Response.Status.OK).entity("User invited successfully").build();
	}
	@Path("deleteBoard")
	@DELETE
	public Response deleteBoard(@QueryParam("teamLeaderId") long teamLeaderId,@QueryParam("BoardId") long BoardId) {
		User user=entityManager.find(User.class, teamLeaderId);
		if(user==null|| !user.getRole().equals("Team Leader"))
		{
	        return Response.status(Response.Status.FORBIDDEN).entity("User is not authorized to delete a board").build();

		}
	    TeamLeader teamLeader = entityManager.find(TeamLeader.class, teamLeaderId);
	    Board board = entityManager.find(Board.class, BoardId);
	    
	    if (teamLeader == null) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Team Leader not found").build();
	    }
	    
	    if (board == null || !board.getTeamLeader().equals(teamLeader)) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Board not found or unauthorized access").build();
	    }
	    
	    entityManager.remove(board);
	    
	    return Response.status(Response.Status.OK).entity("Board deleted successfully!").build();
	}


	@Path("createList")
	@POST
	public Response createList(@QueryParam("teamLeaderId") long teamLeaderId,
	                           @QueryParam("boardId") long boardId,
	                           @QueryParam("listName") String listName) {
		User user=entityManager.find(User.class, teamLeaderId);
		if(user==null|| !user.getRole().equals("Team Leader"))
		{
	        return Response.status(Response.Status.FORBIDDEN).entity("User is not authorized to create a list").build();

		}
	    TeamLeader teamLeader = entityManager.find(TeamLeader.class, teamLeaderId);
	    Board board = entityManager.find(Board.class, boardId);
	    
	    if (teamLeader == null) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Team Leader not found").build();
	    }
	    
	    if (board == null || !board.getTeamLeader().equals(teamLeader)) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Board not found or unauthorized access").build();
	    }

	    Lists newList = new Lists();
	    newList.setListName(listName);
	    newList.setBoard(board);
	    board.getlists().add(newList);
	    board.setLists(board.getlists());

	    entityManager.persist(newList);
	    
	    return Response.status(Response.Status.CREATED).entity("List created successfully within the board!").build();
	}

	@Path("deleteList")
	@DELETE
	public Response deleteList(@QueryParam("teamLeaderId") long teamLeaderId,
	                           @QueryParam("listId") long listId) {
		User user=entityManager.find(User.class, teamLeaderId);
		if(user==null|| !user.getRole().equals("Team Leader"))
		{
	        return Response.status(Response.Status.FORBIDDEN).entity("User is not authorized to delete a list").build();

		}
	    TeamLeader teamLeader = entityManager.find(TeamLeader.class, teamLeaderId);
	    Lists list = entityManager.find(Lists.class, listId);
	    
	    if (teamLeader == null) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Team Leader not found").build();
	    }
	    
	    if (list == null || !list.getBoard().getTeamLeader().equals(teamLeader)) {
	        return Response.status(Response.Status.NOT_FOUND).entity("List not found or unauthorized access").build();
	    }
	    
	    entityManager.remove(list);
	    
	    return Response.status(Response.Status.OK).entity("List deleted successfully!").build();
	}

}

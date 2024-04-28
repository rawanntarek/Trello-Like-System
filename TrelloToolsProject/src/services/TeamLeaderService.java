package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import entities.Board;
import entities.TeamLeader;
import entities.User;

@Stateless
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeamLeaderService {
	@PersistenceContext(unitName="pu")
	private EntityManager entityManager;
	
	@Path("createBoard")
	@POST
	public Response createBoard(@QueryParam("id")long id,@QueryParam("board name") String boardName)
	{
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
		        return Response.status(Response.Status.OK).entity("Board Created successfully!").build();

		    }
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Board already exists").build();

		   
		
	}
	@Path("getBoards")
	@GET
	public Response getBoards (@QueryParam("id") long id)
	{
		 TeamLeader teamLeader = entityManager.find(TeamLeader.class, id);
		    if(teamLeader==null)
		    {
		        return Response.status(Response.Status.NOT_FOUND).entity("Team Leader not found").build();

		    }
		    try {
				TypedQuery<String> query = entityManager.createQuery("SELECT b.name FROM Board b WHERE b.teamLeader.id = :id", String.class);
				query.setParameter("id", id);
				List<String> boards=query.getResultList();
		        return Response.status(Response.Status.OK).entity(boards).build();


		    }
		    catch(Exception e)
		    {
		        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed").build();

		    }
	}
	
 

}

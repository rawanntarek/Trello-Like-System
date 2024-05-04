package services;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
import entities.Card;
import entities.Sprint;

@Stateless
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SprintService {
	
	@PersistenceContext(unitName="pu")
	private EntityManager entityManager;
	@Path("end")
	@POST
	public Response endSprint(@QueryParam("currentSprintId") long currentSprintId,
	                           @QueryParam("newSprintName") String newSprintName,
	                           @QueryParam("sprintDurationDays") int sprintDurationDays) {
	    Sprint currentSprint = entityManager.find(Sprint.class, currentSprintId);
	    if (currentSprint == null) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Current sprint not found").build();
	    }

	    // Update the end date of the current sprint
	    Date currentDate = new Date(System.currentTimeMillis());
	    currentSprint.setEndDate(currentDate);
	    entityManager.merge(currentSprint);

	    // Start a new sprint
	    Sprint newSprint = new Sprint();
	    newSprint.setName(newSprintName);
	    newSprint.setStartDate(currentDate); // Start date is the current date
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(currentDate);
	    calendar.add(Calendar.DAY_OF_YEAR, sprintDurationDays); // Add the sprint duration to get end date
	    Date endDate = (Date) calendar.getTime();
	    newSprint.setEndDate(endDate);
	    entityManager.persist(newSprint);

	    // Move unfinished tasks from the current sprint to the new sprint
	    List<Card> unfinishedTasks = getUnfinishedTasks(currentSprint);
	    for (Card task : unfinishedTasks) {
	        task.setSprint(newSprint);
	        task.setStatus("Pending"); // Set status to "Pending" in the new sprint
	        entityManager.merge(task);
	    }

	    return Response.status(Response.Status.OK)
	            .entity("Sprint ended and new sprint started successfully")
	            .build();
	}

	private List<Card> getUnfinishedTasks(Sprint sprint) {
	    TypedQuery<Card> query = entityManager.createQuery(
	            "SELECT c FROM Card c WHERE c.sprint = :sprint AND c.status != 'Done'",
	            Card.class);
	    query.setParameter("sprint", sprint);
	    return query.getResultList();
	}

	 @GET
	    @Path("tasks")
	    public Response getTasksInSprint(@QueryParam("sprintId") long sprintId) {
	        Sprint sprint = entityManager.find(Sprint.class, sprintId);
	        if (sprint == null) {
	            return Response.status(Response.Status.NOT_FOUND).entity("Sprint not found").build();
	        }
	        TypedQuery<String> query = entityManager.createQuery(
		            "SELECT c.name FROM Card c WHERE c.sprint.id = :sprintId", String.class);

	        query.setParameter("sprintId", sprintId);
	        List<String> cards = query.getResultList();

	        return Response.status(Response.Status.OK).entity(cards).build();
	 }

	    @GET
	    @Path("report")
	    public Response generateSprintReport(@QueryParam("sprintId") long sprintId) {
	        Sprint sprint = entityManager.find(Sprint.class, sprintId);
	        if (sprint == null) {
	            return Response.status(Response.Status.NOT_FOUND).entity("Sprint not found").build();
	        }

	        // Calculate total completed and uncompleted story points
	        TypedQuery<Long> completedPointsQuery = entityManager.createQuery(
	                "SELECT SUM(c.storyPoints) FROM Card c WHERE c.sprint = :sprint AND c.status = 'Done'",
	                Long.class);
	        completedPointsQuery.setParameter("sprint", sprint);
	        Long completedPoints = completedPointsQuery.getSingleResult();

	        TypedQuery<Long> uncompletedPointsQuery = entityManager.createQuery(
	                "SELECT SUM(c.storyPoints) FROM Card c WHERE c.sprint = :sprint AND c.status != 'Done'",
	                Long.class);
	        uncompletedPointsQuery.setParameter("sprint", sprint);
	        Long uncompletedPoints = uncompletedPointsQuery.getSingleResult();

	        String report = "Sprint Report:\n" +
	                        "Sprint Name: " + sprint.getName() + "\n" +
	                        "Start Date: " + sprint.getStartDate() + "\n" +
	                        "End Date: " + sprint.getEndDate() + "\n" +
	                        "Total Completed Story Points: " + completedPoints + "\n" +
	                        "Total Uncompleted Story Points: " + uncompletedPoints + "\n";

	        return Response.status(Response.Status.OK).entity(report).build();
	    }
 
}

package services;


import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import DTOs.UserDTO;
import entities.Collaborator;
import entities.TeamLeader;
import entities.User;



@Stateless
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class UserService {
	@PersistenceContext(unitName="pu")
	private EntityManager entityManager;
	public UserService()
	{
		
	}
	 
	@Path("signup")
	@POST
	public Response signUp(UserDTO user)
	{
		
		TypedQuery<User> query=entityManager.createQuery("SELECT u FROM User u WHERE   u.email=:email AND u.role=:role",User.class);
		query.setParameter("role", user.getRole());
		query.setParameter("email", user.getEmail());
		User Existing;
		try {
			Existing=query.getSingleResult();
		}
		catch(NoResultException ex)
		{
			if(user.getRole().equals("Team Leader"))
			{
				TeamLeader teamLeader=new TeamLeader();
				teamLeader.setUsername(user.getUsername());
				teamLeader.setPassword(user.getPassword());
				teamLeader.setEmail(user.getEmail());
				teamLeader.setRole(user.getRole());
				entityManager.persist(teamLeader);
	            return Response.status(Response.Status.OK).entity("Team Leader user registered successfully!").build();

				
			}
			else if(user.getRole().equals("Collaborator"))
			{
				Collaborator collaborator=new Collaborator();
				collaborator.setUsername(user.getUsername());
				collaborator.setPassword(user.getPassword());
				collaborator.setEmail(user.getEmail());
				collaborator.setRole(user.getRole());
				entityManager.persist(collaborator);
	            return Response.status(Response.Status.OK).entity("Collaborator user registered successfully!").build();

				
			}
			entityManager.persist(user);
            return Response.status(Response.Status.OK).entity("User registered successfully!").build();

		}

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User already exists").build();

		
}
	
	@Path("login")
	@POST
	public Response login(UserDTO user)
	{
	    TypedQuery<User> loginQ=entityManager.createQuery("SELECT u FROM User u WHERE u.email=:email AND u.password=:password AND u.role=:role",User.class);
		loginQ.setParameter("email", user.getEmail());
		loginQ.setParameter("password",user.getPassword());
		loginQ.setParameter("role",user.getRole());
		User Existing;
		try {
            Existing = loginQ.getSingleResult();
        } catch (NoResultException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
        return Response.status(Response.Status.OK).entity("Login successful with role: "+user.getRole()).build();

		

	}
	 @Path("getuser")
	    @GET
	    public Response getUserByID(@QueryParam("id") Long id) {
	        
	        User user=entityManager.find(User.class, id);
	        if (user == null) {
	            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
	        } else {
	            return Response.status(Response.Status.OK).entity(user).build();
	        }
	    }
	 @Path("updateUsername")
	 @PUT
	 public Response updateUsername (@QueryParam("id") Long id,String username)
	 {
		 User user=entityManager.find(User.class, id);
		 if(user==null)
		 {
	            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();

		 }
		 user.setUsername(username);
		 entityManager.merge(user);
         return Response.status(Response.Status.OK).entity("Username updated!").build();

	 }
	 @Path("updateEmail") //it doesnt refuse if email already exists
	 @PUT
	 public Response updateEmail (@QueryParam("id") Long id,String email)
	 {
		 User user=entityManager.find(User.class, id);
		 if(user==null)
		 {
	            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();

		 }
		 user.setEmail(email);
		 entityManager.merge(user);
         return Response.status(Response.Status.OK).entity("Email updated!,Sign up again to verify").build();

	 }
	 @Path("updatePassword")
	 @PUT
	 public Response updatePassword (@QueryParam("id") Long id,String password)
	 {
		 User user=entityManager.find(User.class, id);
		 if(user==null)
		 {
	            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();

		 }
		 user.setPassword(password);
		 entityManager.merge(user);
         return Response.status(Response.Status.OK).entity("Password updated!").build();

	 }
	 
	 

	

}

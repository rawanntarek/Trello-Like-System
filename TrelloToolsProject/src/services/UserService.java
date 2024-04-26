package services;


import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	public Response signUp(User user)
	{
		try {
		TypedQuery<User> usernameQ=entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username",User.class);
		usernameQ.setParameter("username", user.getUsername());
		List<User>usernameExists=usernameQ.getResultList();
		TypedQuery<User> emailQ=entityManager.createQuery("SELECT u FROM User u WHERE u.email=:email",User.class);
		emailQ.setParameter("email", user.getEmail());
		List<User>emailExists=emailQ.getResultList();
		if(!usernameExists.isEmpty() && !emailExists.isEmpty())
		{
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("email & username already exists").build();

		}
		else if (!usernameExists.isEmpty() || !emailExists.isEmpty())
		{
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("email or username already exists").build();

		}
		else
		{
			entityManager.persist(user);
	        return Response.status(Response.Status.OK).entity("User registered successfully!").build();
		
		}
		
		}
		catch (Exception e)
		{
			 e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to register").build();
 
		}
	}

}

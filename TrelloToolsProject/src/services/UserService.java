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
	public String signUp(User user)
	{
		entityManager.persist(user);
		return  ("user registered successfully!") ;	
	}

}

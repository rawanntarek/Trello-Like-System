package entities;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Stateless
@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String username;
	@NotNull
	private String email;
	@NotNull
	private String password;
	@NotNull
	private String role;

	public void setID(long id)
	{
		this.id=id;
	}
	public void setUsername(String username)
	{
		this.username=username;
	}
	public void setEmail(String email)
	{
		this.email=email;
	}
	public void setPassword(String password)
	{
		this.password=password;
	}
	public String getUsername()
	{
		return username;
	}
	public Long getId()
	{
		return id;
	}
	public String getEmail()
	{
		return email;
	}
	public String getPassword()
	{
		return password;
	}
	public void setRole(String role)
	{
		this.role=role;
	}
	public String getRole()
	{
		return role;
	}
	
}

package entities;


import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Stateless
@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String username;
	private String email;
	private String password;
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

}

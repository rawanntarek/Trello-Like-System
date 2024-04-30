package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
public class Card {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String description;
	private String comments;
	@ManyToOne
	private Lists lists;

	private String name;
	@ManyToOne
	@JoinColumn(name = "assignee_id")
	private User assignee;
	
	private String status;

	
	public long getId()
	{
		return id;
	}
	public String getname()
	{
		return name;
	}
	public String getdescription()
	{
		return description;
	}
	public String getcomments()
	{
		return comments;
	}
	public Lists getlists()
	{
		return lists;
	}
	public void setID(int id)
	{
		this.id=id;
	}
	public void setdescription(String description)
	{
		this.description=description;
	}
	public void setcomments(String comments)
	{
		this.comments=comments;
	}
	public void setLists(Lists lists)
	{
		this.lists=lists;
	}

	public void setName(String name) {this.name=name;}
	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status=status;
	}
	


}
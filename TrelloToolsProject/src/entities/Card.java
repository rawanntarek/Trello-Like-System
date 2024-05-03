package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Card {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String description;
	private List<String>comments=new ArrayList<String>();
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
	public List<String> getcomments()
	{
		return comments;
	}
	public Lists getlists()
	{
		return lists;
	}
	 public void addComment(String comment) {
	        this.comments.add(comment);
	    }

	    public void removeComment(String comment) {
	        this.comments.remove(comment);
	    }
	public void setID(int id)
	{
		this.id=id;
	}
	public void setdescription(String description)
	{
		this.description=description;
	}
	public void setcomments(List<String> comments)
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
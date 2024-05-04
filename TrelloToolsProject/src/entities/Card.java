package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	@ElementCollection(targetClass=String.class , fetch = FetchType.LAZY)
	private List<String>comments=new ArrayList<>();
	@ManyToOne
	private Lists lists;

	private String name;
	@ManyToOne
	@JoinColumn(name = "assignee_id")
	private User assignee;
	
	@ManyToOne
	private Sprint sprint;
	
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
	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
	}
	public Sprint getSprint()
	{
		return sprint;
	}


}
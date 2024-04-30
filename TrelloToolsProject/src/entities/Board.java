package entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
@Entity
public class Board {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	@ManyToOne
	private TeamLeader teamLeader;
	@Transient
	@ManyToMany(fetch=FetchType.EAGER)
	private List<Collaborator> collaborators = new ArrayList<Collaborator>();
	@Transient
	@OneToMany(mappedBy="board")
	@Size(min=3,message="at least 3 lists")
	private List<Lists> lists;
	
	public long getId()
	{
		return id;
	}
	public String getBoardName()
	{
		return name;
	}
	public TeamLeader getTeamLeader()
	{
		return teamLeader;
	}
	public List<Lists> getlists()
	{
		return lists;
	}
	public List<Collaborator> getCollaborators()
	{
		return collaborators;
	}
	public void setID (long id)
	{
		this.id=id;
	}
	public void setBoardName(String name)
	{
		this.name=name;
	}
	public void setTeamLeader(TeamLeader teamLeader)
	{
		this.teamLeader=teamLeader;
	}
	public void setLists(List<Lists> lists)
	{
		this.lists=lists;
	}
}

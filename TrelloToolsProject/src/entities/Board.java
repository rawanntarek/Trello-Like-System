package entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
@Entity
public class Board {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	@ManyToOne
	private TeamLeader teamLeader;
	
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

package entities;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
@Entity
@Table(name = "board_table")
public class Board implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	@ManyToOne
	private TeamLeader teamLeader;

	@ManyToMany(fetch=FetchType.EAGER)
	private List<Collaborator> collaborators = new ArrayList<Collaborator>();

	@OneToMany(fetch=FetchType.EAGER,mappedBy="board")
	private List<Lists> lists= new ArrayList<Lists>();
	@OneToMany(mappedBy="board" ,fetch = FetchType.LAZY)
	private List<Sprint> sprint;
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

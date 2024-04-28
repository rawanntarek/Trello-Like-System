package entities;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

public class Lists {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	@ManyToOne
	private Board board;
	@OneToMany(mappedBy="Cards")
	@Size(min=1,message="at least 1 card")
	private List<Card> cards;
	
	public long getId()
	{
		return id;
	}
	public String getListName()
	{
		return name;
	}
	public Board getBoard()
	{
		return board;
	}
	

}

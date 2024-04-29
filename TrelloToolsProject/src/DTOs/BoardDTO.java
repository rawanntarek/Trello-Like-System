package DTOs;

import java.util.List;

public class BoardDTO {
    private long id;
    private String name;
    private TeamLeaderDTO teamLeader;
    private List<UserDTO> collaborators;
    private List<ListsDTO> lists;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamLeaderDTO getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(TeamLeaderDTO teamLeader) {
        this.teamLeader = teamLeader;
    }

    public List<UserDTO> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<UserDTO> collaborators) {
        this.collaborators = collaborators;
    }

    public List<ListsDTO> getLists() {
        return lists;
    }

    public void setLists(List<ListsDTO> lists) {
        this.lists = lists;
    }
}
